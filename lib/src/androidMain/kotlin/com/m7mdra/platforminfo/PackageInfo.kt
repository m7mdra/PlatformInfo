package com.m7mdra.platforminfo

import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Provides information about the current software package on Android.
 */
actual object PackageInfo {
    // The Android application context
    private var applicationContext: android.content.Context? = null

    // Lazily initializes the context
    private val context
        get() = applicationContext
            ?: error("Android context has not been set. Please call setContext in your Application's onCreate.")

    /**
     * Retrieves information about the current software package on Android.
     *
     * @return A [Package] object representing the current package information.
     */
    actual fun current(): Package {
        // Get package information
        val packageManager = context.packageManager
        val info = packageManager.getPackageInfo(context.packageName, 0)

        // Get package build signature
        val buildSignature = getBuildSignature(packageManager)

        // Get installer package information
        val installerPackage = getInstallerPackageName()

        // Create and return Package object
        return Package(
            appName = info.applicationInfo.loadLabel(packageManager).toString(),
            packageName = context.packageName,
            version = info.versionName,
            build = getLongVersionCode(info).toString(),
            buildSignature = buildSignature ?: "",
            installerStore = installerPackage
        )
    }

    /**
     * Retrieves the installer package name.
     * Using initiatingPackageName on Android 11 and newer.
     */
    private fun getInstallerPackageName(): String? {
        val packageManager = context.packageManager
        val packageName = context.packageName
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            packageManager.getInstallSourceInfo(packageName).initiatingPackageName
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstallerPackageName(packageName)
        }
    }

    /**
     * Converts the version code to a long.
     */
    @Suppress("deprecation")
    private fun getLongVersionCode(info: android.content.pm.PackageInfo): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
    }

    /**
     * Retrieves the package build signature.
     */
    @Suppress("deprecation", "PackageManagerGetSignatures")
    private fun getBuildSignature(pm: PackageManager): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                val signingInfo = packageInfo.signingInfo ?: return null

                if (signingInfo.hasMultipleSigners()) {
                    signatureToSha1(signingInfo.apkContentsSigners.first().toByteArray())
                } else {
                    signatureToSha1(signingInfo.signingCertificateHistory.first().toByteArray())
                }
            } else {
                val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                val signatures = packageInfo.signatures

                if (signatures.isNullOrEmpty() || packageInfo.signatures.first() == null) {
                    null
                } else {
                    signatureToSha1(signatures.first().toByteArray())
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            null
        } catch (e: NoSuchAlgorithmException) {
            null
        }
    }

    /**
     * Converts a signature to SHA1.
     */
    @Throws(NoSuchAlgorithmException::class)
    private fun signatureToSha1(sig: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA1")
        digest.update(sig)
        val hashText = digest.digest()
        return bytesToHex(hashText)
    }

    /**
     * Converts bytes to hexadecimal.
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}
