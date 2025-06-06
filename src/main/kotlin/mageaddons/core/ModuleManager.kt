package mageaddons.core

import BloodCamp
import mageaddons.features.combat.CustomWardrobe
import mageaddons.features.combat.RagAxe
import mageaddons.features.dungeon.SplitManager
import mageaddons.features.dungeon.WitherDragonManager
import mageaddons.features.testUse.TestOnly
import net.minecraft.network.Packet

object ModuleManager{
    val modules: ArrayList<ModuleFactory> = arrayListOf(
        //combat
        RagAxe, CustomWardrobe,

        //dungeon
        WitherDragonManager, SplitManager,BloodCamp,
        //QOL

        //TEST
        TestOnly
    )

    data class PacketFunction<T : Packet<*>>(
        val type: Class<T>,
        val function: (T) -> Unit,
        val shouldRun: () -> Boolean,
    )

    data class MessageFunction(val filter: Regex, val shouldRun: () -> Boolean, val function: (String) -> Unit)

    val packetFunctions = mutableListOf<PacketFunction<Packet<*>>>()
    val messageFunctions = mutableListOf<MessageFunction>()

    init {
        modules.stream().filter(ModuleFactory::checkEnable).forEach(ModuleFactory::onEnable)
    }

//    @SubscribeEvent(receiveCanceled = true)
//    fun onReceivePacket(event: PacketEvent.Receive) {
//        packetFunctions.forEach {
//            if (it.type.isInstance(event.packet) && it.shouldRun.invoke()) it.function(event.packet)
//        }
//    }
//
//    @SubscribeEvent(receiveCanceled = true)
//    fun onSendPacket(event: PacketEvent.Send) {
//        packetFunctions.forEach {
//            if (it.type.isInstance(event.packet) && it.shouldRun.invoke()) it.function(event.packet)
//        }
//    }
//
//    @SubscribeEvent(receiveCanceled = true)
//    fun onChatPacket(event: ChatPacketEvent) {
//        messageFunctions.forEach {
//            if (event.message matches it.filter && it.shouldRun()) it.function(event.message)
//        }
//    }

}