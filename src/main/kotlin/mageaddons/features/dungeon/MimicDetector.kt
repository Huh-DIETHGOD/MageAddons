package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.features.dungeon.Dungeon.Info
import mageaddons.utils.Location
import mageaddons.utils.Utils.equalsOneOf
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient

object MimicDetector {
    private var checkedBlocks = mutableSetOf<Pair<Int, Int>>()

    /**
     * Attempts to find a mimic room by checking trapped chest locations.
     * Returns the room name if found.
     */
    fun findMimic(): String? {
        if (!Location.inDungeons) return null
        val world = MinecraftClient.getInstance().world ?: return null

        // Check each room for trapped chests
        Info.uniqueRooms.forEach { room ->
            if (room.type != mageaddons.core.map.RoomType.NORMAL) return@forEach

            room.tiles.forEach { (gx, gz) ->
                val worldX = DungeonScan.startX + gx * (DungeonScan.roomSize / 2)
                val worldZ = DungeonScan.startZ + gz * (DungeonScan.roomSize / 2)

                // Scan a small area in the room center for trapped chest
                for (dx in 0..DungeonScan.roomSize / 2 step 4) {
                    for (dz in 0..DungeonScan.roomSize / 2 step 4) {
                        val pos = net.minecraft.util.math.BlockPos(worldX + dx, 70, worldZ + dz)
                        val block = world.getBlockState(pos)
                        if (block.block == Blocks.TRAPPED_CHEST) {
                            return room.name
                        }
                    }
                }
            }
        }

        return null
    }

    /**
     * Checks if a mimic (usually disguised as a baby zombie on F6/M6) was killed.
     */
    fun checkMimicDead() {
        if (!Location.dungeonFloor.equalsOneOf(6, 7)) return
        if (Info.mimicFound) return

        // Mimic detection on F6/F7 relies on chat messages or entity tracking
        // In the full implementation, this uses entity death events
    }

    fun reset() {
        checkedBlocks.clear()
    }
}
