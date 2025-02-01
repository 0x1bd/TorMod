package com.kvxd.tormod.utils

import com.kvxd.tormod.TorMod
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors

object TorRunner {

    private var torProcess: Process? = null
    private val executor = Executors.newSingleThreadExecutor()

    enum class Status {
        Starting,
        Ready,
        Stopped
    }

    var status: Status = Status.Stopped

    fun startTor() {
        status = Status.Starting
        val executable = TorInstaller.TOR_DIR
            .resolve("tor")
            .resolve("tor") // tor executable

        try {
            makeExecutable(executable)
            startTorProcess(executable)
        } catch (e: IOException) {
            TorMod.logger.error("Failed to start Tor", e)
            status = Status.Stopped
        } catch (e: InterruptedException) {
            TorMod.logger.error("Tor process was interrupted", e)
            status = Status.Stopped
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
        status = Status.Stopped
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
        status = Status.Starting
        val torProcessBuilder = ProcessBuilder(executable.toString(), "--SocksPort", TorMod.config.port.toString())
            .redirectErrorStream(true) // Combine stdout and stderr

        // Set LD_LIBRARY_PATH for Linux
        if (PlatformUtils.platform == PlatformUtils.Platform.LINUX) {
            val torDir = executable.parentFile.absolutePath
            val env = torProcessBuilder.environment()
            env["LD_LIBRARY_PATH"] = torDir
        }

        torProcess = torProcessBuilder.start()
        TorMod.logger.info("Tor started")

        // Monitor the Tor process output
        executor.submit {
            monitorTorOutput(torProcess!!.inputStream)
        }
    }

    private fun monitorTorOutput(inputStream: InputStream) {
        val reader = inputStream.bufferedReader()
        try {
            reader.useLines { lines ->
                lines.forEach { line ->
                    TorMod.logger.info("Tor: $line") // Log Tor output

                    // Update status based on log messages
                    when {
                        line.contains("Bootstrapped 100% (done): Done") -> {
                            status = Status.Ready
                            TorMod.logger.info("Tor is ready")
                        }
                        line.contains("Catching signal TERM, exiting cleanly.") -> {
                            status = Status.Stopped
                            TorMod.logger.info("Tor has stopped cleanly")
                        }
                    }
                }
            }
        } catch (e: IOException) {
            if (e.message == "Stream closed") {
                TorMod.logger.warn("Tor process output stream was closed unexpectedly")
            } else {
                TorMod.logger.error("Error reading Tor process output", e)
            }
        } catch (e: Exception) {
            TorMod.logger.error("Unexpected error reading Tor process output", e)
        } finally {
            try {
                reader.close()
            } catch (e: IOException) {
                TorMod.logger.error("Error closing Tor process output stream", e)
            }
        }
    }

    fun shutdown() {
        executor.shutdown()
    }

}