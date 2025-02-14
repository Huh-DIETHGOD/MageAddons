package mageaddons.utils

import mageaddons.MageAddons.logger
import mageaddons.MageAddons.mc
import net.minecraft.item.ItemStack
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
fun Event.postAndCatch(): Boolean =
    runCatching {
        MinecraftForge.EVENT_BUS.post(this)
    }.onFailure {
        it.printStackTrace()
        logger.error("An error occurred", it)
    }.getOrDefault(isCanceled)
