package mageaddons.utils

import mageaddons.config.Config
import mageaddons.events.ChatEvent
import mageaddons.features.dungeon.Dungeon
import mageaddons.utils.impl.Floor
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.scoreboard.ScoreboardDisplaySlot

object Location {
    private val mc: MinecraftClient get() = MinecraftClient.getInstance()

    private var onHypixel = false

    enum class Island(
        val displayName: String,
    ) {
        Unknown("Unknown"),
        PrivateIsland("Private Island"),
        Hub("Hub"),
        Dungeon("Dungeon"),
        SpiderDen("Spider's Den"),
        CrimsonIsle("Crimson Isle"),
        End("The End"),
        GoldMine("Gold Mine"),
        DeepCaverns("Deep Caverns"),
        DwarvenMines("Dwarven Mines"),
        CrystalHollows("Crystal Hollows"),
        Park("The Park"),
        FarmingIsland("The Farming Islands"),
        DungeonHub("Dungeon Hub"),
        Barn("Barn"),
        Desert("Desert Settlement"),
        Garden("Garden"),
        Rift("The Rift"),
    }

    var island = Island.Unknown
    val inDungeons: Boolean get() = island == Island.Dungeon
    var dungeonFloor = -1
    var masterMode = false
    var inBoss = false
    var currentDungeon: Floor? = null
        private set
    var currentArea: Island = Island.Unknown
    var isInSkyblock: Boolean = false

    private val islandRegex = Regex("^§r§b§l(?:Area|Dungeon): §r§7(.+)§r\$")

    private val entryMessages =
        listOf(
            "[BOSS] Bonzo: Gratz for making it this far, but I'm basically unbeatable.",
            "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
            "[BOSS] The Professor: I was burdened with terrible news recently...",
            "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
            "[BOSS] Livid: Welcome, you've arrived right on time. I am Livid, the Master of Shadows.",
            "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!",
        )

    private var tickCount = 0

    fun onTick() {
        if (mc.world == null) return
        tickCount++
        if (tickCount % 20 != 0) return
        if (Config.forceSkyblock) {
            isInSkyblock = true
            island = Island.Dungeon
            dungeonFloor = 7
            return
        }

        isInSkyblock = onHypixel && mc.world
            ?.scoreboard
            ?.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR)
            ?.name == "SBScoreboard"

        if (island == Island.Unknown) {
            TabList
                .getTabList()
                .firstNotNullOfOrNull { islandRegex.find(it.second) }
                ?.groupValues
                ?.getOrNull(1)
                ?.let { areaName ->
                    Island.entries.find { it.displayName == areaName }?.let { island = it }
                }
        }

        if (island == Island.Dungeon && dungeonFloor == -1) {
            Scoreboard
                .getLines()
                .find {
                    Scoreboard.cleanLine(it).run {
                        contains("The Catacombs (") && !contains("Queue")
                    }
                }?.let {
                    val line = it.substringBefore(")")
                    dungeonFloor = line.lastOrNull()?.digitToIntOrNull() ?: 0
                    masterMode = line[line.length - 2] == 'M'
                }
        }
    }

    fun onChat(event: ChatEvent) {
        if (event.type.toInt() == 2 || !inDungeons) return
        if (event.text.startsWith("[BOSS] Maxor: ")) inBoss = true
        if (entryMessages.any { it == event.text }) inBoss = true
    }

    fun onConnect() {
        onHypixel =
            mc
                .runCatching {
                    mc.player
                        ?.networkHandler
                        ?.connection
                        ?.address
                        ?.toString()
                        ?.contains("hypixel") == true
                }.getOrDefault(false)
    }

    fun onWorldUnload() {
        island = Island.Unknown
        dungeonFloor = -1
        inBoss = false
        currentDungeon = null
    }

    fun onDisconnect() {
        onHypixel = false
        isInSkyblock = false
        onWorldUnload()
    }
}
