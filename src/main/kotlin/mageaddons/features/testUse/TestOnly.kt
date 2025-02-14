package mageaddons.features.testUse

import mageaddons.MageAddons
import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.MessageUtils.devMessage
import mageaddons.utils.MessageUtils.modMessage
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TestOnly: ModuleFactory(
    name = "test",
    toggle = Config.testCommandEnabled
) {
    val testtt: String
        get() = "§bgg"
    val abcd: String = "§basc"


    @SubscribeEvent
    fun onTest(event: ServerChatEvent){
        if (Config.testCommandEnabled and event.message.contains(MageAddons.mc.thePlayer.name)) {
            modMessage("test success")
            devMessage("§b§ltest success")
            val lines = mutableListOf(
                "§bScan Finished!").joinToString()
            modMessage(lines)
            modMessage(abcd)
            modMessage(testtt)

        }
    }
}