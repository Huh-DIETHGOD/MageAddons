package mageaddons.events

import net.minecraft.network.message.MessageType
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket
import net.minecraft.network.packet.s2c.play.TeamS2CPacket
import net.minecraft.text.Text

data class ChatEvent(val packet: ChatMessageS2CPacket) {
    private val content: Text
        get() = packet.unsignedContent() ?: Text.literal(packet.body().content())

    val text: String by lazy {
        content.string.replace(Regex("§[0-9a-fklmnor]"), "")
    }

    val formattedText: String by lazy {
        content.string
    }

    /** 0 = chat, 1 = other (game-info no longer travels through this packet in 1.20.2+) */
    val type: Byte
        get() = if (packet.serializedParameters().type().matchesKey(MessageType.CHAT)) 0 else 1
}

data class ChatPacketEvent(val message: String)
data class MessageSentEvent(val message: String)

data class TabListEvent(val packet: PlayerListS2CPacket)
data class ScoreboardEvent(val packet: TeamS2CPacket)
