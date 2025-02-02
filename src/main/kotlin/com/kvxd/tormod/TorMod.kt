package com.kvxd.tormod

import com.kvxd.tormod.utils.PlatformUtils
import com.kvxd.tormod.utils.TorInstaller
import com.kvxd.tormod.utils.TorRunner
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.option.OptionsScreen
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.gui.screen.TitleScreen
import java.util.concurrent.atomic.AtomicBoolean

class TorMod : ClientModInitializer {

    companion object {
        val logger: Logger = LoggerFactory.getLogger("TorMod")
        lateinit var config: TorModConfig
    }

    private lateinit var holder: ConfigHolder<TorModConfig>
    private val initialized = AtomicBoolean(false)

    override fun onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(::onClientTick)
        ClientLifecycleEvents.CLIENT_STOPPING.register(::onClientStopping)
    }

    private fun onClientTick(client: MinecraftClient) {
        if (!initialized.get() && client.currentScreen is TitleScreen) {
            initialized.set(true)
            initializeMod(client)
        }
    }

    private fun initializeMod(client: MinecraftClient) {
        logger.info("TorMod running on ${PlatformUtils.platform}")

        AutoConfig.register(
            TorModConfig::class.java, ::GsonConfigSerializer
        )

        holder = AutoConfig.getConfigHolder(TorModConfig::class.java)
        config = holder.config

        if (!TorInstaller.isInstalled())
            TorInstaller.install()
        else
            logger.info("Tor is already installed.")

        if (config.enabled)
            TorRunner.startTor()
    }

    private fun onClientStopping(client: MinecraftClient) {
        holder.save()
        TorRunner.stopTor()
        TorRunner.shutdown()
    }
}