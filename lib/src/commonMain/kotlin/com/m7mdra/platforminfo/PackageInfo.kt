package com.m7mdra.platforminfo
/**
 * Provides information about the current software package on the calling platform.
 */
expect object PackageInfo {

    /**
     * Retrieves information about the current software package on the calling platform.
     *
     * @return A [Package] object representing the current package information.
     */
    fun current(): Package
}