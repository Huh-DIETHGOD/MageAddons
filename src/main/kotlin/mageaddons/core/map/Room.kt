package mageaddons.core.map

import mageaddons.config.Config
import mageaddons.core.RoomData
import mageaddons.features.dungeon.MapRender
import java.awt.Color

class Room(override val x: Int, override val z: Int, var data: RoomData) : Tile {
    var core = 0
    var isSeparator = false
    var uniqueRoom: UniqueRoom? = null
    override var state: RoomState = RoomState.UNDISCOVERED
    override val color: Color
        get() = if (state == RoomState.UNOPENED) Config.colorUnopened//MapRender.legitRender &&
        else when (data.type) {
            RoomType.BLOOD -> Config.colorBlood
            RoomType.CHAMPION -> Config.colorMiniboss
            RoomType.ENTRANCE -> Config.colorEntrance
            RoomType.FAIRY -> Config.colorFairy
            RoomType.PUZZLE -> Config.colorPuzzle
            RoomType.RARE -> Config.colorRare
            RoomType.TRAP -> Config.colorTrap
            else -> if (uniqueRoom?.hasMimic == true) Config.colorRoomMimic else Config.colorRoom
        }
}
