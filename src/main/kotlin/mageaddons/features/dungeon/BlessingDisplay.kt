package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.utils.Location
import mageaddons.utils.impl.Blessing
import mageaddons.utils.Color
import mageaddons.utils.RenderUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object BlessingDisplay {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()
    private val blessings = mutableMapOf<Blessing, Int>()

    fun onTick() {
        if (!Config.blessingDisplay && !Config.forceBlessingDisplay) return
        if (!Location.inDungeons) return

        // Parse blessings from tab footer text
        parseBlessings()
    }

    fun render(context: DrawContext) {
        if (!Config.blessingDisplay && !Config.forceBlessingDisplay) return
        if (!Location.inDungeons) return

        var yOffset = Config.blessingY
        val x = Config.blessingX

        blessings.forEach { (blessing, level) ->
            val shouldShow = when (blessing) {
                Blessing.POWER -> Config.displayPower
                Blessing.TIME -> Config.displayTime
                Blessing.STONE -> Config.displayStone
                Blessing.LIFE -> Config.displayLife
                Blessing.WISDOM -> Config.displayWisdom
            }

            if (shouldShow) {
                val text = "${blessing.symbol} ${blessing.displayName}: $level"
                RenderUtils.drawText(context, text, x, yOffset, Color.WHITE)
                yOffset += 12
            }
        }
    }

    private fun parseBlessings() {
        // Parse blessing levels from scoreboard or tab list
        // The footer typically shows: "Power V   Time III   Stone II   Life IV   Wisdom I"
        val scoreboardLines = mageaddons.utils.Scoreboard.getLines()

        scoreboardLines.forEach { line ->
            val cleanLine = mageaddons.utils.Scoreboard.cleanLine(line)

            Blessing.entries.forEach { blessing ->
                val regex = Regex("${blessing.displayName}\\s+(\\w+)")
                regex.find(cleanLine)?.let { match ->
                    val levelStr = match.groupValues[1]
                    val level = when {
                        levelStr.all { it == 'I' } -> levelStr.length
                        levelStr.toIntOrNull() != null -> levelStr.toInt()
                        else -> 0
                    }
                    if (level > 0) blessings[blessing] = level
                }
            }
        }
    }
}
