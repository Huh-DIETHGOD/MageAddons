package ultimate.huh.kotlin.MageAddons.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType

object MageConfig : Config(Mod("Mage Addons", ModType.SKYBLOCK), "mageaddons-config.json") {
    @Text(
        name = "Dynamic Key Command",
        description = "'/ma help' for more info",
        category = "Random Beta Features",
        secure = false
    )
    var dynamicCommandString = ""
}
