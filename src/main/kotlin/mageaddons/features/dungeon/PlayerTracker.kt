package mageaddons.features.dungeon

import mageaddons.config.Config
import mageaddons.features.dungeon.Dungeon.dungeonTeammates
import mageaddons.utils.Utils.sendClientMessage

object PlayerTracker {
    val roomClears = mutableMapOf<String, Int>()

    /**
     * Called when the dungeon ends. If API key is configured, fetches player stats.
     */
    fun onDungeonEnd() {
        if (Config.apiKey.isBlank()) {
            sendClientMessage("§cPlease set your API key in the config to enable team info!")
            return
        }
        sendEndOfDungeonStats()
    }

    private fun sendEndOfDungeonStats() {
        val totalSecrets = Dungeon.Info.secretCount
        val clearCount = Dungeon.Info.uniqueRooms.count { it.getState().ordinal >= mageaddons.core.map.RoomState.CLEARED.ordinal }

        sendClientMessage("§b§lDungeon Completed!")
        sendClientMessage("§aTotal Secrets: §f$totalSecrets")
        sendClientMessage("§aRooms Cleared: §f$clearCount/${Dungeon.Info.roomCount}")

        dungeonTeammates.forEach { (name, player) ->
            val clears = roomClears.getOrDefault(name, 0)
            sendClientMessage("§7${player.displayName}: §f$clears room clears")
        }
    }

    fun reset() {
        roomClears.clear()
    }
}
