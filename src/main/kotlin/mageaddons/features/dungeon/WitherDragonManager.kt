package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.features.dungeon.impl.DragonEnum
import mageaddons.utils.DungeonUtil.getF7Phase
import mageaddons.utils.MessageUtils.modMessage
import mageaddons.utils.impl.M7Phases
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

object WitherDragonManager: ModuleFactory(
    name = "WitherDragon Manager",
    toggle = Config.dragonHelper
) {
    var enabled: Boolean = toggle
        private set
    var priorityDragon = DragonEnum.None
    var currentTick: Long = 0
    private var arrowsHit: Int = 0

    @SubscribeEvent
    fun onPacket(event: PlaySoundEvent){
        if(getF7Phase() !== M7Phases.P5) return
        if (event.name != "random.successful_hit" || priorityDragon == DragonEnum.None) return
        if (priorityDragon.entity?.isEntityAlive == true && currentTick - priorityDragon.spawnedTime < priorityDragon.skipKillTime) arrowsHit++

    }

    @SubscribeEvent
    fun onServerTick(event: TickEvent.ServerTickEvent) {
        currentTick++
    }

    fun arrowDeath(dragon: DragonEnum) {
        if (currentTick - dragon.spawnedTime >= dragon.skipKillTime) return
        modMessage("§fYou hit §6$arrowsHit §farrows on §${dragon.colorCode}${dragon.name}.")
        arrowsHit = 0
    }

    fun arrowSpawn(dragon: DragonEnum) {
        if (priorityDragon == DragonEnum.None || dragon != priorityDragon) return
        arrowsHit = 0
            if (dragon.entity?.isEntityAlive != true && arrowsHit <= 0) return
            modMessage("§fYou hit §6${arrowsHit} §farrows on §${dragon.colorCode}${dragon.name}${if (dragon.entity?.isEntityAlive == true) " §fin §c${String.format(
                Locale.US, "%.2f", dragon.skipKillTime.toFloat()/20)} §fSeconds." else "."}")
            arrowsHit = 0
    }

}