package com.m7mdra.platforminfo

/**
 * Represents a software package with essential information.
 *
 * @property appName The name of the application.
 * @property packageName The unique identifier of the package.
 * @property version The version of the package.
 * @property build The build number of the package.
 * @property buildSignature The cryptographic signature of the package build.
 * @property installerStore The store or platform from which the package was installed (nullable).
 */
class Package(
    val appName: String,
    val packageName: String,
    val version: String,
    val build: String,
    val buildSignature: String,
    val installerStore: String?
) {
    /**
     * Returns a string representation of the [Package] object.
     *
     * @return A formatted string containing package information.
     */
    override fun toString(): String {
        return "Package(appName='$appName', " +
                "packageName='$packageName'," +
                "version='$version'," +
                "build='$build'," +
                "buildSignature='$buildSignature'," +
                "installerStore=$installerStore)"
    }
}
