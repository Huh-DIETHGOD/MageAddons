package mageaddons.core

import mageaddons.core.map.Puzzle
import mageaddons.core.map.RoomType

data class RoomData(
    val name: String,
    val type: RoomType,
    val core: Int = 0,
    val secrets: Int = 0,
    val puzzle: Puzzle? = null,
    val crypts: List<Pair<Int, Int>> = emptyList(),
) {
    companion object {
        val UNKNOWN = RoomData("Unknown", RoomType.NORMAL)
    }
}
