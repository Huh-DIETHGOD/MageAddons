package mageaddons.features.dungeon

import mageaddons.MageAddons
import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.events.TabListEvent
import mageaddons.utils.*
import mageaddons.utils.Location.inDungeons
import mageaddons.utils.PlayerUtils.mcText
import mageaddons.utils.impl.Blessing
import net.minecraft.client.renderer.GlStateManager
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

    fun renderBlessings(){
        MageAddons.mc.mcProfiler.endStartSection("text")
        if (toggle && inDungeons) {
            GlStateManager.pushMatrix()
            RenderUtils.renderCenteredText(listOf(blessings.toString()), 0, 0, color = 1)
        }

    }

    private fun handleHeaderFooterPacket(packet: S47PacketPlayerListHeaderFooter) {
        Blessing.entries.forEach { blessing ->
            blessing.regex.find(packet.footer.unformattedText.noControlCodes)?.let { match -> blessing.current = romanToInt(match.groupValues[1]) }
        }
    }
}