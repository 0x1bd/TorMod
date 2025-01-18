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
        get() = System.getProperty("os.name").lowercase().let { os ->
            when {
                os.contains("win") -> Platform.WINDOWS
                os.contains("linux") -> Platform.LINUX
                os.contains("mac") -> Platform.MACOS
                else -> throw UnsupportedOperationException("Unsupported platform: $os")
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