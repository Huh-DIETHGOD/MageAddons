package mageaddons.features.dungeon

import mageaddons.MageAddons.mc
import mageaddons.core.DungeonPlayer
import mageaddons.core.map.*
import mageaddons.utils.Location.dungeonFloor
import mageaddons.utils.impl.DungeonClass
import net.minecraft.util.Identifier

object MapUpdate {
    var roomAdded = false

    fun updateRooms() {
        // Updates room states based on map data changes
        // In the full implementation, this reads map color data to detect:
        // - Green rooms (fully cleared with secrets)
        // - Cleared rooms (mobs killed but secrets remaining)
        // - Failed rooms (puzzle failed)
        // - Opened doors
        // - Undiscovered rooms

        for (i in 0 until 121) {
            val tile = Dungeon.Info.dungeonList[i]
            if (tile is Room && tile.state == RoomState.UNDISCOVERED) {
                // Check if room has been entered via map data
                tile.state = RoomState.DISCOVERED
                tile.uniqueRoom?.setState(RoomState.DISCOVERED)
            }
            if (tile is Door) {
                if (tile.type == DoorType.WITHER && !tile.opened) {
                    // Check door open state via map data
                    // For now, just check if adjacent rooms are discovered
                    val x = tile.x
                    val z = tile.z
                    val neighbors = listOf(
                        Dungeon.Info.dungeonList.getOrNull((z - 1) * 11 + x),
                        Dungeon.Info.dungeonList.getOrNull((z + 1) * 11 + x)
                    )
                    if (neighbors.any { it is Room && it.state != RoomState.UNDISCOVERED }) {
                        tile.opened = true
                    }
                }
            }
        }

        MapRenderList.renderUpdated = true
    }

    fun updatePlayers(tabList: List<Pair<String, String>>) {
        val world = mc.world ?: return

        // Clear stale player data
        val currentPlayers = world.players.map { it.name.string }
        Dungeon.dungeonTeammates.keys.filter { it !in currentPlayers }.forEach {
            Dungeon.dungeonTeammates.remove(it)
        }

        // Update player positions from world
        world.players.forEach { player ->
            val name = player.name.string
            if (name == mc.player?.name?.string) return@forEach // Skip self

            val dungeonPlayer = Dungeon.dungeonTeammates.getOrPut(name) {
                DungeonPlayer(name)
            }

            dungeonPlayer.posX = player.x
            dungeonPlayer.posZ = player.z
            dungeonPlayer.yaw = player.headYaw

            // Extract class info from tab list
            tabList.find { it.first == name }?.let { (_, displayName) ->
                dungeonPlayer.dungeonClass = DungeonClass.fromName(
                    displayName.replace(Regex("§[0-9a-fk-or]"), "")
                )
                // Extract class level
                Regex("\\[?(\\d+)\\]?").find(displayName.replace(Regex("§[0-9a-fk-or]"), ""))
                    ?.groupValues?.getOrNull(1)?.toIntOrNull()?.let {
                        dungeonPlayer.classLevel = it
                    }
            }
        }
    }

    fun getPlayers() {
        mc.world?.players?.forEach { player ->
            val name = player.name.string
            if (name == mc.player?.name?.string) return@forEach
            Dungeon.dungeonTeammates.getOrPut(name) { DungeonPlayer(name) }
        }
    }

    fun preloadHeads() {
        Dungeon.dungeonTeammates.keys.forEach { name ->
            // In 1.21.1, we use the player's UUID to load their skin
            mc.player?.networkHandler?.playerList?.find {
                it.profile.name == name
            }?.let { entry ->
                val uuid = entry.profile.id
                val skinId = Identifier.of("mageaddons", "heads/${uuid.toString().replace("-", "")}")
                Dungeon.dungeonTeammates[name]?.skinTexture = skinId
            }
        }
    }

    fun updateUniques() {
        // After scanning, resolve unique room groupings
        Dungeon.Info.uniqueRooms.forEach { uniqueRoom ->
            // Ensure all rooms in a unique room group share the same state
            val bestState = uniqueRoom.tiles.map { (x, z) ->
                val tile = Dungeon.Info.dungeonList[z * 11 + x]
                if (tile is Room) tile.state else RoomState.UNDISCOVERED
            }.maxByOrNull { it.ordinal } ?: RoomState.UNDISCOVERED

            uniqueRoom.setState(bestState)
        }
    }
}
