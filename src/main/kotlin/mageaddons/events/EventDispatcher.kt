package mageaddons.events

import mageaddons.MageAddons.mc
import mageaddons.core.ModuleManager
import mageaddons.utils.Location.inDungeons
import mageaddons.utils.Utils.noControlCodes
import mageaddons.utils.Utils.postAndCatch
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket

object EventDispatcher {

    /**
     * Handles incoming chat packets and dispatches to appropriate listeners
     */
    fun onChatPacket(packet: ChatMessageS2CPacket) {
        if (!inDungeons) return
        try {
            val event = ChatEvent(packet)
            // Dispatch to dungeon chat listener
            mageaddons.features.dungeon.Dungeon.onChatPacket(event)
            // Dispatch to location
            mageaddons.utils.Location.onChat(event)
            // Dispatch to module listeners
            ModuleManager.modules.forEach { module ->
                module.packetListeners.forEach { listener ->
                    postAndCatch { listener.invoke(packet) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Handles outgoing chat messages (String overload for mixin compatibility)
     */
    fun onMessageSent(message: String) {
        try {
            val cleaned = message.noControlCodes()
            if (cleaned.startsWith("/")) return

            val event = MessageSentEvent(cleaned)
            ModuleManager.modules.forEach { module ->
                module.messageListeners.forEach { listener ->
                    postAndCatch { listener.invoke(event) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Handles outgoing chat messages (packet overload)
     */
    fun onMessageSent(packet: ChatMessageC2SPacket) {
        onMessageSent(packet.chatMessage())
    }

    /**
     * Handles game info messages
     */
    fun onGameMessage(packet: GameMessageS2CPacket) {
        try {
            if (packet.overlay()) return // Skip action bar
            val message = packet.content().string.noControlCodes()
            ModuleManager.modules.forEach { module ->
                module.gameMessageListeners.forEach { listener ->
                    postAndCatch { listener.invoke(message) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
