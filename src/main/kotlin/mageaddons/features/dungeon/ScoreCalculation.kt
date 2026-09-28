package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.features.dungeon.Dungeon.Info
import mageaddons.utils.Location

object ScoreCalculation {
    var paul = false
    private var lastScore = 0
    private var reached300 = false
    private var reached270 = false
    private var score300Time = 0L

    fun updateScore() {
        if (!Location.inDungeons) return

        // Calculate score based on secrets, crypts, puzzles, mimic, and time
        val secrets = Info.secretCount
        val crypts = Info.cryptCount
        val completedPuzzles = Info.puzzles.count { it.value }

        // Skill score (simplified)
        var skillScore = secrets.coerceAtMost(getSecretTarget())

        // Explore score
        var exploreScore = completedPuzzles * 10 + crypts * 2

        // Bonus scores
        if (Info.mimicFound) exploreScore += 2
        if (paul) exploreScore += 10

        val totalScore = (skillScore + exploreScore).coerceAtMost(300)

        if (totalScore > lastScore) {
            lastScore = totalScore

            // Check score thresholds
            if (totalScore >= 300 && !reached300) {
                reached300 = true
                score300Time = System.currentTimeMillis() - Info.startTime
                handleScoreThreshold(300)
            }
            if (totalScore >= 270 && !reached270) {
                reached270 = true
                handleScoreThreshold(270)
            }
        }
    }

    private fun getSecretTarget(): Int {
        // Returns the secret target for S+ based on floor
        return when (Location.dungeonFloor) {
            1 -> 30
            2 -> 40
            3 -> 50
            4 -> 60
            5 -> 70
            6 -> 80
            7 -> 90
            else -> 80
        }
    }

    fun getSecretPercent(): Double {
        return when (Location.dungeonFloor) {
            1 -> 0.30
            2 -> 0.40
            3 -> 0.50
            4 -> 0.60
            5 -> 0.70
            6 -> 0.85
            7 -> 0.85
            else -> 0.80
        }
    }

    fun getScoreDisplay(): String {
        return "§bS+ §f(last: $lastScore/300)"
    }

    private fun handleScoreThreshold(score: Int) {
        if (Config.scoreMessage == 0) return
        if (Config.scoreMessage == 1 && score == 270) return

        val message = when (score) {
            300 -> Config.message300
            270 -> Config.message270
            else -> return
        }

        // Send party message
        mageaddons.utils.Utils.sendClientMessage("/pc $message")

        // Show title if enabled
        if (Config.scoreTitle > 0) {
            if (Config.scoreTitle == 1 && score == 270) return
            val title = if (Config.timeTo300 && score == 300 && score300Time > 0) {
                "§6$message §7(${score300Time / 1000}s)"
            } else {
                "§6$message"
            }
            mageaddons.utils.Utils.sendClientMessage("/title @p title {\"text\":\"$title\"}")
        }
    }
}
