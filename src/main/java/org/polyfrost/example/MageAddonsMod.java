package org.polyfrost.example;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.polyfrost.example.command.MACommand;
import org.polyfrost.example.config.MAConfig;
import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import net.minecraftforge.fml.common.Mod;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = MageAddonsMod.MODID, name = MageAddonsMod.NAME, version = MageAddonsMod.VERSION)
public class MageAddonsMod {
    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance(MODID)
    public static MageAddonsMod INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static MAConfig config;

    static KeyBinding tarKey1;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new MAConfig();
        CommandManager.INSTANCE.registerCommand(new MACommand());

        int keyCode = config.getKeyCode();
        tarKey1 = new KeyBinding("/eq command", Keyboard.KEY_X, "null");
        ClientRegistry.registerKeyBinding(tarKey1);
    }

    // Listen for key input events
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (tarKey1.isPressed()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("sent1");
        }
    }
}
