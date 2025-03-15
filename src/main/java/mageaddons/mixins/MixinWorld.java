package mageaddons.mixins;

import mageaddons.events.EntityLeaveWorldEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mageaddons.utils.UtilsKt.postAndCatch;

@Mixin(World.class)
public abstract class MixinWorld {
    @Inject(method = "removeEntity", at = @At("HEAD"))
    private void onRemoveEntity(Entity entityIn, CallbackInfo ci) {
        postAndCatch(new EntityLeaveWorldEvent(entityIn));
    }
}
