package mageaddons.mixins;

import mageaddons.events.PostEntityMetadata;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mageaddons.utils.UtilsKt.postAndCatch;

@Mixin({S1CPacketEntityMetadata.class})
public class MixinS1CPacketEntityMetadata {
    @Redirect(method = {"processPacket(Lnet/minecraft/network/play/INetHandlerPlayClient;)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/network/play/INetHandlerPlayClient;handleEntityMetadata(Lnet/minecraft/network/play/server/S1CPacketEntityMetadata;)V"))
    private void redirectProcessPacket(INetHandlerPlayClient instance, S1CPacketEntityMetadata packet) {
        instance.handleEntityMetadata(packet);
        postAndCatch(new PostEntityMetadata(packet));
    }
}
