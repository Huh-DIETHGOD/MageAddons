package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.core.map.Door
import mageaddons.core.map.DoorType
import mageaddons.features.dungeon.Dungeon.Info
import mageaddons.features.dungeon.Dungeon.espDoors
import mageaddons.utils.Location
import mageaddons.utils.Location.inDungeons
import mageaddons.utils.RenderUtils
import mageaddons.utils.Utils.equalsOneOf
import mageaddons.utils.Utils.sendClientMessage
import net.minecraft.util.math.Box

object BloodCamp : ModuleFactory("Blood Camp Helper") {
    private const val WATCHER_MESSAGE = "[BOSS] The Watcher: "

    private val mobNames = listOf(
        "Fel", "Sniper", "Crypt Dreadlord", "Tank Zombie",
        "Skeleton Grunt", "Skeleton Lord", "Skeleton Master",
        "Parasite", "Prince", "Undead Priest", "Withermancer"
    )

    private var mobsRemaining = 0
    private var isActive = false
    private var watcherDone = false

    override fun onEnable() {
        isActive = Config.bloodCampHelper
    }

    override fun onTick() {
        if (!isActive || !inDungeons) return
        if (watcherDone) return
    }

    fun onChatMessage(message: String) {
        if (!Config.bloodCampHelper) return

        if (message.startsWith(WATCHER_MESSAGE)) {
            // Parse mob kill announcement
            val mobPart = message.substringAfter(WATCHER_MESSAGE)
            when {
                mobPart.contains("remains") -> {
                    mobsRemaining = mobPart.substringBefore(" ").toIntOrNull() ?: mobsRemaining
                    if (mobsRemaining == 0) {
                        watcherDone = true
                        sendClientMessage("§aBlood Camp Complete!")
                    }
                }
                mobPart.contains("has spawned") -> {
                    mobsRemaining = 5 // Default blood room spawns 5 mobs
                    watcherDone = false
                }
                mobPart.contains("defeated") -> {
                    mobsRemaining--
                    if (mobsRemaining <= 0) {
                        watcherDone = true
                        sendClientMessage("§aBlood Room Cleared!")
                    }
                }
            }
        }
    }

    private fun addEspDoors() {
        // Find unopened wither doors for ESP
        for (i in 0 until 121) {
            val tile = Info.dungeonList[i]
            if (tile is Door && tile.type == DoorType.WITHER && !tile.opened) {
                espDoors.add(tile)
            }
        }
    }
}
