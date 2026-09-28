package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.utils.Location
import mageaddons.utils.RenderUtils
import mageaddons.utils.Color
import mageaddons.utils.Utils.sendClientMessage
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.Box

/**
 * Manages the F7/M7 P5 Wither Dragon fight.
 */
object WitherDragonManager {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    private enum class DragonState {
        NOT_SPAWNED, SPAWNED, DEAD
    }

    private data class Dragon(
        val name: String,
        val priority: Int,
        var state: DragonState = DragonState.NOT_SPAWNED
    )

    private val dragons = mutableListOf(
        Dragon("Maxor", 4),
        Dragon("Storm", 3),
        Dragon("Goldor", 2),
        Dragon("Necron", 1)
    )

    var isActive = false
    private var currentTarget: Dragon? = null

    fun onTick() {
        if (!Location.inDungeons) return
        if (!Config.dragonHelper) return
        if (!Location.inBoss) return

        // Check if in P5 (Necron phase after all dragons)
        if (dragons.all { it.state == DragonState.DEAD }) {
            isActive = false
            return
        }

        isActive = true

        // Find highest priority alive dragon
        currentTarget = dragons
            .filter { it.state == DragonState.SPAWNED }
            .minByOrNull { it.priority }
    }

    fun onDragonSpawn(name: String) {
        dragons.find { it.name.equals(name, ignoreCase = true) }?.let { dragon ->
            dragon.state = DragonState.SPAWNED
            if (Config.dragonHelper) {
                sendClientMessage("§c${dragon.name} has spawned! Priority: ${dragon.priority}")
            }
        }
    }

    fun onDragonDeath(name: String) {
        dragons.find { it.name.equals(name, ignoreCase = true) }?.let { dragon ->
            dragon.state = DragonState.DEAD
        }
    }

    fun renderWorld() {
        if (!isActive || !Config.dragonBox) return

        currentTarget?.let { dragon ->
            // Draw a box around the targeted dragon
            // The actual dragon positions are read from entity data
            mc.world?.entities?.forEach { entity ->
                if (entity.name.string.contains(dragon.name, ignoreCase = true)) {
                    val box = entity.boundingBox.expand(0.5, 0.5, 0.5)
                    RenderUtils.draw3DBox(box,
                        Color(255, 0, 0, (Config.witherDoorFill * 255).toInt()),
                        Color(255, 0, 0),
                        Config.witherDoorFill)
                }
            }
        }
    }

    fun reset() {
        dragons.forEach { it.state = DragonState.NOT_SPAWNED }
        isActive = false
        currentTarget = null
    }
}
