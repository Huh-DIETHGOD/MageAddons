package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.DungeonUtil.getM7Phase
import mageaddons.utils.Location.inDungeons
import mageaddons.utils.impl.M7Phases
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object SplitManager: ModuleFactory(
    name = "SplitPower",
    toggle = Config.splitManager
) {
    var easyPower: Int? = Config.easySplitPower
    private var currentPower: Int? = 0
    @SubscribeEvent
    fun onServer(event: ServerChatEvent){
        if(!inDungeons || getM7Phase() != M7Phases.P5) return

    }



}