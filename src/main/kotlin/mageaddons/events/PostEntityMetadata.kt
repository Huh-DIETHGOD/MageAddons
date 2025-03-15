package mageaddons.events

import net.minecraft.network.play.server.S1CPacketEntityMetadata
import net.minecraftforge.fml.common.eventhandler.Event

data class PostEntityMetadata(val packet: S1CPacketEntityMetadata) : Event()