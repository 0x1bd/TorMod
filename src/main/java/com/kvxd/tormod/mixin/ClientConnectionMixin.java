package com.kvxd.tormod.mixin;

import com.kvxd.tormod.TorMod;
import com.kvxd.tormod.TorModConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.proxy.Socks5ProxyHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.handler.PacketSizeLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "addHandlers", at = @At("RETURN"))
    private static void addHandler(ChannelPipeline pipeline, NetworkSide side, boolean local, PacketSizeLogger packetSizeLogger, CallbackInfo ci) {
        if (!TorMod.config.getEnabled()) return;
        if (side != NetworkSide.CLIENTBOUND) return;

        pipeline.addFirst(new Socks5ProxyHandler(new InetSocketAddress("localhost", TorMod.config.getPort())));
    }

}
