package com.kvxd.tormod.gui

import com.kvxd.tormod.TorMod
import com.kvxd.tormod.utils.OSUtils
import com.kvxd.tormod.utils.TorRunner
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.text.Text

class PortPromptScreen(private val parent: Screen) : Screen(Text.translatable("text.tormod.port_in_use")) {

    override fun init() {
        val centerX = width / 2
        val centerY = height / 2
        val buttonWidth = 120
        val spacing = 10

        val totalWidth = buttonWidth * 3 + spacing * 2

        val startX = centerX - totalWidth / 2

        addDrawableChild(ButtonWidget.builder(Text.translatable("text.tormod.terminate_process")) { _ ->
            OSUtils.terminateProcessOnPort(TorMod.config.port)
            TorRunner.startTor()
            client!!.setScreen(parent)
        }.position(startX, centerY).width(buttonWidth).build())

        addDrawableChild(ButtonWidget.builder(Text.translatable("text.tormod.disable")) { _ ->
            TorMod.config.enabled = false
            TorRunner.status = TorRunner.Status.Stopped
            client!!.setScreen(parent)
        }.position(startX + buttonWidth + spacing, centerY).width(buttonWidth).build())

        addDrawableChild(ButtonWidget.builder(Text.translatable("text.tormod.use_system_daemon")) { _ ->
            TorMod.useSystem = true
            TorRunner.status = TorRunner.Status.Stopped
            client!!.setScreen(parent)
        }.position(startX + (buttonWidth + spacing) * 2, centerY).width(buttonWidth).build())
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(context, mouseX, mouseY, delta)
        super.render(context, mouseX, mouseY, delta)

        context.drawCenteredTextWithShadow(textRenderer, title.string, width / 2, height / 2 - 70, 0xFFFFFF)

        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.translatable("text.tormod.port_in_use.description").string.replace(
                "{0}",
                TorMod.config.port.toString()
            ),
            width / 2,
            height / 2 - 50,
            0xAAAAAA
        )
    }
}