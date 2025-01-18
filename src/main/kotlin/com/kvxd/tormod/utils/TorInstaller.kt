package com.kvxd.tormod.utils

import com.kvxd.tormod.TorMod
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import java.io.BufferedInputStream
import java.net.URI

object TorInstaller {

    val TOR_DIR = FabricLoader.getInstance().gameDir.resolve("tor-${TorMod.config.torVersion}").toFile()

    private fun download(url: URL, destination: Path): File {
        url.openStream().use { stream ->
            Files.copy(stream, destination, StandardCopyOption.REPLACE_EXISTING)
        }
        return destination.toFile()
    }

    private fun extractTarGz(file: File, destination: File) = BufferedInputStream(file.inputStream()).use { bis ->
        GzipCompressorInputStream(bis).use { gzis ->
            TarArchiveInputStream(gzis).use { tar ->
                generateSequence { tar.nextEntry }
                    .forEach { entry ->
                        destination.resolve(entry.name).let { entryFile ->
                            if (entry.isDirectory) entryFile.mkdirs()
                            else {
                                entryFile.parentFile.mkdirs()
                                Files.copy(tar, entryFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                            }
                        }
                    }
            }
        }
    }

    fun isInstalled(): Boolean {
        return TOR_DIR.exists() && TOR_DIR.isDirectory && TOR_DIR.listFiles()?.isNotEmpty() == true
    }

    fun install() {
        if (isInstalled()) {
            TorMod.logger.info("TOR is already installed.")
            return
        }

        TOR_DIR.mkdirs()

        val torUrl =
            URI("https://archive.torproject.org/tor-package-archive/torbrowser/${TorMod.config.torVersion}/tor-expert-bundle-${PlatformUtils.platform.name.lowercase()}-${PlatformUtils.architecture.name.lowercase()}-${TorMod.config.torVersion}.tar.gz").toURL()
        val tarGzFile = TOR_DIR.resolve("tor-${TorMod.config.torVersion}.tar.gz").toPath()

        TorMod.logger.info("Downloading TOR from ${torUrl}...")
        download(torUrl, tarGzFile)

        TorMod.logger.info("Extracting TOR...")
        extractTarGz(tarGzFile.toFile(), TOR_DIR)

        TorMod.logger.info("TOR installation complete.")
    }
}