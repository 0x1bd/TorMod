package com.kvxd.tormod

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigHolder
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.minecraft.client.MinecraftClient

class TorMod : ClientModInitializer {

    companion object {
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
    }

    private fun onClientStopping(client: MinecraftClient) {
        holder.save()
    }

}
