package com.kvxd.tormod.utils

object PlatformUtils {

    enum class Platform {
        WINDOWS,
        LINUX,
        MACOS
    }

    val platform: Platform
        get() = OSUtils.os.family.let { osFamily ->
            when {
                osFamily.contains("Windows", ignoreCase = true) -> Platform.WINDOWS
                osFamily.contains("Linux", ignoreCase = true) -> Platform.LINUX
                osFamily.contains("Mac OS X", ignoreCase = true) || osFamily.equals(
                    "macOS",
                    ignoreCase = true
                ) -> Platform.MACOS

                else -> throw UnsupportedOperationException("Unsupported platform: $osFamily")
            }
        }

    val platformString = platform.name.lowercase()

}