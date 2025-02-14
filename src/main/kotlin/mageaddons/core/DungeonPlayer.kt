package mageaddons.core

import mageaddons.MageAddons.scope
import mageaddons.core.map.Room
import mageaddons.features.dungeon.Dungeon
import mageaddons.utils.APIUtils
import mageaddons.utils.Location
import mageaddons.utils.MapUtils
import mageaddons.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mageaddons.utils.impl.DungeonClass
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.util.ResourceLocation

data class DungeonPlayer(
    var skin: ResourceLocation?,
    var Tname: String?,
    var clazz: DungeonClass?,
    val clazzlvl: Int?,
    var entity: EntityPlayer?
) {
    var name = ""

    /** Minecraft formatting code for the player's name */
    var colorPrefix = 'f'

    /** The player's name with formatting code */
    val formattedName: String
        get() = "§$colorPrefix$name"

    var mapX = 0
    var mapZ = 0
    var yaw = 0f

    /** Has information from player entity been loaded */
    var playerLoaded = false
    var icon = ""
    var renderHat = false
    var dead = false
    var uuid = ""
    var isPlayer = false

    /** Stats for compiling player tracker information */
    var startingSecrets = 0
    var lastRoom = ""
    var lastTime = 0L
    var roomVisits: MutableList<Pair<Long, String>> = mutableListOf()

    /** Set player data that requires entity to be loaded */
    fun setData(player: EntityPlayer) {
        renderHat = player.isWearing(EnumPlayerModelParts.HAT)
        uuid = player.uniqueID.toString()
        playerLoaded = true
        scope.launch(Dispatchers.IO) {
            val secrets = APIUtils.getSecrets(uuid)
            Utils.runMinecraftThread {
                startingSecrets = secrets
            }
        }
    }

    /** Gets the player's room, used for room tracker */
    fun getCurrentRoom(): String {
        if (dead) return "Dead"
        if (Location.inBoss) return "Boss"
        val x = (mapX - MapUtils.startCorner.first) / (MapUtils.roomSize + MapUtils.connectorSize)
        val z = (mapZ - MapUtils.startCorner.second) / (MapUtils.roomSize + MapUtils.connectorSize)
        return (Dungeon.Info.dungeonList.getOrNull(x * 2 + z * 22) as? Room)?.data?.name ?: "Error"
    }
}
