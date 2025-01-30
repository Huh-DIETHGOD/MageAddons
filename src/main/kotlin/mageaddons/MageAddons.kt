package mageaddons

import mageaddons.commands.MageAddonsCommands
import mageaddons.config.Config
import mageaddons.features.dungeon.*
import mageaddons.ui.GuiRenderer
import mageaddons.utils.Location
import mageaddons.utils.RenderUtils
import mageaddons.utils.UpdateChecker
import gg.essential.api.EssentialAPI
import gg.essential.vigilance.gui.SettingsGui
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
import org.lwjgl.input.Keyboard
import java.awt.Desktop
import java.io.File
import java.net.URI
import kotlin.coroutines.EmptyCoroutineContext

@Mod(
    modid = MageAddons.MOD_ID,
    name = MageAddons.MOD_NAME,
    version = MageAddons.MOD_VERSION,
    modLanguageAdapter = "mageaddons.utils.KotlinAdapter"
)
object MageAddons {
    const val MOD_ID = "mageaddons"
    const val MOD_NAME = "Mage Addons"
    const val MOD_VERSION = "0.0.1"
    val CHAT_PREFIX: String
        get() = "§b§l<§f${Config.customPrefix.ifBlank { MOD_NAME }}§b§l>§r"

    val mc: Minecraft = Minecraft.getMinecraft()
    var display: GuiScreen? = null
    private val toggleLegitKey = KeyBinding("Legit Peek", Keyboard.KEY_NONE, "Mage Addons")
    val scope = CoroutineScope(EmptyCoroutineContext)
    val logger: Logger = LogManager.getLogger("MageAddons")

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        File(event.modConfigurationDirectory, "mageaddons").mkdirs()
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand((MageAddonsCommands()))
        listOf(
            this, Dungeon, GuiRenderer, Location, RunInformation, WitherDoorESP
        ).forEach(MinecraftForge.EVENT_BUS::register)
        RenderUtils
        ClientRegistry.registerKeyBinding(toggleLegitKey)
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
//      if (Config.peekMode == 0 && toggleLegitKey.isPressed) {
//      MapRender.legitPeek = !MapRender.legitPeek
//      }
    }

    @SubscribeEvent
    fun onGuiClose(event: GuiOpenEvent) {
        if (event.gui == null && mc.currentScreen is SettingsGui) {
            MapRenderList.renderUpdated = true
        }
    }
}
