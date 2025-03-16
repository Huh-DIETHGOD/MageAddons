package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.events.TabListEvent
import mageaddons.utils.Color
import mageaddons.utils.PlayerUtils.mcText
import mageaddons.utils.TabList
import mageaddons.utils.impl.Blessing
import mageaddons.utils.noControlCodes
import mageaddons.utils.romanToInt
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object BlessingDisplay: ModuleFactory(
    name = "Blessing Display",
    toggle = Config.blessingDisplay
) {
    var blessingDis: List<Int> = listOf()
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
    @SubscribeEvent
    fun onHeaderPacket(event: TabListEvent) {
        if (event.packet is S47PacketPlayerListHeaderFooter) {
            handleHeaderFooterPacket(event.packet)
        }
    }

    private fun handleHeaderFooterPacket(packet: S47PacketPlayerListHeaderFooter) {
        Blessing.entries.forEach { blessing ->
            blessing.regex.find(packet.footer.unformattedText.noControlCodes)?.let { match -> blessing.current = romanToInt(match.groupValues[1]) }
        }
    }
}