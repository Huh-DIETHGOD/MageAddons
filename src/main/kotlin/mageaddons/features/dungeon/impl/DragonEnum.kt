package mageaddons.features.dungeon.impl

import mageaddons.features.dungeon.WitherDragonManager
import mageaddons.features.dungeon.WitherDragonManager.priorityDragon
import mageaddons.features.dungeon.WitherDragonPriority.displaySpawningDragon
import mageaddons.features.dungeon.WitherDragonPriority.findPriority
import mageaddons.utils.MessageUtils.modMessage
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.network.play.server.S2APacketParticles
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.Vec3
import org.lwjgl.util.Color
import org.lwjgl.util.ReadableColor

enum class DragonEnum(
    val spawnPos: Vec3,
    val boxesDimensions: AxisAlignedBB,
    val colorCode: Char,
    val color: ReadableColor,
    val xRange: ClosedRange<Double>,
    val zRange: ClosedRange<Double>,
    var timeToSpawn: Int = 100,
    var state: WitherDragonState = WitherDragonState.DEAD,
    var timesSpawned: Int = 0,
    var entityId: Int? = null,
    var entity: EntityDragon? = null,
    var isSprayed: Boolean = false,
    var spawnedTime: Long = 0,
    val skipKillTime: Int = 0
) {
    Red(Vec3(27.0, 14.0, 59.0), AxisAlignedBB(14.5, 13.0, 45.5, 39.5, 28.0, 70.5),'c', Color.RED,
        24.0..30.0, 56.0..62.0, skipKillTime = 50),

    Orange(Vec3(85.0, 14.0, 56.0), AxisAlignedBB(72.0, 8.0,  47.0, 102.0,28.0, 77.0),'6', Color.ORANGE,
        82.0..88.0, 53.0..59.0, skipKillTime = 62),

    Green(Vec3(27.0, 14.0, 94.0), AxisAlignedBB(7.0,  8.0,  80.0, 37.0, 28.0, 110.0),'a', Color.GREEN,
        23.0..29.0, 91.0..97.0, skipKillTime = 52),

    Blue(Vec3(84.0, 14.0, 94.0), AxisAlignedBB(71.5, 16.0, 82.5, 96.5, 26.0, 107.5),'b', Color.BLUE,
        82.0..88.0, 91.0..97.0, skipKillTime = 39),

    Purple(Vec3(56.0, 14.0, 125.0), AxisAlignedBB(45.5, 13.0, 113.5,68.5, 23.0, 136.5),'5', Color.PURPLE,
        53.0..59.0, 122.0..128.0, skipKillTime = 38),

    None(Vec3(0.0, 0.0, 0.0), AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0), 'f', Color.WHITE,
        0.0..0.0, 0.0..0.0);
}

enum class WitherDragonState {
    SPAWNING,
    ALIVE,
    DEAD
}

fun handleSpawnPacket(particle: S2APacketParticles) {
    if (
        particle.particleCount != 20 ||
        particle.yCoordinate != 19.0 ||
        particle.particleType != EnumParticleTypes.FLAME ||
        particle.xOffset != 2f ||
        particle.yOffset != 3f ||
        particle.zOffset != 2f ||
        particle.particleSpeed != 0f ||
        !particle.isLongDistance ||
        particle.xCoordinate % 1 != 0.0 ||
        particle.zCoordinate % 1 != 0.0
    ) return

    val (spawned, dragons) = DragonEnum.entries.fold(0 to mutableListOf<DragonEnum>()) { (spawned, dragons), dragon ->
        val newSpawned = spawned + dragon.timesSpawned

        if (dragon.state == WitherDragonState.SPAWNING) {
            if (dragon !in dragons) dragons.add(dragon)
            return@fold newSpawned to dragons
        }

        if (!checkParticle(particle, dragon)) return@fold newSpawned to dragons
        if (WitherDragonManager.enabled) modMessage("§${dragon.colorCode}$dragon §fdragon is spawning.")

        dragon.state = WitherDragonState.SPAWNING
        dragons.add(dragon)
        newSpawned to dragons
    }

    if (dragons.isNotEmpty() && (dragons.size == 2 || spawned >= 2) && (priorityDragon == DragonEnum.None || priorityDragon.entity?.isDead == false))
        priorityDragon = findPriority(dragons).also { displaySpawningDragon(it) }
}

private fun checkParticle(event: S2APacketParticles, dragon: DragonEnum): Boolean =
    event.xCoordinate in dragon.xRange && event.zCoordinate in dragon.zRange