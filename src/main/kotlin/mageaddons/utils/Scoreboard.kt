package mageaddons.utils

import mageaddons.utils.Utils.removeFormatting
import net.minecraft.client.MinecraftClient
import net.minecraft.scoreboard.ScoreboardDisplaySlot

object Scoreboard {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    fun getLines(): List<String> {
        val scoreboard = mc.player?.scoreboard ?: return emptyList()
        val objective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR) ?: return emptyList()

        return scoreboard.getScoreboardEntries(objective)
            .filter { !it.hidden() }
            .sortedByDescending { it.value() }
            .map { it.owner().removeFormatting() }
    }

    fun getCleanedLines(): List<String> =
        getLines().map { cleanLine(it) }

    fun cleanLine(line: String): String =
        line.replace(Regex("§[0-9a-fk-or]"), "").trim()

    fun getScoreboardTitle(): String? {
        val scoreboard = mc.player?.scoreboard ?: return null
        return scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR)?.displayName?.string?.removeFormatting()
    }
}
