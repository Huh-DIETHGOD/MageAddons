package mageaddons.features.combat

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.MessageUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

object CustomWardrobe : ModuleFactory(
    name = "Custom Wardrobe",
    toggle = Config.wardrobeHotKeyEnabled
) {
    private var key = Config.wardrobeHotKey
    @SubscribeEvent
    fun onKeyPress(event: InputEvent.KeyInputEvent){
        if(!toggle) return
        if(event.equals(key)) MessageUtils.sendCommand("wardrobe")
    }
}