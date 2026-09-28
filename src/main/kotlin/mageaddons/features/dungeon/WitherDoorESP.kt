package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.map.Door
import mageaddons.core.map.DoorType
import mageaddons.features.dungeon.Dungeon.Info
import mageaddons.utils.Color
import mageaddons.utils.Location
import mageaddons.utils.RenderUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Box

object WitherDoorESP {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    fun renderWorld() {
        if (!Location.inDungeons) return
        if (Config.witherDoorESP == 0) return

        val renderer = mc.worldRenderer
        if (renderer == null) return

        val camera = mc.gameRenderer?.camera ?: return

        Info.dungeonList.forEach { tile ->
            if (tile !is Door || tile.type != DoorType.WITHER || tile.opened) return@forEach

            // Convert grid position to world coordinates
            val worldX = DungeonScan.startX + tile.x * (DungeonScan.roomSize / 2)
            val worldZ = DungeonScan.startZ + tile.z * (DungeonScan.roomSize / 2)

            // Determine key color
            val outlineColor = if (Info.keys > 0) Config.witherDoorKeyColor else Config.witherDoorNoKeyColor
            val fillColor = outlineColor.withAlpha(Config.witherDoorFill)

            val box = Box(
                (worldX - 0.5).toDouble(), 68.0,
                (worldZ - 0.5).toDouble(),
                (worldX + 0.5).toDouble(), 73.0,
                (worldZ + 0.5).toDouble()
            )

            RenderUtils.draw3DBox(box, fillColor, outlineColor, Config.witherDoorFill)
        }
    }
}
