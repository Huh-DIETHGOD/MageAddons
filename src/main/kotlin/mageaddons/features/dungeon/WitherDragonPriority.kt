package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.features.dungeon.SplitManager.easyPower
import mageaddons.features.dungeon.WitherDragonManager.enabled
import mageaddons.features.dungeon.impl.DragonEnum
import mageaddons.utils.DungeonUtil
import mageaddons.utils.MessageUtils.devMessage
import mageaddons.utils.MessageUtils.modMessage
import mageaddons.utils.PlayerUtils
import mageaddons.utils.equalsOneOf
import mageaddons.utils.impl.Blessing
import mageaddons.utils.impl.DungeonClass

object WitherDragonPriority {
    fun findPriority(spawningDragon: MutableList<DragonEnum>): DragonEnum {
        val priorityList = listOf(DragonEnum.Red, DragonEnum.Orange, DragonEnum.Blue, DragonEnum.Purple, DragonEnum.Green)
        return if (!enabled) {
            spawningDragon.sortBy { priorityList.indexOf(it) }
            spawningDragon[0]
        } else
            sortPriority(spawningDragon)
    }

    fun displaySpawningDragon(dragon: DragonEnum) {
        if (dragon == DragonEnum.None) return
        if (enabled && WitherDragonManager.enabled) PlayerUtils.alert("§${dragon.colorCode}${dragon.name} is spawning!", 30)
        if (enabled && WitherDragonManager.enabled) modMessage("§${dragon.colorCode}${dragon.name} §7is your priority dragon!")
    }

    private fun sortPriority(spawningDragon: MutableList<DragonEnum>): DragonEnum {
        val totalPower = Blessing.POWER.current * (if (Config.paulBonus) 1.25 else 1.0) + (if (Blessing.TIME.current > 0) 2.5 else 0.0)
        val playerClass = DungeonUtil.currentDungeonPlayer.clazz.apply { if (this == DungeonClass.Unknown) modMessage("§cFailed to get dungeon class.") }

        val dragonList = listOf(DragonEnum.Orange, DragonEnum.Green, DragonEnum.Red, DragonEnum.Blue, DragonEnum.Purple)
        val priorityList =
            if (totalPower >= 20 || (spawningDragon.any { it == DragonEnum.Purple } && totalPower >= easyPower!!))
                if (playerClass.equalsOneOf(DungeonClass.Berserk, DungeonClass.Mage)) dragonList else dragonList.reversed()
            else listOf(DragonEnum.Red, DragonEnum.Orange, DragonEnum.Blue, DragonEnum.Purple, DragonEnum.Green)

        spawningDragon.sortBy { priorityList.indexOf(it) }

        if (totalPower >= easyPower!!) {
            if (playerClass == DungeonClass.Tank || playerClass == DungeonClass.Healer) {
                if (spawningDragon.any { it == DragonEnum.Purple }) spawningDragon.sortByDescending { priorityList.indexOf(it) }
                }
        }
        devMessage("§7Priority: §6$totalPower §7Class: §${playerClass?.colorCode}${playerClass?.name} §7Dragons: §a${spawningDragon.joinToString(", ") { it.name }} §7-> §c${priorityList.joinToString(", ") { it.name.first().toString() }}")
        return spawningDragon[0]
    }

}