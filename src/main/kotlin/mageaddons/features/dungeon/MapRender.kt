package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.RoomData
import mageaddons.core.map.*
import mageaddons.features.dungeon.Dungeon.Info
import mageaddons.utils.Color
import mageaddons.utils.Location
import mageaddons.utils.MapUtils
import mageaddons.utils.RenderUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

/**
 * Renders the dungeon map as a HUD overlay using the 1.21.1 DrawContext API.
 */
object MapRender {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    // Map dimensions
    private const val TILE_SIZE = 6   // pixel size per grid tile
    private const val ROOM_SIZE = 12  // pixel size per room (2 tiles)
    private const val MAP_SIZE = TILE_SIZE * 11 // 66 pixels for 11x11 grid

    fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!Config.mapEnabled) return
        if (!Location.inDungeons) return
        if (Config.mapHideInBoss && Location.inBoss) return

        val x = Config.mapX
        val y = Config.mapY
        val scale = Config.mapScale

        val matrices = context.matrices
        matrices.push()
        matrices.translate(x.toFloat(), y.toFloat(), 0f)
        matrices.scale(scale, scale, 1f)

        // Draw map background
        val bgColor = Config.mapBackground
        RenderUtils.drawRect(context, 0, 0, (MAP_SIZE * scale).toInt(), (MAP_SIZE * scale).toInt(), bgColor)

        // Draw border
        if (Config.mapBorderWidth > 0) {
            RenderUtils.drawRectBorder(context, 0, 0, MAP_SIZE, MAP_SIZE,
                Config.mapBorder, Config.mapBorderWidth)
        }

        // Draw tiles
        for (z in 0..10) {
            for (x in 0..10) {
                val tile = Info.dungeonList[z * 11 + x]
                if (tile is Unknown) continue
                drawTile(context, tile, x * TILE_SIZE, z * TILE_SIZE)
            }
        }

        // Draw player positions
        drawPlayers(context)

        // Draw room names
        if (Config.mapRoomNames > 0) {
            drawRoomNames(context)
        }

        // Draw run information below map
        if (Config.mapShowRunInformation) {
            drawRunInfo(context)
        }

        matrices.pop()
    }

    private fun drawTile(context: DrawContext, tile: Tile, px: Int, pz: Int) {
        val color = when {
            tile is Room -> {
                if (tile.state == RoomState.UNDISCOVERED && Config.mapDarkenUndiscovered) {
                    tile.color.darker(Config.mapDarkenPercent)
                } else tile.color
            }
            tile is Door -> {
                if (tile.type == DoorType.WITHER && tile.opened && Config.mapGrayUndiscovered) {
                    Color(128, 128, 128)
                } else tile.color
            }
            else -> tile.color
        }

        RenderUtils.drawRect(context, px, pz, TILE_SIZE, TILE_SIZE, color)

        // Draw checkmark for cleared rooms
        if (tile is Room && Config.mapCheckmark > 0) {
            when (tile.state) {
                RoomState.GREEN -> {
                    if (Config.mapCenterCheckmark) {
                        drawGreenCheck(context, px + TILE_SIZE / 2, pz + TILE_SIZE / 2)
                    }
                }
                RoomState.CLEARED -> {
                    if (Config.mapCenterCheckmark) {
                        drawWhiteCheck(context, px + TILE_SIZE / 2, pz + TILE_SIZE / 2)
                    }
                }
                RoomState.FAILED -> {
                    if (Config.mapCenterCheckmark) {
                        drawRedX(context, px + TILE_SIZE / 2, pz + TILE_SIZE / 2)
                    }
                }
                else -> {}
            }
        }
    }

    private fun drawGreenCheck(context: DrawContext, x: Int, y: Int) {
        RenderUtils.drawText(context, "✓", x - 3, y - 4, Color(85, 255, 85), 0.6f)
    }

    private fun drawWhiteCheck(context: DrawContext, x: Int, y: Int) {
        RenderUtils.drawText(context, "✓", x - 3, y - 4, Color.WHITE, 0.6f)
    }

    private fun drawRedX(context: DrawContext, x: Int, y: Int) {
        RenderUtils.drawText(context, "✗", x - 3, y - 4, Color(255, 0, 0), 0.6f)
    }

    private fun drawPlayers(context: DrawContext) {
        val player = mc.player ?: return
        // Draw self
        val (selfGx, selfGz) = MapUtils.worldToGrid(player.blockX, player.blockZ)
        val selfPx = selfGx * TILE_SIZE + TILE_SIZE / 2
        val selfPz = selfGz * TILE_SIZE + TILE_SIZE / 2

        if (selfGx in 0..10 && selfGz in 0..10) {
            RenderUtils.drawRect(context, selfPx - 2, selfPz - 2, 4, 4, Color.WHITE)
        }

        // Draw teammates
        Dungeon.dungeonTeammates.forEach { (name, dp) ->
            val (gx, gz) = MapUtils.worldToGrid(dp.posX.toInt(), dp.posZ.toInt())
            val px = gx * TILE_SIZE + TILE_SIZE / 2
            val pz = gz * TILE_SIZE + TILE_SIZE / 2

            if (gx in 0..10 && gz in 0..10) {
                RenderUtils.drawRect(context, px - 2, pz - 2, 4, 4,
                    when (dp.dungeonClass) {
                        mageaddons.utils.impl.DungeonClass.HEALER -> Color(255, 255, 85)
                        mageaddons.utils.impl.DungeonClass.MAGE -> Color(85, 255, 255)
                        mageaddons.utils.impl.DungeonClass.BERSERK -> Color(255, 85, 85)
                        mageaddons.utils.impl.DungeonClass.ARCHER -> Color(85, 255, 85)
                        mageaddons.utils.impl.DungeonClass.TANK -> Color(170, 170, 170)
                        else -> Color(255, 255, 255)
                    })

                // Draw player name if enabled
                if (Config.playerHeads > 0) {
                    RenderUtils.drawCenteredText(context, name,
                        px, pz + 6, Color.WHITE, Config.playerNameScale)
                }
            }
        }
    }

    private fun drawRoomNames(context: DrawContext) {
        Info.uniqueRooms.forEach { room ->
            val showName = when (Config.mapRoomNames) {
                2 -> true // All rooms
                1 -> room.type == RoomType.PUZZLE || room.type == RoomType.TRAP
                else -> false
            }

            if (!showName) return@forEach

            // Calculate center position of the unique room
            val centerX = room.tiles.map { it.first }.average().toInt() * TILE_SIZE + TILE_SIZE / 2
            val centerZ = room.tiles.map { it.second }.average().toInt() * TILE_SIZE + TILE_SIZE / 2

            val textColor = when {
                room.getState() == RoomState.GREEN -> Config.colorTextGreen
                room.getState() == RoomState.CLEARED -> Config.colorTextCleared
                room.getState() == RoomState.FAILED -> Config.colorTextFailed
                else -> Config.colorTextUncleared
            }

            // Show secrets if enabled
            if (Config.mapRoomSecrets > 0 && room.type != RoomType.BLOOD && room.type != RoomType.ENTRANCE) {
                val secretCount = room.getSecretCount()
                if (secretCount > 0) {
                    RenderUtils.drawCenteredText(context,
                        "[${secretCount}]", centerX, centerZ - 4, Color.WHITE, Config.textScale)
                }
            }

            RenderUtils.drawCenteredText(context,
                room.name, centerX, centerZ + 2, textColor, Config.textScale)
        }
    }

    private fun drawRunInfo(context: DrawContext) {
        val startY = MAP_SIZE + 4

        if (Config.runInformationScore) {
            RenderUtils.drawText(context, "Score: ${ScoreCalculation.getScoreDisplay()}",
                0, startY, Color.WHITE, 0.65f)
        }
    }
}
