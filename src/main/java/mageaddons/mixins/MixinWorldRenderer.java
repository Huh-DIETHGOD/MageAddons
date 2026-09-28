package mageaddons.mixins;

import mageaddons.config.Config;
import mageaddons.features.dungeon.WitherDoorESP;
import mageaddons.features.dungeon.WitherDragonManager;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow
    private ClientWorld world;

    // Hook before entity rendering for 3D ESP features
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
    private void afterRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera,
                             GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager,
                             Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
        // Render ESP features here
        try {
            // The actual rendering is handled by the fabric WorldRenderEvents
        } catch (Exception ignored) {}
    }
}
