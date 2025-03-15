package mageaddons.core.map

import mageaddons.config.Config
import java.awt.Color

class Door(override val x: Int, override val z: Int, var type: DoorType) : Tile {
    var opened = false
    override var state: RoomState = RoomState.UNDISCOVERED
    override val color: Color
        get() =
        when (type) {
            DoorType.BLOOD -> Config.colorBloodDoor
            DoorType.ENTRANCE -> Config.colorEntranceDoor
            DoorType.WITHER -> if (opened) Config.colorOpenWitherDoor else Config.colorWitherDoor
            else -> Config.colorRoomDoor
        }
}
