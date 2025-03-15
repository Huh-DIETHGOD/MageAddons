package mageaddons.utils

import gg.essential.elementa.dsl.toConstraint
import mageaddons.MageAddons.mc
import mageaddons.config.Config
import mageaddons.core.DungeonPlayer
import mageaddons.core.map.RoomState
import mageaddons.features.dungeon.DungeonScan
import mageaddons.utils.Utils.equalsOneOf
import mageaddons.utils.Utils.itemID
import gg.essential.elementa.utils.withAlpha
import mageaddons.utils.RenderUtils.worldRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_LINE_STRIP
import org.lwjgl.opengl.GL11.GL_QUADS
import java.awt.Color
import kotlin.math.roundToInt

object RenderUtils {

    val tessellator: Tessellator = Tessellator.getInstance()
    val worldRenderer: WorldRenderer = tessellator.worldRenderer
    val neuCheckmarks = CheckmarkSet(10, "neu")
    val defaultCheckmarks = CheckmarkSet(16, "default")
    val legacyCheckmarks = CheckmarkSet(8, "legacy")
    private val mapIcons = ResourceLocation("mageaddons", "marker.png")

    fun preDraw() {
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
    }

    fun postDraw() {
        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
    }

    fun addQuadVertices(x: Double, y: Double, w: Double, h: Double) {
        worldRenderer.pos(x, y + h, 0.0).endVertex()
        worldRenderer.pos(x + w, y + h, 0.0).endVertex()
        worldRenderer.pos(x + w, y, 0.0).endVertex()
        worldRenderer.pos(x, y, 0.0).endVertex()
    }

    fun drawTexturedQuad(x: Double, y: Double, width: Double, height: Double) {
        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        worldRenderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex()
        worldRenderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex()
        worldRenderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex()
        worldRenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex()
        tessellator.draw()
    }

    fun drawBox(
        aabb: AxisAlignedBB,
        color: Color,
        width: Float,
        outline: Float,
        fill: Float,
        ignoreDepth: Boolean
    ) {
        GlStateManager.pushMatrix()
        preDraw()
        GlStateManager.depthMask(!ignoreDepth)
        GL11.glLineWidth(width)

        drawOutlinedAABB(aabb, color.withAlpha(outline))

        drawFilledAABB(aabb, color.withAlpha(fill))

        GlStateManager.depthMask(true)
        postDraw()
        GlStateManager.popMatrix()
    }

    fun drawBlock(
        pos: BlockPos,
        color: Color,
        outlineWidth: Number = 3,
        outlineAlpha: Number = 1,
        fill: Number = 1,
        depth: Boolean = false,
        lineSmoothing: Boolean = true,
        expand: Double = 0.0
    ){
    }

    fun renderRect(x: Number, y: Number, w: Number, h: Number, color: Color) {
        if (color.alpha == 0) return
        preDraw()
        color.bind()

        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION)
        addQuadVertices(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
        tessellator.draw()

        postDraw()
    }

    fun renderRectBorder(x: Double, y: Double, w: Double, h: Double, thickness: Double, color: Color) {
        if (color.alpha == 0) return
        preDraw()
        color.bind()

        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION)
        addQuadVertices(x - thickness, y, thickness, h)
        addQuadVertices(x - thickness, y - thickness, w + thickness * 2, thickness)
        addQuadVertices(x + w, y, thickness, h)
        addQuadVertices(x - thickness, y + h, w + thickness * 2, thickness)
        tessellator.draw()

        postDraw()
    }

    fun renderCenteredText(text: List<String>, x: Int, y: Int, color: Int) {
        if (text.isEmpty()) return
        GlStateManager.pushMatrix()
        GlStateManager.translate(x.toFloat(), y.toFloat(), 0f)
        GlStateManager.scale(Config.textScale, Config.textScale, 1f)
        val fontHeight = mc.fontRendererObj.FONT_HEIGHT + 1
        val yTextOffset = text.size * fontHeight / -2f

        text.withIndex().forEach { (index, text) ->
            mc.fontRendererObj.drawString(
                text,
                mc.fontRendererObj.getStringWidth(text) / -2f,
                yTextOffset + index * fontHeight,
                color,
                true
            )
        }
        GlStateManager.popMatrix()
    }

    fun drawCheckmark(x: Float, y: Float, state: RoomState) {
        val (checkmark, size) = when (Config.mapCheckmark) {
            1 -> defaultCheckmarks.getCheckmark(state) to defaultCheckmarks.size.toDouble()
            2 -> neuCheckmarks.getCheckmark(state) to neuCheckmarks.size.toDouble()
            3 -> legacyCheckmarks.getCheckmark(state) to legacyCheckmarks.size.toDouble()
            else -> return
        }
        if (checkmark != null) {
            GlStateManager.enableAlpha()
            GlStateManager.color(1f, 1f, 1f, 1f)
            mc.textureManager.bindTexture(checkmark)

            drawTexturedQuad(
                x + (MapUtils.roomSize - size) / 2,
                y + (MapUtils.roomSize - size) / 2,
                size,
                size
            )
            GlStateManager.disableAlpha()
        }
    }

    fun drawPlayerHead(name: String, player: DungeonPlayer) {
        GlStateManager.pushMatrix()
        try {
            // Translates to the player's location which is updated every tick.
            if (player.isPlayer || name == mc.thePlayer.name) {
                GlStateManager.translate(
                    (mc.thePlayer.posX - DungeonScan.startX + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.first,
                    (mc.thePlayer.posZ - DungeonScan.startZ + 15) * MapUtils.coordMultiplier + MapUtils.startCorner.second,
                    0.0
                )
            } else {
                GlStateManager.translate(player.mapX.toFloat(), player.mapZ.toFloat(), 0f)
            }

            // Apply head rotation and scaling
            GlStateManager.rotate(player.yaw + 180f, 0f, 0f, 1f)
            GlStateManager.scale(Config.playerHeadScale, Config.playerHeadScale, 1f)
            GlStateManager.enableAlpha()

            if (Config.mapVanillaMarker && (player.isPlayer || name == mc.thePlayer.name)) {
                GlStateManager.rotate(180f, 0f, 0f, 1f)
                GlStateManager.color(1f, 1f, 1f, 1f)
                mc.textureManager.bindTexture(mapIcons)
                worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
                worldRenderer.pos(-6.0, 6.0, 0.0).tex(0.0, 0.0).endVertex()
                worldRenderer.pos(6.0, 6.0, 0.0).tex(1.0, 0.0).endVertex()
                worldRenderer.pos(6.0, -6.0, 0.0).tex(1.0, 1.0).endVertex()
                worldRenderer.pos(-6.0, -6.0, 0.0).tex(0.0, 1.0).endVertex()
                tessellator.draw()
                GlStateManager.rotate(-180f, 0f, 0f, 1f)
            } else {
                // Render black border around the player head
                renderRectBorder(-6.0, -6.0, 12.0, 12.0, 1.0, Color(0, 0, 0, 255))

                preDraw()
                GlStateManager.enableTexture2D()
                GlStateManager.color(1f, 1f, 1f, 1f)

                mc.textureManager.bindTexture(player.skin)

                Gui.drawScaledCustomSizeModalRect(-6, -6, 8f, 8f, 8, 8, 12, 12, 64f, 64f)
                if (player.renderHat) {
                    Gui.drawScaledCustomSizeModalRect(-6, -6, 40f, 8f, 8, 8, 12, 12, 64f, 64f)
                }

                postDraw()
            }

            // Handle player names
            if (Config.playerHeads == 2 || Config.playerHeads == 1 && mc.thePlayer.heldItem?.itemID.equalsOneOf(
                    "SPIRIT_LEAP",
                    "INFINITE_SPIRIT_LEAP",
                    "HAUNT_ABILITY"
                )
            ) {

                GlStateManager.rotate(-player.yaw + 180f, 0f, 0f, 1f)
                GlStateManager.translate(0f, 10f, 0f)
                GlStateManager.scale(Config.playerNameScale, Config.playerNameScale, 1f)
                mc.fontRendererObj.drawString(
                    name,
                    -mc.fontRendererObj.getStringWidth(name) / 2f,
                    0f,
                    0xffffff,
                    true
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        GlStateManager.popMatrix()
    }

    fun Color.bind() {
        GlStateManager.color(this.red / 255f, this.green / 255f, this.blue / 255f, this.alpha / 255f)
    }

    fun Color.grayScale(): Color {
        val gray = (red * 0.299 + green * 0.587 + blue * 0.114).roundToInt()
        return Color(gray, gray, gray, alpha)
    }

    fun Color.darken(factor: Float): Color {
        return Color((red * factor).roundToInt(), (green * factor).roundToInt(), (blue * factor).roundToInt(), alpha)
    }

    fun Entity.getInterpolatedPosition(partialTicks: Float): Triple<Double, Double, Double> {
        return Triple(
            this.lastTickPosX + (this.posX - this.lastTickPosX) * partialTicks,
            this.lastTickPosY + (this.posY - this.lastTickPosY) * partialTicks,
            this.lastTickPosZ + (this.posZ - this.lastTickPosZ) * partialTicks
        )
    }

    fun drawText(
        text: String,
        x: Float,
        y: Float,
        scale: Double = 1.0,
        color: mageaddons.utils.Color = mageaddons.utils.Color.WHITE,
        shadow: Boolean = true,
        center: Boolean = false
    ) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.translate(x, y, 0f)
        GlStateManager.scale(scale, scale, scale)
        var yOffset = y - mc.fontRendererObj.FONT_HEIGHT
        text.split("\n").forEach {
            yOffset += mc.fontRendererObj.FONT_HEIGHT
            val xOffset = if (center) {
                mc.fontRendererObj.getStringWidth(it) / -2f
            } else 0f
            mc.fontRendererObj.drawString(
                it,
                xOffset,
                0f,
                color.rgba,
                shadow
            )
        }
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    /**
     * @author https://github.com/odtheking/Odin
     */
    var partialTicks = 0f
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onRenderWorld(event: RenderWorldLastEvent) {
        this.partialTicks = event.partialTicks
    }
    /**
     * Gets the rendered x-coordinate of an entity based on its last tick and current tick positions.
     *
     * @receiver The entity for which to retrieve the rendered x-coordinate.
     * @return The rendered x-coordinate.
     */
    val Entity.renderX: Double
        get() = lastTickPosX + (posX - lastTickPosX) * partialTicks

    /**
     * Gets the rendered y-coordinate of an entity based on its last tick and current tick positions.
     *
     * @receiver The entity for which to retrieve the rendered y-coordinate.
     * @return The rendered y-coordinate.
     */
    val Entity.renderY: Double
        get() = lastTickPosY + (posY - lastTickPosY) * partialTicks

    /**
     * Gets the rendered z-coordinate of an entity based on its last tick and current tick positions.
     *
     * @receiver The entity for which to retrieve the rendered z-coordinate.
     * @return The rendered z-coordinate.
     */
    val Entity.renderZ: Double
        get() = lastTickPosZ + (posZ - lastTickPosZ) * partialTicks

    /**
     * Gets the rendered position of an entity as a `Vec3`.
     *
     * @receiver The entity for which to retrieve the rendered position.
     * @return The rendered position as a `Vec3`.
     */
    val Entity.renderVec: Vec3
        get() = Vec3(renderX, renderY, renderZ)
    fun AxisAlignedBB.outlineBounds(): AxisAlignedBB =
        expand(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)

    private fun addVertexesForOutlinedBox(aabb: AxisAlignedBB) {
        val minX = aabb.minX
        val minY = aabb.minY
        val minZ = aabb.minZ
        val maxX = aabb.maxX
        val maxY = aabb.maxY
        val maxZ = aabb.maxZ

        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION)
        worldRenderer.pos(minX, minY, minZ).endVertex()
        worldRenderer.pos(minX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, minZ).endVertex()
        worldRenderer.pos(minX, minY, minZ).endVertex()

        worldRenderer.pos(minX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(minX, maxY, minZ).endVertex()

        worldRenderer.pos(minX, maxY, maxZ).endVertex()
        worldRenderer.pos(minX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, minY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, maxZ).endVertex()
        worldRenderer.pos(maxX, maxY, minZ).endVertex()
        worldRenderer.pos(maxX, minY, minZ).endVertex()

    }

    fun drawFilledAABB(
        aabb: AxisAlignedBB,
        color: Color,
    ) {
        color.bind()

        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION)

        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()

        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
    }

    fun depth(depth: Boolean) {
        if (depth) GlStateManager.enableDepth() else GlStateManager.disableDepth()
        GlStateManager.depthMask(depth)
    }

    fun drawOutlinedAABB(
        aabb: AxisAlignedBB,
        color: Color,
    ) {
        color.bind()

        worldRenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION)

        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()

        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()

        tessellator.draw()
    }

    private fun resetDepth() {
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
    }
}
