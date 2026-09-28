package mageaddons.mixins;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    // Hook render ticks for per-frame updates
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(CallbackInfo ci) {
        try {
            // Per-frame updates can go here
        } catch (Exception ignored) {}
    }
}
