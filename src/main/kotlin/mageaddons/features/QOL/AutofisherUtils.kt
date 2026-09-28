package mageaddons.features.QOL

import mageaddons.MageAddons.mc
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.projectile.FishingBobberEntity
import net.minecraft.item.FishingRodItem
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.sound.SoundCategory

object AutofisherUtils {

    /** @return the player's fishing bobber, or null if no rod is cast. */
    fun getBobber(): FishingBobberEntity? = mc.player?.fishHook

    /** Checks the main hand for a fishing rod (also matches custom NBT-based rods). */
    fun isHoldingRod(): Boolean {
        val player = mc.player ?: return false
        val stack = player.mainHandStack
        return stack.item is FishingRodItem || stack.name.string.contains("Rod", ignoreCase = true)
    }

    /**
     * Simulates a right-click (use key) press. The vanilla input handler picks the
     * press up on the next client tick and performs item use, which casts or
     * reels in the fishing rod.
     */
    fun simulateUseClick() {
        KeyBinding.onKeyPressed(mc.options.useKey.boundKey)
    }

    /** Simulates an arbitrary key press (keyboard key or mouse button). */
    fun simulateKeyPress(key: InputUtil.Key) {
        KeyBinding.onKeyPressed(key)
    }

    /** @return true if the sound packet is a fishing bobber splash sound. */
    fun isBobberSplash(packet: PlaySoundS2CPacket): Boolean {
        if (packet.category != SoundCategory.WEATHER) return false
        val path = packet.sound.value().id.path
        return path.endsWith("fishing_bobber.splash")
    }
}
