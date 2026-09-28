package mageaddons.core

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class KeyBindManager(
    val name: String,
    var defaultKey: Int,
) {
    var keyBinding: KeyBinding = KeyBinding(
        "key.mageaddons.$name",
        InputUtil.Type.KEYSYM,
        defaultKey,
        "category.mageaddons"
    )

    init {
        KeyBindingHelper.registerKeyBinding(keyBinding)
    }

    fun isPressed(): Boolean = keyBinding.isPressed

    fun wasPressed(): Boolean = keyBinding.wasPressed()
}
