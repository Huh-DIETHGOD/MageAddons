package mageaddons.mixins;

import io.netty.channel.ChannelHandlerContext;
import mageaddons.events.PacketEvent;
import mageaddons.utils.ServerUtils;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mageaddons.utils.UtilsKt.postAndCatch;


@Mixin(value = {NetworkManager.class}, priority = 1001)
public abstract class MixinNetworkManager {
    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        if (postAndCatch(new PacketEvent.Receive(packet)) && !ci.isCancelled()) ci.cancel();
    }

    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (!ServerUtils.handleSendPacket(packet))
            if (postAndCatch(new PacketEvent.Send(packet)) && !ci.isCancelled()) ci.cancel();
    }

}
