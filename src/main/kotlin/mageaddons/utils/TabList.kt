package mageaddons.utils

import net.minecraft.client.MinecraftClient

object TabList {
    fun getTabList(): List<Pair<String, String>> {
        val client = MinecraftClient.getInstance()
        val networkHandler = client.player?.networkHandler ?: return emptyList()
        return networkHandler.playerList.map {
            it.profile.name to (it.displayName?.string ?: it.profile.name)
        }
    }

    fun getDungeonTabList(): List<Pair<String, String>> {
        val tabList = getTabList()
        // Filter for dungeon player entries (they have class/level info)
        return tabList.filter { (_, display) ->
            display.contains("Healer") || display.contains("Mage") ||
            display.contains("Berserk") || display.contains("Archer") ||
            display.contains("Tank")
        }
    }
}
