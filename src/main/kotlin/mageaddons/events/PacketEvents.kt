package mageaddons.events

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

open class PacketEvent(val packet: Packet<*>) : Event() {

    @Cancelable
    class Receive(packet: Packet<*>) : PacketEvent(packet)

    @Cancelable
    class Send(packet: Packet<*>) : PacketEvent(packet)
}