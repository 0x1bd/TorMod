package com.kvxd.tormod.utils

object PlatformUtils {

    enum class Platform {
        WINDOWS,
        LINUX,
        MACOS
    }

    enum class Architecture {
        X86_64,
        I686
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

    val architecture: Architecture
        get() = System.getProperty("os.arch").uppercase().let { arch ->
            when {
                arch.contains("64") -> Architecture.X86_64
                arch.contains("86") -> Architecture.I686
                else -> throw UnsupportedOperationException("Unsupported architecture: $arch")
            }
        }

    val architectureString = architecture.name.lowercase()

}