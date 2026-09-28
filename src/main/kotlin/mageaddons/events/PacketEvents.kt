package mageaddons.events

import net.minecraft.entity.Entity
import net.minecraft.network.packet.Packet

data class EntityLeaveWorldEvent(val entity: Entity) {
    companion object {
        // Called from mixin, nullable to allow registration
        var POST: ((EntityLeaveWorldEvent) -> Unit) = {}
    }
}

open class PacketEvent(val packet: Packet<*>) {
    class Receive(packet: Packet<*>) : PacketEvent(packet)
    class Send(packet: Packet<*>) : PacketEvent(packet)
}

data class PostEntityMetadata(val packet: net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket)
