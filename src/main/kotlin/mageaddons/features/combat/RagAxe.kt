package mageaddons.features.combat

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.*
import mageaddons.utils.MessageUtils.modMessage
import mageaddons.utils.MessageUtils.partyMessage
import mageaddons.utils.PlayerUtils.alert
import net.minecraft.network.play.server.S29PacketSoundEffect
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object RagAxe : ModuleFactory(
    name = "RagAxe Tracker",
    toggle = Config.ragAxe
) {
    private val shouldAnnounce = Config.ragAxeAnnouncer
    @SubscribeEvent
    fun onSoundReceived(event: PlaySoundEvent) {
        if (event.name != "mob.wolf.howl" || !isHolding("RAGNAROCK_AXE")) return
        if (toggle) alert("§bCasted Rag Axe", 20)
        val strengthGain = ((mc.thePlayer?.heldItem?.getSBStrength ?: return) * 1.5).toInt()
        if (toggle) modMessage("Gained strength: $strengthGain")
        if (shouldAnnounce) partyMessage("Gained strength from RagAxe: $strengthGain")
    }

    @SubscribeEvent
    fun onChatReceived(event: ServerChatEvent) {
        if(event.message.contains(Regex("Ragnarock was cancelled due to (?:being hit|taking damage)!"))) alert("§cRag Axe Cancelled",20)
    }
}
