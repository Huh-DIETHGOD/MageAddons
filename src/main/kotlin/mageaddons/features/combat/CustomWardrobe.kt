package mageaddons.features.combat

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.features.QOL.HotKeys.transformKey
import mageaddons.utils.MessageUtils
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

object CustomWardrobe : ModuleFactory(
    name = "Custom Wardrobe",
    toggle = Config.customWardrobe
) {
    @SubscribeEvent
    fun onKeyPress(event: InputEvent.KeyInputEvent){
        if(!Config.customWardrobe) return
        if(event.equals("x")) MessageUtils.sendCommand("wardrobe")
    }
}