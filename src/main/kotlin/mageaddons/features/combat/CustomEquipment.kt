package mageaddons.features.combat

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.MessageUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard

object CustomEquipment : ModuleFactory(
    name = "Custom Equipment",
    toggle = Config.equipmentHotKeyEnabled
) {
    private var key = Config.equipmentHotKey
    @SubscribeEvent
    fun onKeyPress(event: InputEvent.KeyInputEvent){
        if(!toggle) return
        if(event.equals(key)) MessageUtils.sendCommand("wardrobe")
    }
}