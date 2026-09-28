package mageaddons.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Items
import net.minecraft.item.map.MapState

object MapUtils {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    const val roomSize = 32
    const val startX = -185
    const val startZ = -185

    var calibrated = false
    var mapData: MapState? = null
    var mapDataUpdated = false

    /**
     * Attempts to calibrate the dungeon map by finding a filled map in the player's inventory
     * and extracting its MapState.
     */
    fun calibrateMap(): Boolean {
        val player = mc.player ?: return false

        // Find a filled map item in the player's inventory
        val mapStack = player.inventory.main.firstOrNull { it.item == Items.FILLED_MAP } ?: return false

        val mapId = mapStack.get(DataComponentTypes.MAP_ID) ?: return false
        val mapState = mc.world?.getMapState(mapId) ?: return false

        // Check if this is a dungeon map (has dungeon-like dimensions)
        if (mapState.dimension == mc.world?.dimension) {
            mapData = mapState
            return true
        }

        return false
    }

    /**
     * Checks if the map has been updated since last read
     */
    fun checkForMapUpdate() {
        val player = mc.player ?: return
        val mapStack = player.inventory.main.firstOrNull { it.item == Items.FILLED_MAP } ?: return
        val mapId = mapStack.get(DataComponentTypes.MAP_ID) ?: return
        val mapState = mc.world?.getMapState(mapId) ?: return

        if (mapState != mapData) {
            mapData = mapState
            mapDataUpdated = true
        }
    }

    /**
     * Gets a map color at the given map coordinates
     */
    fun getMapColor(x: Int, z: Int): Int? {
        val state = mapData ?: return null
        val colors = state.colors
        val index = x + z * 128
        if (index < 0 || index >= colors.size) return null
        return colors[index].toInt() and 0xFF
    }

    /**
     * Converts world coordinates to dungeon grid coordinates
     */
    fun worldToGrid(worldX: Int, worldZ: Int): Pair<Int, Int> {
        val gridX = (worldX - startX) / (roomSize / 2)
        val gridZ = (worldZ - startZ) / (roomSize / 2)
        return Pair(gridX, gridZ)
    }
}
