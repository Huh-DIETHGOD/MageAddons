package mageaddons.utils

import mageaddons.MageAddons
import mageaddons.MageAddons.logger
import mageaddons.MageAddons.mc
import mageaddons.utils.MessageUtils.modMessage
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import net.minecraft.util.StringUtils
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event

object Utils {
    fun Any?.equalsOneOf(vararg other: Any): Boolean = other.any { this == it }

    fun runMinecraftThread(run: () -> Unit) {
        if (!mc.isCallingFromMinecraftThread) {
            mc.addScheduledTask(run)
        } else {
            run()
        }
    }

    fun String.removeFormatting(): String = StringUtils.stripControlCodes(this)

    val ItemStack.itemID: String
        get() = this.getSubCompound("ExtraAttributes", false)?.getString("id") ?: ""
}

private val FORMATTING_CODE_PATTERN = Regex("§[0-9a-fk-or]", RegexOption.IGNORE_CASE)

val String?.noControlCodes: String
    get() = this?.let { FORMATTING_CODE_PATTERN.replace(it, "") } ?: ""

private val romanMap = mapOf('I' to 1, 'V' to 5, 'X' to 10, 'L' to 50, 'C' to 100, 'D' to 500, 'M' to 1000)
private val numberRegex = Regex("^[0-9]+$")
fun romanToInt(s: String): Int {
    return if (s.matches(numberRegex)) s.toInt()
    else {
        var result = 0
        for (i in 0 until s.length - 1) {
            val current = romanMap[s[i]] ?: 0
            val next = romanMap[s[i + 1]] ?: 0
            result += if (current < next) -current else current
        }
        result + (romanMap[s.last()] ?: 0)
    }
}

/**
 * Checks if the current object is equal to at least one of the specified objects.
 *
 * @param options List of other objects to check.
 * @return `true` if the object is equal to one of the specified objects.
 */
fun Any?.equalsOneOf(vararg options: Any?): Boolean = options.any { this == it }

fun String?.matchesOneOf(vararg options: Regex): Boolean = options.any { it.matches(this ?: "") }


/**
 * Posts an event to the event bus and catches any errors.
 * @author Skytils
 */
fun Event.postAndCatch(): Boolean {
    return runCatching {
        MinecraftForge.EVENT_BUS.post(this)
    }.onFailure {
        it.printStackTrace()
        logger.error("An error occurred", it)
        val style = ChatStyle()
        style.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("§6Click to copy the error to your clipboard."))
        modMessage("${MageAddons.MOD_VERSION} Caught an ${it::class.simpleName ?: "error"} at ${this::class.simpleName}. §cPlease click this message to copy and send it in the Odin discord!",)}.getOrDefault(isCanceled)
}
