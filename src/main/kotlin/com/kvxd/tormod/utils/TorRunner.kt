package com.kvxd.tormod.utils

import com.kvxd.tormod.TorMod
import java.io.File
import java.io.IOException

object TorRunner {

    private var torProcess: Process? = null

    fun startTor() {
        val executable = TorInstaller.TOR_DIR
            .resolve("tor")
            .resolve("tor") // tor executable

        try {
            makeExecutable(executable)
            startTorProcess(executable)
        } catch (e: IOException) {
            TorMod.logger.error("Failed to start Tor", e)
        } catch (e: InterruptedException) {
            TorMod.logger.error("Tor process was interrupted", e)
        }
    }

    fun stopTor() {
        torProcess?.let { process ->
            try {
                process.destroy()
                process.waitFor()
                TorMod.logger.info("Tor process stopped")
            } catch (e: InterruptedException) {
                TorMod.logger.error("Failed to stop Tor process", e)
            } finally {
                torProcess = null
            }
        }
    }

    private fun makeExecutable(executable: File) {
        if (PlatformUtils.platform == PlatformUtils.Platform.LINUX || PlatformUtils.platform == PlatformUtils.Platform.MACOS) {
            val process = ProcessBuilder("chmod", "+x", executable.toString())
                .inheritIO()
                .start()
            process.waitFor()
        }
    }

    private fun startTorProcess(executable: File) {
        val torProcessBuilder = ProcessBuilder(executable.toString(), "--SocksPort", TorMod.config.port.toString())
            .inheritIO()

        // Set LD_LIBRARY_PATH for Linux
        // Required because otherwise tor does not use the correct libraries
        if (PlatformUtils.platform == PlatformUtils.Platform.LINUX) {
            val torDir = executable.parentFile.absolutePath
            val env = torProcessBuilder.environment()
            env["LD_LIBRARY_PATH"] = torDir
        }

        torProcess = torProcessBuilder.start()
        TorMod.logger.info("Tor started")
    }

}