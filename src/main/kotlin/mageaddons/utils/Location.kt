package mageaddons.utils

import mageaddons.MageAddons.mc
import mageaddons.config.Config
import mageaddons.events.ChatEvent
import mageaddons.features.dungeon.Dungeon
import mageaddons.utils.impl.Floor
import net.minecraft.client.network.NetHandlerPlayClient
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object Location {

    private var onHypixel = false
    var island = Island.Unknown
    val inDungeons
        get() = island == Island.Dungeon
    var dungeonFloor = -1
    var masterMode = false
    var inBoss = false
    var currentDungeon: Floor? = null
        private set
    var currentArea: Island = Island.Unknown
    var isInSkyblock: Boolean = false

    private var islandRegex = Regex("^§r§b§l(?:Area|Dungeon): §r§7(.+)§r\$")

    private val entryMessages = listOf(
        "[BOSS] Bonzo: Gratz for making it this far, but I'm basically unbeatable.",
        "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
        "[BOSS] The Professor: I was burdened with terrible news recently...",
        "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
        "[BOSS] Livid: Welcome, you've arrived right on time. I am Livid, the Master of Shadows.",
        "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!"
    )

    private var tickCount = 0

    fun onTick() {
        if (mc.theWorld == null) return
        tickCount++
        if (tickCount % 20 != 0) return
        if (Config.forceSkyblock) {
            isInSkyblock = true
            island = Island.Dungeon
            dungeonFloor = 7
            return
        }

        isInSkyblock = onHypixel && mc.theWorld.scoreboard?.getObjectiveInDisplaySlot(1)?.name == "SBScoreboard"

        if (island == Island.Unknown) {
            TabList.getTabList().firstNotNullOfOrNull { islandRegex.find(it.second) }
                ?.groupValues?.getOrNull(1)?.let { areaName ->
                    Island.entries.find { it.displayName == areaName }?.let { island = it }
                }
        }

        if (island == Island.Dungeon && dungeonFloor == -1) {
            Scoreboard.getLines().find {
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

    @SubscribeEvent
    fun onChat(event: ChatEvent) {
        if (event.packet.type.toInt() == 2 || !inDungeons) return
        if (event.text.startsWith("[BOSS] Maxor: ")) inBoss = true
        if (entryMessages.any { it == event.text }) inBoss = true
    }

    @SubscribeEvent
    fun onConnect(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        onHypixel = mc.runCatching {
            !event.isLocal && ((thePlayer?.clientBrand?.lowercase()?.contains("hypixel")
                ?: currentServerData?.serverIP?.lowercase()?.contains("hypixel")) == true)
        }.getOrDefault(false)
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        island = Island.Unknown
        dungeonFloor = -1
        inBoss = false
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        onHypixel = false
        isInSkyblock = false
        island = Island.Unknown
        dungeonFloor = -1
        inBoss = false
        currentDungeon = null
    }

    /**
     * Returns the current area from the tab list info.
     * If no info can be found, return Island.Unknown.
     *
     * @author Aton
     */
    private fun getArea(): Island {
        if (mc.isSingleplayer) return Island.SinglePlayer
        if (!isInSkyblock) return Island.Unknown
        val netHandlerPlayClient: NetHandlerPlayClient = mc.thePlayer?.sendQueue ?: return Island.Unknown
        val list = netHandlerPlayClient.playerInfoMap ?: return Island.Unknown

        val area = list.find {
            it?.displayName?.unformattedText?.startsWith("Area: ") == true ||
                    it?.displayName?.unformattedText?.startsWith("Dungeon: ") == true
        }?.displayName?.formattedText

        return Island.entries.firstOrNull { area?.contains(it.displayName, true) == true } ?: Island.Unknown
    }

    enum class Island(val displayName: String) {
        SinglePlayer("Singleplayer"),
        PrivateIsland("Private Island"),
        Garden("Garden"),
        SpiderDen("Spider's Den"),
        CrimsonIsle("Crimson Isle"),
        TheEnd("The End"),
        GoldMine("Gold Mine"),
        DeepCaverns("Deep Caverns"),
        DwarvenMines("Dwarven Mines"),
        GlaciteMineshaft("Mineshaft"),
        CrystalHollows("Crystal Hollows"),
        FarmingIsland("The Farming Islands"),
        ThePark("The Park"),
        Dungeon("Catacombs"),
        DungeonHub("Dungeon Hub"),
        Hub("Hub"),
        DarkAuction("Dark Auction"),
        JerryWorkshop("Jerry's Workshop"),
        Kuudra("Kuudra"),
        Unknown("(Unknown)");

        fun isArea(area: Island): Boolean = this == area

        fun isArea(vararg areas: Island): Boolean = this in areas
    }
}
