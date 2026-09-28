package mageaddons.core.map

import mageaddons.features.dungeon.Dungeon
import mageaddons.features.dungeon.MapRenderList

class UniqueRoom(val startX: Int, val startZ: Int, val room: Room) {
    val name: String = room.data.name
    val type: RoomType = room.data.type
    val tiles = mutableSetOf<Pair<Int, Int>>()
    var hasMimic = false
    var separatorCount = 0
    private var state: RoomState = RoomState.UNDISCOVERED

    init {
        tiles.add(Pair(startX, startZ))
        room.uniqueRoom = this
        Dungeon.Info.uniqueRooms.add(this)
        Dungeon.Info.roomCount++
        MapRenderList.renderUpdated = true
    }

    fun addTile(x: Int, z: Int, tile: Room) {
        tiles.add(Pair(x, z))
        tile.uniqueRoom = this
        if (tile.isSeparator) separatorCount++
    }

    fun getState(): RoomState = state

    fun setState(newState: RoomState) {
        if (newState.ordinal > state.ordinal) {
            state = newState
        }
    }

    fun getCenterX(): Int = startX + tiles.maxOf { it.first }
    fun getCenterZ(): Int = startZ + tiles.maxOf { it.second }

    fun getSecretCount(): Int {
        return tiles.sumOf { (x, z) ->
            val tile = Dungeon.Info.dungeonList[z * 11 + x]
            if (tile is Room) tile.data.secrets else 0
        }
    }
}
