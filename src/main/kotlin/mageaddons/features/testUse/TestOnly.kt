package mageaddons.features.testUse

import mageaddons.MageAddons
import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.MessageUtils.devMessage
import mageaddons.utils.MessageUtils.modMessage
import mageaddons.utils.PlayerUtils.alert
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TestOnly: ModuleFactory(
    name = "test",
    toggle = Config.testCommandEnabled
) {
    @SubscribeEvent
    fun onTest(event: ServerChatEvent){
        if (Config.testCommandEnabled && event.message.contains(MageAddons.mc.thePlayer.name)) {
            modMessage("test success")
            devMessage("§b§ltest success")
            alert("Test")
        }
    }
}