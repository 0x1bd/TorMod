package com.kvxd.tormod.mixin;

import com.kvxd.tormod.TorMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        int textColor = TorMod.config.getEnabled() ? Colors.GREEN : Colors.RED;

        ButtonWidget button = ButtonWidget.builder(Text.translatable("text.tormod.title"), btn -> {
                    TorMod.config.setEnabled(!TorMod.config.getEnabled());
                    btn.setMessage(Text.translatable("text.tormod.title").styled(style -> style.withColor(TorMod.config.getEnabled() ? Colors.GREEN : Colors.RED)));
                })
                .position(5, 5)
                .width(60)
                .build();

        button.setMessage(Text.translatable("text.tormod.title").styled(style -> style.withColor(textColor)));

        addDrawableChild(button);
    }

}
