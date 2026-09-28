package mageaddons.utils

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import mageaddons.MageAddons.mc
import net.minecraft.text.Text

object Utils {

    fun String.removeFormatting(): String =
        Regex("§[0-9a-fklmnor]").replace(this, "")

    fun String.noControlCodes(): String =
        replace(Regex("§[0-9a-fklmnor]"), "")

    fun <T> T.equalsOneOf(vararg values: T): Boolean =
        values.any { it == this }

    fun Int.equalsOneOf(vararg values: Int): Boolean =
        values.any { it == this }

    fun String.romanToInt(): Int {
        val map = mapOf(
            'I' to 1, 'V' to 5, 'X' to 10, 'L' to 50,
            'C' to 100, 'D' to 500, 'M' to 1000
        )
        var result = 0
        var prev = 0
        for (ch in this) {
            val cur = map[ch] ?: return -1
            result += if (cur > prev) cur - 2 * prev else cur
            prev = cur
        }
        return result
    }

    fun String.removeColorCodes(): String =
        replace(Regex("§[0-9a-fk-or]"), "")

    fun postAndCatch(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendClientMessage(message: String) {
        mc.player?.sendMessage(Text.literal(message), false)
    }

    fun sendClientActionBar(message: String) {
        mc.player?.sendMessage(Text.literal(message), true)
    }

    fun writeJson(value: Int): JsonElement = JsonPrimitive(value)
    fun writeJson(value: String): JsonElement = JsonPrimitive(value)
    fun writeJson(value: Boolean): JsonElement = JsonPrimitive(value)
}
