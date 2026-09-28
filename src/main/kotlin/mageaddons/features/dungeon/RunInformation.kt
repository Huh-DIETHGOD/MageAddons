package mageaddons.features.dungeon

import mageaddons.features.dungeon.Dungeon.Info

object RunInformation {
    private var completedPuzzles = 0
    private var totalPuzzles = 0
    var deaths = 0
    var mimicKilled = false

    fun updatePuzzleCount(tabList: List<Pair<String, String>>) {
        // Parse puzzle completion status from tab list
        tabList.forEach { (_, display) ->
            val clean = display.replace(Regex("§[0-9a-fk-or]"), "")
            if (clean.contains("Failed") || clean.contains("✗")) {
                // Puzzle failed
                totalPuzzles++
            } else if (clean.contains("Completed") || clean.contains("✓")) {
                completedPuzzles++
                totalPuzzles++
            }
        }
    }

    fun getPuzzleStatus(): String {
        return "$completedPuzzles/$totalPuzzles"
    }

    fun getMissingPuzzles(): Int = totalPuzzles - completedPuzzles

    fun reset() {
        completedPuzzles = 0
        totalPuzzles = 0
        deaths = 0
        mimicKilled = false
    }
}
