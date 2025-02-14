package mageaddons.commands

import mageaddons.MageAddons.display
import mageaddons.MageAddons.mc
import mageaddons.config.Config
import mageaddons.features.dungeon.Dungeon
import mageaddons.features.dungeon.DungeonScan
import mageaddons.features.dungeon.ScanUtils
import mageaddons.utils.Utils
import gg.essential.universal.UChat
import mageaddons.utils.MessageUtils.modMessage
import net.minecraft.client.gui.GuiScreen
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos

class MageAddonsCommands : CommandBase() {

    private val commands = listOf("help", "scan", "roomdata")

    override fun getCommandName(): String = "mageaddons"

    override fun getCommandAliases(): List<String> = listOf("ma", "mageaddons")

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            display = Config.gui()
            return
        }
        when (args[0]) {
            // Help command
            "help" -> {
                UChat.chat(
                    """
                        #§b§l<§MageAddons Commands§b§l>
                        #  §b/mageaddons §9> §3Opens the main mod GUI. §7(Alias: fm, fmap)
                        #  §b/§fmageaddons §bscan §9> §3Rescans the map.
                        #  §b/§fmageaddons §broomdata §9> §3Copies current room data or room core to clipboard.
                    """.trimMargin("#")
                )
            }
            // Scans the dungeon
            "scan" -> {
                Dungeon.reset()
                DungeonScan.scan()
            }
            // Copies room data or room core to clipboard
            "roomdata" -> {
                val pos = ScanUtils.getRoomCentre(mc.thePlayer.posX.toInt(), mc.thePlayer.posZ.toInt())
                val data = ScanUtils.getRoomData(pos.first, pos.second)
                if (data != null) {
                    GuiScreen.setClipboardString(data.toString())
                    modMessage("§aCopied room data to clipboard.")
                } else {
                    GuiScreen.setClipboardString(ScanUtils.getCore(pos.first, pos.second).toString())
                    modMessage("§cExisting room data not found. §aCopied room core to clipboard.")
                }
            }
            "personbest" -> {

            }
            // Unknown command help message
            else -> modMessage("§cUnknown command. Use §b/§fmageaddons help §cfor a list of commands.")
        }
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<String>,
        pos: BlockPos,
    ): MutableList<String> {
        if (args.size == 1) {
            return getListOfStringsMatchingLastWord(args, commands)
        }
        return mutableListOf()
    }
}
