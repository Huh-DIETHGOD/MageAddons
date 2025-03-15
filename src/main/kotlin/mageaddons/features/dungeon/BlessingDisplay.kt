package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.Color
import mageaddons.utils.PlayerUtils.mcText
import mageaddons.utils.impl.Blessing

object BlessingDisplay: ModuleFactory(
    name = "Blessing Display",
    toggle = Config.blessingDisplay
) {
    private val power = Config.displayPower
    private val time = Config.displayTime
    private val stone = Config.displayStone
    private val life = Config.displayLife
    private val wisdom = Config.displayWisdom
    private data class BlessingData(val type: Blessing, val enabled: () -> Boolean, val color: () -> Color)
    private val blessings = listOf(
        BlessingData(Blessing.POWER, { power }, { Color.DARK_RED }),
        BlessingData(Blessing.TIME, { time }, { Color.PURPLE }),
        BlessingData(Blessing.STONE, { stone }, { Color.GREEN }),
        BlessingData(Blessing.LIFE, { life }, { Color.RED }),
        BlessingData(Blessing.WISDOM, { wisdom }, { Color.BLUE })
    )

}