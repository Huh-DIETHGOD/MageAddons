package mageaddons.utils.impl

import net.minecraft.network.Packet

data class PacketFunction<T : Packet<*>>(
    val type: Class<T>,
    val function: (T) -> Unit,
    val shouldRun: () -> Boolean,
)

data class MessageFunction(
    val filter: Regex,
    val shouldRun: () -> Boolean,
    val function: (String) -> Unit
)

data class TickTask(
    var ticksLeft: Int,
    val server: Boolean,
    val function: () -> Unit
)