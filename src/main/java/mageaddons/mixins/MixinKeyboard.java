package mageaddons.mixins;

import mageaddons.core.ModuleManager;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {

    @Shadow
    @Final
    private MinecraftClient client;

    // Hook key presses for hotkey features
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (this.client.currentScreen != null) return;
        if (action != 0) return; // Only handle press events

        try {
            // Check module keybindings
            ModuleManager.INSTANCE.getModules().forEach(module -> {
                module.getKeyBindings().forEach(keyBinding -> {
                    if (keyBinding.matchesKey(key, scancode)) {
                        module.toggle();
                    }
                });
            });
        } catch (Exception ignored) {}
    }
}
