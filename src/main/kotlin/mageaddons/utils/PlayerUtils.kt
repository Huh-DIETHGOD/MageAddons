package mageaddons.utils

import mageaddons.MageAddons.mc
import mageaddons.utils.RenderUtils.drawText
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PlayerUtils {
    fun alert(
        title: String,
        time: Int = 2,
        color: Int = 0xFFFFFF,
        playSound: Boolean = true,
        displayText: Boolean = true,
    ) {
        if (displayText) this.displayTitle(title, time, color = color)
    }


    private var displayTitle = ""
    private var titleTicks = 0
    private var displayColor = 0xFFFFFF

    inline val posX get() = mc.thePlayer?.posX ?: 0.0
    inline val posY get() = mc.thePlayer?.posY ?: 0.0
    inline val posZ get() = mc.thePlayer?.posZ ?: 0.0

    fun displayTitle(title: String, ticks: Int, color: Int = 0xFFFFFF) {
        displayTitle = title
        titleTicks = ticks
        displayColor = color
    }

    @SubscribeEvent
    fun onOverlay(event: RenderGameOverlayEvent.Pre) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL || titleTicks <= 0) return
        mc.entityRenderer.setupOverlayRendering()
        val sr = ScaledResolution(mc)

        mcText(
            text = displayTitle, x = sr.scaledWidth / 2f,
            y = sr.scaledHeight / 2.5f, scale = 4.0,
            color = displayColor, center = true
        )
    }

    fun mcText(text: String, x: Number, y: Number, scale: Number, color: Int, shadow: Boolean = true, center: Boolean = true) {
        drawText("$text§r", x.toFloat(), y.toFloat(), scale.toDouble(), color, shadow, center)
    }


}
