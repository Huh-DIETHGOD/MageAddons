package mageaddons

import gg.essential.api.EssentialAPI
import gg.essential.universal.UChat
import gg.essential.vigilance.gui.SettingsGui
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mageaddons.commands.MageAddonsCommands
import mageaddons.config.Config
import mageaddons.config.PersonalBestConfig
import mageaddons.core.ModuleManager
import mageaddons.events.EventDispatcher
import mageaddons.features.QOL.HotKeys
import mageaddons.features.QOL.HotKeys.transformKey
import mageaddons.features.dungeon.*
import mageaddons.ui.GuiRenderer
import mageaddons.utils.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Desktop
import java.io.File
import java.net.URI
import kotlin.coroutines.EmptyCoroutineContext

@Mod(
    modid = MageAddons.MOD_ID,
    name = MageAddons.MOD_NAME,
    version = MageAddons.MOD_VERSION,
    modLanguageAdapter = "mageaddons.utils.KotlinAdapter",
)
object MageAddons {
    const val MOD_ID = "mageaddons"
    const val MOD_NAME = "Mage Addons"
    const val MOD_VERSION = "0.0.1"
    val CHAT_PREFIX: String
        get() = "§b§l<§f${ MOD_NAME }§b§l>§r"

    val mc: Minecraft = Minecraft.getMinecraft()
    var display: GuiScreen? = null
    val scope = CoroutineScope(EmptyCoroutineContext)
    val logger: Logger = LogManager.getLogger("MageAddons")

    var equipmentKey = KeyBinding("/eq", transformKey(Config.equipmentHotKey), "Mage Addons")
    var wardrobeKey = KeyBinding("/wd", transformKey(Config.wardrobeHotKey), "Mage Addons")

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        File(event.modConfigurationDirectory, "mageaddons").mkdirs()
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        PersonalBestConfig.loadConfig()
        ClientCommandHandler.instance.registerCommand(MageAddonsCommands())
        listOf(
            this,
            Dungeon,
            GuiRenderer,
            Location,
            RunInformation,
            HotKeys,
            PlayerUtils,
            Utils,
            ModuleManager,
            EventDispatcher,
            WitherDoorESP
        ).forEach(MinecraftForge.EVENT_BUS::register)
        RenderUtils
        listOf(
            wardrobeKey,
            equipmentKey,
        ).forEach(ClientRegistry::registerKeyBinding)
    }

    @Mod.EventHandler
    fun postInit(event: FMLLoadCompleteEvent) = scope.launch {
        if (UpdateChecker.hasUpdate() > 0) {
            EssentialAPI.getNotifications()
                .push(MOD_NAME, "New release available on Github. Click to open download link.", 10f, action = {
                    try {
                        Desktop.getDesktop().browse(URI(""))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
        }
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return

        mc.mcProfiler.startSection("mageaddons")

        if (display != null) {
            mc.displayGuiScreen(display)
            display = null
        }

        Dungeon.onTick()
        GuiRenderer.onTick()
        Location.onTick()

        mc.mcProfiler.endSection()
    }

    @SubscribeEvent
    fun onKey(event: InputEvent.KeyInputEvent) {
        while (equipmentKey.isPressed) {
            if (Config.equipmentHotKeyEnabled) UChat.say("/equipment")
        }
        while (wardrobeKey.isPressed && Config.wardrobeHotKeyEnabled) {
            if (Config.wardrobeHotKeyEnabled) UChat.say("/wardrobe")
        }
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        if (event.gui == null && mc.currentScreen is SettingsGui) {
            MapRenderList.renderUpdated = true
        }
    }
}
