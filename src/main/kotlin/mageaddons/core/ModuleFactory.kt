package mageaddons.core

import mageaddons.events.ChatPacketEvent
import mageaddons.events.MessageSentEvent
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.network.packet.Packet

/**
 * Base class for toggleable feature modules.
 * In the Fabric port, we use a simplified module system without the Essential framework.
 */
abstract class ModuleFactory(val name: String, defaultEnabled: Boolean = false) {
    var enabled: Boolean = defaultEnabled

    /** Packet listeners - called when chat packets arrive */
    val packetListeners: MutableList<(Packet<*>) -> Unit> = mutableListOf()

    /** Called when the module sends a chat message */
    val messageListeners: MutableList<(MessageSentEvent) -> Unit> = mutableListOf()

    /** Called when a game message (non-action-bar) arrives */
    val gameMessageListeners: MutableList<(String) -> Unit> = mutableListOf()

    /** Key bindings for this module */
    val keyBindings: MutableList<KeyBinding> = mutableListOf()

    open fun onEnable() {}
    open fun onDisable() {}
    open fun onTick() {}

    fun registerKeyBinding(id: String, defaultKey: Int, category: String = "Mage Addons"): KeyBinding {
        val key = KeyBinding(
            "key.$id",
            InputUtil.Type.KEYSYM,
            defaultKey,
            "category.$category"
        )
        KeyBindingHelper.registerKeyBinding(key)
        keyBindings.add(key)
        return key
    }

    fun toggle() {
        enabled = !enabled
        if (enabled) onEnable() else onDisable()
    }
}
