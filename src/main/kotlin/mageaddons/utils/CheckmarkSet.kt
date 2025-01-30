package mageaddons.utils

import mageaddons.MageAddons.mc
import mageaddons.core.map.RoomState
import mageaddons.features.dungeon.MapRender
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.util.ResourceLocation

class CheckmarkSet(val size: Int, location: String) {
    private val crossResource = ResourceLocation("mageaddons", "$location/cross.png")
    private val greenResource = ResourceLocation("mageaddons", "$location/green_check.png")
    private val questionResource = ResourceLocation("mageaddons", "$location/question.png")
    private val whiteResource = ResourceLocation("mageaddons", "$location/white_check.png")

    init {
        listOf(crossResource, greenResource, questionResource, whiteResource).forEach {
            mc.textureManager.loadTexture(it, SimpleTexture(it))
        }
    }

    fun getCheckmark(state: RoomState): ResourceLocation? {
        return when (state) {
            RoomState.CLEARED -> whiteResource
            RoomState.GREEN -> greenResource
            RoomState.FAILED -> crossResource
            RoomState.UNOPENED -> questionResource
            else -> null
        }
    }
}
