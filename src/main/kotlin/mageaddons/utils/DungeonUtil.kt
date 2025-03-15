package mageaddons.utils

import mageaddons.MageAddons.mc
import mageaddons.core.DungeonPlayer
import mageaddons.features.dungeon.Dungeon
import mageaddons.utils.Location.currentDungeon
import mageaddons.utils.Location.dungeonFloor
import mageaddons.utils.PlayerUtils.posY
import mageaddons.utils.impl.DungeonClass
import mageaddons.utils.impl.Floor
import mageaddons.utils.impl.M7Phases

object DungeonUtil {
    val floorNumber: Int
        get() = dungeonFloor

    val inBoss: Boolean
        get() = Location.inBoss

    val dungeonTeammates: ArrayList<DungeonPlayer>
        get() = ArrayList(Dungeon.dungeonTeammates.values) ?: ArrayList()

    val currentDungeonPlayer: DungeonPlayer
        get() = dungeonTeammates.find { it.name == mc.thePlayer?.name } ?: DungeonPlayer(mc.thePlayer.locationSkin)


    /**
     * Checks if the current dungeon floor number matches any of the specified options.
     *
     * @param options The floor number options to compare with the current dungeon floor.
     * @return `true` if the current dungeon floor matches any of the specified options, otherwise `false`.
     */
    fun isFloor(vararg options: Int): Boolean {
        return options.any { it == dungeonFloor }
    }

    /**
     * Gets the current phase of floor 7 boss.
     *
     * @return The current phase of floor 7 boss, or `null` if the player is not in the boss room.
     */
    fun getM7Phase(): M7Phases {
        if ((!isFloor(7) || !inBoss) && !Location.currentArea.isArea(Location.Island.SinglePlayer)) return M7Phases.Unknown

        return when {
            posY > 210 -> M7Phases.P1
            posY > 155 -> M7Phases.P2
            posY > 100 -> M7Phases.P3
            posY > 45 -> M7Phases.P4
            else -> M7Phases.P5
        }
    }
}