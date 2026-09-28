package mageaddons.core.map

import mageaddons.utils.Color

interface Tile {
    val x: Int
    val z: Int
    var state: RoomState
    val color: Color
}
