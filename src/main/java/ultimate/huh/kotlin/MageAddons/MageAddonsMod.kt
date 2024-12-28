package ultimate.huh.kotlin.MageAddons

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File

class MageAddonsMod {

    @Mod(
        modid = MageAddonsMod.MOD_ID,
        name = MageAddonsMod.MOD_NAME,
        version = MageAddonsMod.MOD_VERSION,
        clientSideOnly = true
    )
    class MageAddonsMod{
        @Mod.EventHandler
        fun preInit(event: FMLPreInitializationEvent) {
            val directory = File(event.modConfigurationDirectory, "mageaddonsmod")
            directory.mkdirs()
        }
        @Mod.EventHandler
        fun onInit(event: FMLInitializationEvent) {
        }



        companion object {
            const val MOD_ID = "mageaddons"
            const val MOD_NAME = "Mage Addons"
            const val MOD_VERSION = "0.0.1"
            const val CHAT_PREFIX = "§f<§3MageAddons§f>§r"

            val mc: Minecraft = Minecraft.getMinecraft()
            var display: GuiScreen? = null


        }

    }
}