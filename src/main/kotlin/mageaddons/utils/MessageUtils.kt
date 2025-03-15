package mageaddons.utils

import gg.essential.universal.UChat
import mageaddons.MageAddons.CHAT_PREFIX
import mageaddons.MageAddons.MOD_NAME
import mageaddons.MageAddons.mc
import mageaddons.config.Config
import net.minecraft.util.ChatComponentText

object MessageUtils {
    var isDevMod: Boolean = Config.developerMode

    /**
     * Play a title to player
     */
    fun playTitle(
        title: String,
        subtitle: String,
        fadeIn: Int,
        stay: Int,
        fadeOut: Int,
    ) {
        mc.ingameGUI.displayTitle(title, subtitle, fadeIn, stay, fadeOut)
    }

    /**
     * Sends a command to public chat
     */
    fun sendCommand(text: String) {
        sendChatMessage("/$text")
    }

    /**
     * Directly send a message to public chat
     */
    fun sendChatMessage(message: Any) {
        UChat.say(message.toString())
    }

    /**
     *  Sends a MOD message to the chat with the prefix
     */
    fun modMessage(message: String) {
        mc.ingameGUI.chatGUI.printChatMessage(
            ChatComponentText("$CHAT_PREFIX $message")
        )
    }

    /**
     * Sends a MOD message which can only be seen by the developer
     */
    fun devMessage(message: String) {
        if (isDevMod) {
            sendChatMessage("§a§l<§b${ MOD_NAME } §3§lDevMod§a>§r $message")
        }
    }

    /**
     *  Sends a PARTY message to the chat with the prefix
     */
    fun partyMessage(message: String) {
        sendCommand("pc MageAddons >> $message")
    }
}
