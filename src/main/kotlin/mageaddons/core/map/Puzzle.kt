package mageaddons.core.map

import mageaddons.utils.Utils.equalsOneOf

enum class Puzzle(val roomDataName: String, val totalPuzzles: Int = 1) {
    // Basic puzzles
    BLAZE("Blaze"),

    // Room 1x1 puzzles
    THREE_WEIRDOS("Three Weirdos"),
    CREEPER_BEAMS("Creeper Beams"),
    HIGHER_OR_LOWER("Higher or Lower"),
    QUIZ("Quiz"),

    // Large puzzles
    TIC_TAC_TOE("Tic Tac Toe"),
    WATER_BOARD("Water Board"),
    BOULDER("Boulder"),
    ICE_PATH("Ice Path"),
    SILVERFISH("Silverfish"),
    ICE_FILL("Ice Fill"),
    TELEPORT_MAZE("Teleport Maze"),
    BOMB_DEFUSE("Bomb Defuse");

    companion object {
        fun fromRoomName(name: String): Puzzle? =
            entries.find { it.roomDataName.equals(name, ignoreCase = true) }

        /**
         * Returns the total number of puzzles from the room name
         */
        fun getTotalPuzzles(name: String): Int =
            fromRoomName(name)?.totalPuzzles ?: 1

        /**
         * Determines if this puzzle room has a success/fail state tracked by the tab list
         */
        fun isTrackedPuzzle(name: String): Boolean {
            val puzzle = fromRoomName(name) ?: return false
            return puzzle.equalsOneOf(
                BLAZE, THREE_WEIRDOS, CREEPER_BEAMS, HIGHER_OR_LOWER,
                TIC_TAC_TOE, WATER_BOARD, BOULDER, ICE_PATH, ICE_FILL,
                TELEPORT_MAZE, BOMB_DEFUSE
            )
        }
    }
}
