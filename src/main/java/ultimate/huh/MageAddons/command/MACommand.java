package ultimate.huh.MageAddons.command;

import ultimate.huh.MageAddons.MageAddonsMod;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;

/**
 * An example command implementing the Command api of OneConfig.
 * Registered in ExampleMod.java with `CommandManager.INSTANCE.registerCommand(new ExampleCommand());`
 *
 * @see Command
 * @see Main
 * @see MageAddonsMod
 */
@Command(value = MageAddonsMod.MODID, description = "Access the " + MageAddonsMod.NAME + " GUI.")
public class MACommand {
    @Main
    private void handle() {
        MageAddonsMod.INSTANCE.config.openGui();
    }
}