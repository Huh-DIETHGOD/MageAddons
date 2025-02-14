package mageaddons.core

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

class KeyBindManager(
    val name: String,
    var defaultHotKey: Keybinding,
) {
    constructor(name: String, key: Int) : this(name, Keybinding(key))

    var hotKey: Keybinding = defaultHotKey

    fun write(): JsonElement = JsonPrimitive(hotKey.key)

    fun read(element: JsonElement?) {
        element?.asInt?.let {
            hotKey.key = it
        }
    }

    /**
     * Action to do, when hotkey is pressed
     */
    fun onPress(block: () -> Unit): KeyBindManager {
        hotKey.onPress = block
        return this
    }
}

class Keybinding(
    var key: Int,
) {
    /**
     * Intended to active when keybind is pressed.
     */
    var onPress: (() -> Unit)? = null

    /**
     * @return `true` if [key] is held down.
     */
    fun isDown(): Boolean = if (key == 0) false else (if (key < 0) Mouse.isButtonDown(key + 100) else Keyboard.isKeyDown(key))
}
