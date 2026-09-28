package mageaddons.mixins;

import mageaddons.events.EntityLeaveWorldEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    private World world;

    // Fire entity leave event when remove is called
    @Inject(method = "remove", at = @At("HEAD"))
    private void onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if (this.world != null && !this.world.isClient) return;

        // This is a client-side entity being removed
        try {
            EntityLeaveWorldEvent event = new EntityLeaveWorldEvent((Entity) (Object) this);
            // Post the event via our event system
            mageaddons.events.EntityLeaveWorldEvent.Companion.getPOST().invoke(event);
        } catch (Exception ignored) {}
    }
}
