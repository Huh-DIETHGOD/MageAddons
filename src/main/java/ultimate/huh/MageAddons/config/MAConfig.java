package ultimate.huh.MageAddons.config;

import ultimate.huh.MageAddons.MageAddonsMod;
import ultimate.huh.MageAddons.hud.MAHud;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;
import org.lwjgl.input.Keyboard;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class MAConfig extends Config {
    @HUD(
            name = "Example HUD"
    )
    public MAHud hud = new MAHud();

    @Switch(
            name = "Example Switch",
            size = OptionSize.SINGLE // Optional
    )
    public static boolean exampleSwitch = false; // The default value for the boolean Switch.

    @Slider(
            name = "Example Slider",
            min = 0f, max = 100f, // Minimum and maximum values for the slider.
            step = 10 // The amount of steps that the slider should have.
    )
    public static float exampleSlider = 50f; // The default value for the float Slider.

    @Dropdown(
            name = "Example Dropdown", // Name of the Dropdown
            options = {"Option 1", "Option 2", "Option 3", "Option 4"} // Options available.
    )
    public static int exampleDropdown = 1; // Default option (in this case "Option 2")

    // Add a key binding option
    @Dropdown(
            name = "Key Binding",
            options = {"P", "O", "I", "U"} // Add more keys as needed
    )
    public static int keyBindingOption = 0; // Default to "P"

    public MAConfig() {
        super(new Mod(MageAddonsMod.NAME, ModType.UTIL_QOL), MageAddonsMod.MODID + ".json");
        initialize();
    }

    // Method to get the key code based on the dropdown selection
    public int getKeyCode() {
        switch (keyBindingOption) {
            case 0: return Keyboard.KEY_X;
            case 1: return Keyboard.KEY_O;
            case 2: return Keyboard.KEY_I;
            case 3: return Keyboard.KEY_U;
            default: return Keyboard.KEY_P;
        }
    }
}