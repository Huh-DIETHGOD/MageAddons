package mageaddons.core

import mageaddons.MageAddons
import mageaddons.features.settings.AlwaysActive
import mageaddons.utils.MessageUtils.modMessage
import net.minecraft.network.Packet
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent
import org.lwjgl.input.Keyboard
import kotlin.reflect.full.hasAnnotation

// settings handler. for integrated registry
abstract class ModuleFactory(
    val name: String,
    functionKey: Int? = Keyboard.KEY_NONE,
    var toggle: Boolean = false,
) {
    // check if the function is enabled
    var isEnabled: Boolean = toggle
        private set

    @Transient
    val alwaysActive = this::class.hasAnnotation<AlwaysActive>()

    protected inline val mc get() = MageAddons.mc

    // check if the function includes hotkeys
    var keybinding: Keybinding? = functionKey?.let { Keybinding(it).apply { onPress = ::onKeybind } }

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun onEnable() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this)
    }

    fun checkEnable(): Boolean = isEnabled

    fun toggle() {
        isEnabled = !isEnabled
        if (isEnabled) {
            onEnable()
        } else {
            onDisable()
        }
    }

    open fun onKeybind() {
        modMessage("$name ${if (isEnabled) "§aenabled" else "§cdisabled"}.")
    }

    fun <T : Packet<*>> onPacket(
        type: Class<T>,
        shouldRun: () -> Boolean = { alwaysActive || isEnabled },
        func: (T) -> Unit,
    ) {
        @Suppress("UNCHECKED_CAST")
        ModuleManager.packetFunctions.add(
            ModuleManager.PacketFunction(type, func, shouldRun) as ModuleManager.PacketFunction<Packet<*>>,
        )
    }

}
