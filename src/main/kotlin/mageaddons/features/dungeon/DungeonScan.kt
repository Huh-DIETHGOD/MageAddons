package mageaddons.features.dungeon

import mageaddons.MageAddons.mc
import mageaddons.config.Config
import mageaddons.core.RoomData
import mageaddons.core.map.*
import mageaddons.utils.Location.dungeonFloor
import mageaddons.utils.RoomDataLoader
import mageaddons.utils.Utils.equalsOneOf
import mageaddons.utils.Utils.sendClientMessage
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkSectionPos
import kotlin.math.ceil

/**
 * Handles everything related to scanning the dungeon. Running [scan] will update [Dungeon.Info].
 */
object DungeonScan {
    /**
     * The size of each dungeon room in blocks.
     */
    const val roomSize = 32

    /**
     * The starting coordinates to start scanning (the north-west corner).
     */
    const val startX = -185
    const val startZ = -185

    private var lastScanTime = 0L
    var isScanning = false
    var hasScanned = false

    val shouldScan: Boolean
        get() = Config.autoScan && !isScanning && !hasScanned &&
            System.currentTimeMillis() - lastScanTime >= 250 && dungeonFloor != -1

    fun scan() {
        isScanning = true
        var allChunksLoaded = true

        // Scans the dungeon in a 11x11 grid.
        for (x in 0..10) {
            for (z in 0..10) {
                // Translates the grid index into world position.
                val xPos = startX + x * (roomSize / 2)
                val zPos = startZ + z * (roomSize / 2)

                val chunkPos = ChunkSectionPos.from(xPos, 0, zPos)
                val world = mc.world ?: continue
                if (!world.isChunkLoaded(chunkPos.x shr 2, chunkPos.z shr 2)) {
                    allChunksLoaded = false
                    continue
                }

                // Skip already scanned rooms
                if (Dungeon.Info.dungeonList[x + z * 11].run {
                        this !is Unknown && (this as? Room)?.data?.name != "Unknown"
                    }) continue

                scanRoom(xPos, zPos, z, x)?.let { tile ->
                    val prev = Dungeon.Info.dungeonList[z * 11 + x]
                    if (tile is Room) {
                        if ((prev as? Room)?.uniqueRoom != null) {
                            prev.uniqueRoom?.addTile(x, z, tile)
                        } else if (Dungeon.Info.uniqueRooms.none { unique -> unique.name == tile.data.name }) {
                            UniqueRoom(x, z, tile)
                        }
                        MapUpdate.roomAdded = true
                    }
                    Dungeon.Info.dungeonList[z * 11 + x] = tile
                    MapRenderList.renderUpdated = true
                }
            }
        }

        if (MapUpdate.roomAdded) {
            MapUpdate.updateUniques()
        }

        if (allChunksLoaded) {
            hasScanned = true
            if (Config.scanChatInfo) {
                val maxSecrets = ceil(Dungeon.Info.secretCount * ScoreCalculation.getSecretPercent())
                var maxBonus = 5
                if (dungeonFloor.equalsOneOf(6, 7)) maxBonus += 2
                if (ScoreCalculation.paul) maxBonus += 10
                val minSecrets = ceil(maxSecrets * (40 - maxBonus) / 40).toInt()

                sendClientMessage("§aScan Finished!")
                sendClientMessage("§aPuzzles (§c${Dungeon.Info.puzzles.size}§a): §d${Dungeon.Info.puzzles.keys.joinToString { it.roomDataName }}")
                sendClientMessage("§6Trap: §a${Dungeon.Info.trapType}")
                sendClientMessage("§8Wither Doors: §7${Dungeon.Info.witherDoors - 1}")
                sendClientMessage("§7Total Crypts: §6${Dungeon.Info.cryptCount}")
                sendClientMessage("§7Total Secrets: §b${Dungeon.Info.secretCount}")
                sendClientMessage("§7Minimum Secrets: §e$minSecrets")
            }
        }

        lastScanTime = System.currentTimeMillis()
        isScanning = false
    }

    /**
     * Scans a single room at the given world coordinates.
     * @return The Tile (Room or Door) found at this position, or null
     */
    fun scanRoom(worldX: Int, worldZ: Int, gridZ: Int, gridX: Int): Tile? {
        val world = mc.world ?: return null
        // Check center block to determine room type
        val centerX = worldX + roomSize / 4
        val centerZ = worldZ + roomSize / 4

        // Check for door tiles (at corners of rooms)
        if (gridX % 2 == 1 && gridZ % 2 == 1) {
            // This is between 4 rooms - check for door
            val pos = BlockPos(worldX, 70, worldZ)
            val block = world.getBlockState(pos)
            val doorType = DoorType.fromMapColor(block.block.hashCode() and 0xFF)
            if (doorType != null) {
                val door = Door(gridX, gridZ, doorType)
                if (doorType == DoorType.WITHER) {
                    Dungeon.Info.witherDoors++
                }
                return door
            }
        }

        // Scan room center for type identification
        val blockStates = mutableListOf<net.minecraft.block.BlockState>()
        for (dx in 0 until roomSize / 2 step 4) {
            for (dz in 0 until roomSize / 2 step 4) {
                val pos = BlockPos(worldX + dx, 70, worldZ + dz)
                val state = world.getBlockState(pos)
                if (state.block != Blocks.AIR) {
                    blockStates.add(state)
                }
            }
        }

        // Determine room type from blocks
        val roomType = determineRoomType(blockStates)
        val core = calculateCore(blockStates)
        val roomData = RoomDataLoader.findRoomData(core) ?: RoomData("Unknown", roomType)

        return Room(gridX, gridZ, roomData).also { room ->
            room.core = core
            when (roomType) {
                RoomType.PUZZLE -> {
                    roomData.puzzle?.let { Dungeon.Info.puzzles[it] = false }
                }
                RoomType.TRAP -> {
                    Dungeon.Info.trapType = roomData.name
                }
                else -> {}
            }
            Dungeon.Info.secretCount += roomData.secrets
        }
    }

    private fun determineRoomType(states: List<net.minecraft.block.BlockState>): RoomType {
        // Determine room type based on block composition
        // This is a simplified version - the full version uses specific block patterns
        if (states.any { it.block == Blocks.REDSTONE_BLOCK }) return RoomType.BLOOD
        if (states.any { it.block == Blocks.EMERALD_BLOCK }) return RoomType.ENTRANCE
        // More room type detection logic here...
        return RoomType.NORMAL
    }

    private fun calculateCore(states: List<net.minecraft.block.BlockState>): Int {
        // Calculate a hash from the blocks found in the room center
        return states.sumOf { it.block.hashCode() }.let { it xor (it ushr 16) } and 0xFFFF
    }
}
