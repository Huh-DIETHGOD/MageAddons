package mageaddons.mixins;

import mageaddons.events.ChatEvent;
import mageaddons.events.PostEntityMetadata;
import mageaddons.events.TabListEvent;
import mageaddons.events.EventDispatcher;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {

    // Hook chat messages
    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(ChatMessageS2CPacket packet, CallbackInfo ci) {
        try {
            EventDispatcher.INSTANCE.onChatPacket(packet);
        } catch (Exception ignored) {}
    }

    // Hook game messages (/title, /say, etc.)
    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    private void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        try {
            EventDispatcher.INSTANCE.onGameMessage(packet);
        } catch (Exception ignored) {}
    }

    // Hook entity metadata updates
    @Inject(method = "onEntityTrackerUpdate", at = @At("HEAD"))
    private void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {
        try {
            PostEntityMetadata event = new PostEntityMetadata(packet);
            // Fire event - can be consumed by features
        } catch (Exception ignored) {}
    }

    // Hook tab list updates
    @Inject(method = "onPlayerList", at = @At("HEAD"))
    private void onPlayerList(PlayerListS2CPacket packet, CallbackInfo ci) {
        try {
            TabListEvent event = new TabListEvent(packet);
            // Fire event
        } catch (Exception ignored) {}
    }

    // Hook team updates (scoreboard)
    @Inject(method = "onTeam", at = @At("HEAD"))
    private void onTeam(TeamS2CPacket packet, CallbackInfo ci) {
        try {
            mageaddons.events.ScoreboardEvent event = new mageaddons.events.ScoreboardEvent(packet);
            // Fire event
        } catch (Exception ignored) {}
    }

    // Hook sound packets (used by the Autofisher for bobber splash bite detection)
    @Inject(method = "onPlaySound", at = @At("HEAD"))
    private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo ci) {
        try {
            mageaddons.features.QOL.Autofisher.INSTANCE.onSoundPacket(packet);
        } catch (Exception ignored) {}
    }
}
