package mageaddons.utils

import mageaddons.MageAddons.mc
import net.minecraft.network.Packet

object ServerUtils {
    private val packets = ArrayList<Packet<*>>()

    @JvmStatic
    fun handleSendPacket(packet: Packet<*>): Boolean {
        return packets.remove(packet)
    }

    private fun sendPacketNoEvent(packet: Packet<*>) {
        packets.add(packet)
        mc.netHandler?.addToSendQueue(packet)
    }
}