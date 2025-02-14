package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object SplitPower: ModuleFactory(
    name = "SplitPower",
    toggle = true
) {
    @SubscribeEvent
    fun onServer(event: ServerChatEvent){


    }

    var easyPower: Int? = Config.easySplitPower
    var currentPower: Int? = 0

}