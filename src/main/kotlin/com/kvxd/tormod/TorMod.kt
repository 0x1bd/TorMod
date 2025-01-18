package com.kvxd.tormod

import com.kvxd.tormod.utils.TorInstaller
import com.kvxd.tormod.utils.TorRunner
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.client.MinecraftClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TorMod : ClientModInitializer {

    companion object {
        val logger: Logger = LoggerFactory.getLogger("TorMod")
        lateinit var config: TorModConfig
    }

    private lateinit var holder: ConfigHolder<TorModConfig>

    override fun onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(::onClientStarted)
        ClientLifecycleEvents.CLIENT_STOPPING.register(::onClientStopping)
    }

    private fun onClientStarted(client: MinecraftClient) {
        AutoConfig.register(
            TorModConfig::class.java, ::GsonConfigSerializer
        )

        holder = AutoConfig.getConfigHolder(TorModConfig::class.java)

        config = holder.config

        if (!config.enabled) return

        if (!TorInstaller.isInstalled())
            TorInstaller.install()
        else
            logger.info("TOR is already installed.")

        TorRunner.startTor()
    }

    private fun onClientStopping(client: MinecraftClient) {
        holder.save()
        TorRunner.stopTor()
    }

}
