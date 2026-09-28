package mageaddons.mixins;

import mageaddons.config.Config;
import mageaddons.features.dungeon.MapRender;
import mageaddons.features.dungeon.BlessingDisplay;
import mageaddons.utils.Location;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {

    // Render the dungeon map and blessing display after the vanilla HUD
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        try {
            // Map rendering is handled by HudRenderCallback in the main class
            // This is a fallback if the callback isn't sufficient
        } catch (Exception ignored) {}
    }
}
