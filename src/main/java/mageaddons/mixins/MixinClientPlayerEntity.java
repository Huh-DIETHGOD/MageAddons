package mageaddons.mixins;

import mageaddons.events.EventDispatcher;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayerEntity {

    // Hook chat message sending (sendChatMessage lives on ClientPlayNetworkHandler in 1.21.1)
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String content, CallbackInfo ci) {
        try {
            EventDispatcher.INSTANCE.onMessageSent(content);
        } catch (Exception ignored) {}
    }
}
