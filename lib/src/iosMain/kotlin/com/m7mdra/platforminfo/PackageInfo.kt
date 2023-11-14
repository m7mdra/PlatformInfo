package com.m7mdra.platforminfo

import platform.Foundation.NSBundle

/**
 * Provides information about the current software package on iOS.
 */
actual object PackageInfo {

    /**
     * Retrieves information about the current software package on iOS.
     *
     * @return A [Package] object representing the current package information.
     */
    actual fun current(): Package {
        val bundle = NSBundle.mainBundle
        val installStore = installedFrom()
        return Package(
            appName = bundle.objectForInfoDictionaryKey("CFBundleDisplayName").toString(),
            packageName = bundle.bundleIdentifier ?: "",
            version = bundle.objectForInfoDictionaryKey("CFBundleShortVersionString").toString(),
            build = bundle.objectForInfoDictionaryKey("CFBundleVersion").toString(),
            installerStore = installStore,
            buildSignature = ""
        )
    }

    /**
     * Determines the installer store based on the app's origin.
     *
     * @return A string representing the installer store (e.g., "com.apple", "com.apple.testflight", "com.apple.simulator").
     */
    private fun installedFrom(): String {
        val bundle = NSBundle.mainBundle

        val appStoreReceipt = bundle.appStoreReceiptURL?.path

        return if (appStoreReceipt?.contains("CoreSimulator") == true) {
            "com.apple.simulator"
        } else {
            if (appStoreReceipt?.contains("sandboxReceipt") == true) {
                "com.apple.testflight"
            } else {
                "com.apple"
            }
        }
    }
}
