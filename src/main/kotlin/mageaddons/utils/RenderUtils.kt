package mageaddons.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.lwjgl.opengl.GL11

object RenderUtils {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    fun drawRect(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Color) {
        context.fill(x, y, x + width, y + height, color.rgb)
    }

    fun drawRectBorder(context: DrawContext, x: Int, y: Int, width: Int, height: Int, color: Color, thickness: Float = 1f) {
        val t = thickness.toInt()
        // Top
        context.fill(x, y, x + width, y + t, color.rgb)
        // Bottom
        context.fill(x, y + height - t, x + width, y + height, color.rgb)
        // Left
        context.fill(x, y, x + t, y + height, color.rgb)
        // Right
        context.fill(x + width - t, y, x + width, y + height, color.rgb)
    }

    fun drawCenteredText(context: DrawContext, text: String, x: Int, y: Int, color: Color, scale: Float = 1f) {
        val textRenderer = mc.textRenderer
        val matrices = context.matrices
        matrices.push()
        matrices.translate(x.toFloat(), y.toFloat(), 0f)
        matrices.scale(scale, scale, 1f)
        val textWidth = textRenderer.getWidth(text)
        context.drawText(textRenderer, Text.literal(text), -textWidth / 2, 0, color.rgb, false)
        matrices.pop()
    }

    fun drawText(context: DrawContext, text: String, x: Int, y: Int, color: Color, scale: Float = 1f) {
        val matrices = context.matrices
        matrices.push()
        matrices.translate(x.toFloat(), y.toFloat(), 0f)
        matrices.scale(scale, scale, 1f)
        context.drawText(mc.textRenderer, Text.literal(text), 0, 0, color.rgb, false)
        matrices.pop()
    }

    fun draw3DBox(box: Box, color: Color, outlineColor: Color, fillAlpha: Float = 0.25f) {
        val matrixStack = MatrixStack()
        drawFilledBox(matrixStack, box,
            color.red / 255f, color.green / 255f, color.blue / 255f, fillAlpha)
        drawBoxOutline(matrixStack, box,
            outlineColor.red / 255f, outlineColor.green / 255f, outlineColor.blue / 255f, outlineColor.alpha / 255f)
    }

    private fun drawFilledBox(matrices: MatrixStack, box: Box, r: Float, g: Float, b: Float, a: Float) {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableCull()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)

        val matrix = matrices.peek().positionMatrix

        // Bottom
        buffer.vertex(matrix, box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)

        // Top
        buffer.vertex(matrix, box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)

        // Sides
        buffer.vertex(matrix, box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)

        buffer.vertex(matrix, box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)

        buffer.vertex(matrix, box.minX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.maxY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.minY.toFloat(), box.minZ.toFloat()).color(r, g, b, a)

        buffer.vertex(matrix, box.minX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.minX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.maxY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)
        buffer.vertex(matrix, box.maxX.toFloat(), box.minY.toFloat(), box.maxZ.toFloat()).color(r, g, b, a)

        RenderSystem.setShader(GameRenderer::getPositionColorProgram)
        BufferRenderer.drawWithGlobalProgram(buffer.end())

        RenderSystem.enableCull()
        RenderSystem.disableBlend()
    }

    private fun drawBoxOutline(matrices: MatrixStack, box: Box, r: Float, g: Float, b: Float, a: Float) {
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableCull()
        RenderSystem.setShader(GameRenderer::getPositionColorProgram)

        val tessellator = Tessellator.getInstance()
        val buffer = tessellator.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)

        val matrix = matrices.peek().positionMatrix
        val x1 = box.minX.toFloat(); val y1 = box.minY.toFloat(); val z1 = box.minZ.toFloat()
        val x2 = box.maxX.toFloat(); val y2 = box.maxY.toFloat(); val z2 = box.maxZ.toFloat()

        // 12 edges
        val edges = listOf(
            Triple(x1, y1, z1) to Triple(x2, y1, z1), Triple(x2, y1, z1) to Triple(x2, y1, z2),
            Triple(x2, y1, z2) to Triple(x1, y1, z2), Triple(x1, y1, z2) to Triple(x1, y1, z1),
            Triple(x1, y2, z1) to Triple(x2, y2, z1), Triple(x2, y2, z1) to Triple(x2, y2, z2),
            Triple(x2, y2, z2) to Triple(x1, y2, z2), Triple(x1, y2, z2) to Triple(x1, y2, z1),
            Triple(x1, y1, z1) to Triple(x1, y2, z1), Triple(x2, y1, z1) to Triple(x2, y2, z1),
            Triple(x2, y1, z2) to Triple(x2, y2, z2), Triple(x1, y1, z2) to Triple(x1, y2, z2),
        )

        edges.forEach { (from, to) ->
            buffer.vertex(matrix, from.first, from.second, from.third).color(r, g, b, a)
            buffer.vertex(matrix, to.first, to.second, to.third).color(r, g, b, a)
        }

        RenderSystem.setShader(GameRenderer::getPositionColorProgram)
        BufferRenderer.drawWithGlobalProgram(buffer.end())

        RenderSystem.enableCull()
        RenderSystem.disableBlend()
    }
}
