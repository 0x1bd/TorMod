package com.kvxd.tormod.config

import com.kvxd.tormod.TorModConfig
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screen.Screen

class ModMenuIntegration : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent: Screen? ->
            AutoConfig.getConfigScreen(
                TorModConfig::class.java, parent
            ).get()
        }
    }

}