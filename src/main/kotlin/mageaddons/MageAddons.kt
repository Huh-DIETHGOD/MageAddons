@file:Suppress("ktlint:standard:no-wildcard-imports")

package mageaddons

import mageaddons.config.Config
import mageaddons.core.ModuleManager
import mageaddons.events.EventDispatcher
import mageaddons.features.dungeon.*
import mageaddons.features.QOL.Autofisher
import mageaddons.ui.GuiRenderer
import mageaddons.utils.*
import mageaddons.utils.Location
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object MageAddons : ClientModInitializer {
    val mc: MinecraftClient get() = MinecraftClient.getInstance()
    val logger: Logger = LogManager.getLogger("Mage Addons")

    const val MOD_ID = "mageaddons"
    const val MOD_NAME = "Mage Addons"
    const val MOD_VERSION = "0.0.3"
    const val CHAT_PREFIX = "§b§l<§fMage Addons§b§l>§r "

    private var worldLoaded = false

    override fun onInitializeClient() {
        logger.info("$MOD_NAME v$MOD_VERSION initializing for Fabric 1.21.1")

        // Initialize module system
        ModuleManager.init()

        // Register tick events
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick)

        // Register world connection events
        ClientPlayConnectionEvents.JOIN.register { _, _, _ ->
            worldLoaded = true
            Location.onConnect()
        }
        ClientPlayConnectionEvents.DISCONNECT.register { _, _ ->
            worldLoaded = false
            Location.onDisconnect()
            Dungeon.reset()
            WitherDragonManager.reset()
        }

        // Register HUD rendering
        HudRenderCallback.EVENT.register { context, tickCounter ->
            renderHud(context)
        }

        // Register world rendering (for 3D ESP features)
        WorldRenderEvents.AFTER_ENTITIES.register { context ->
            renderWorld(context)
        }

        // Register commands
        registerCommands()
    }

    private fun onClientTick(client: MinecraftClient) {
        if (client.world == null) return

        // Update location detection
        Location.onTick()

        // Update module ticks
        ModuleManager.onTick()

        // Core dungeon update
        if (Location.inDungeons) {
            Dungeon.onTick()
            BlessingDisplay.onTick()
            if (Config.dragonHelper) {
                WitherDragonManager.onTick()
            }
        }
    }

    private fun renderHud(context: DrawContext) {
        if (mc.world == null) return

        // Render dungeon map
        if (Location.inDungeons) {
            MapRender.render(context, 0, 0, 0f)
        }

        // Render blessing display
        if (Config.blessingDisplay || Config.forceBlessingDisplay) {
            BlessingDisplay.render(context)
        }

        // Render custom HUD elements
        GuiRenderer.render(context)
    }

    private fun renderWorld(context: net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext) {
        if (mc.world == null) return
        if (!Location.inDungeons) return

        // Render wither door ESP
        if (Config.witherDoorESP > 0) {
            WitherDoorESP.renderWorld()
        }

        // Render dragon boxes
        if (Config.dragonHelper && Config.dragonBox) {
            WitherDragonManager.renderWorld()
        }
    }

    private fun registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            val root =
                ClientCommandManager
                    .literal("ma")
                    .then(
                        ClientCommandManager
                            .literal("scan")
                            .executes {
                                if (Location.inDungeons && !DungeonScan.isScanning) {
                                    DungeonScan.hasScanned = false
                                    DungeonScan.scan()
                                    Utils.sendClientMessage("${CHAT_PREFIX}§aManual scan triggered!")
                                } else {
                                    Utils.sendClientMessage("${CHAT_PREFIX}§cCannot scan right now!")
                                }
                                1
                            },
                    ).then(
                        ClientCommandManager
                            .literal("help")
                            .executes {
                                Utils.sendClientMessage("$CHAT_PREFIX Commands: /ma scan, /ma help, /ma af, /ma config")
                                1
                            },
                    ).then(
                        ClientCommandManager
                            .literal("config")
                            .executes {
                                val screen = Config.createConfigScreen(mc.currentScreen)
                                mc.setScreen(screen)
                                1
                            },
                    ).then(
                        ClientCommandManager
                            .literal("af")
                            .executes {
                                Autofisher.toggle()
                                1
                            },
                    ).then(
                        ClientCommandManager
                            .literal("autofisher")
                            .executes {
                                Autofisher.toggle()
                                1
                            },
                    )
            dispatcher.register(root)

            val mageAddonsRoot =
                ClientCommandManager
                    .literal("mageaddons")
                    .redirect(root.build())
            dispatcher.register(mageAddonsRoot)
        }
    }
}
