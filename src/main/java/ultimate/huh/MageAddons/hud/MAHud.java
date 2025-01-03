package ultimate.huh.MageAddons.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import ultimate.huh.MageAddons.config.MAConfig;

/**
 * An example OneConfig HUD that is started in the config and displays text.
 *
 * @see MAConfig#hud
 */
public class MAHud extends SingleTextHud {
    public MAHud() {
        super("Test", true);
    }

    @Override
    public String getText(boolean example) {
        return "I'm an example HUD";
    }
}
