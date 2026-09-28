package mageaddons.core

import mageaddons.utils.impl.DungeonClass
import net.minecraft.util.Identifier

class DungeonPlayer(
    val name: String,
) {
    var dungeonClass: DungeonClass = DungeonClass.EMPTY
    var classLevel = 0
    var posX = 0.0
    var posZ = 0.0
    var yaw = 0f
    var skinTexture: Identifier? = null
    var isDead = false

    val displayName: String
        get() =
            when (dungeonClass) {
                DungeonClass.EMPTY -> name
                else -> "${dungeonClass.displayName} $classLevel"
            }
}
