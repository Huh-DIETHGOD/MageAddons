package mageaddons.config

import mageaddons.features.QOL.Autofisher
import mageaddons.utils.Color
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object Config {
    // ===== Dungeon General =====
    var mapEnabled = true
    var autoScan = true
    var scanChatInfo = true
    var mapShowRunInformation = true
    var scoreElementEnabled = false
    var bloodCampHelper = false
    var witherDoorESP = 0
    var mapX = 10
    var mapY = 10
    var mapScale = 1.25f

    // ===== Dungeon Message =====
    var mimicMessageEnabled = false
    var mimicMessage = "Mimic Killed!"
    var scoreMessage = 0
    var scoreTitle = 0
    var message270 = "270 Score"
    var message300 = "300 Score"
    var timeTo300 = false

    // ===== Dungeon Map =====
    var mapHideInBoss = false
    var playerHeads = 0
    var scoreHideInBoss = false
    var playerNameScale = 0.8f
    var playerHeadScale = 1.0f
    var mapRoomNames = 2
    var mapVanillaMarker = false
    var textScale = 0.75f

    // ===== Dungeon Map Render =====
    var mapBackground = Color(0, 0, 0, 100)
    var mapBorder = Color(0, 0, 0, 255)
    var mapBorderWidth = 3f
    var mapDarkenUndiscovered = true
    var mapDarkenPercent = 0.4f
    var mapGrayUndiscovered = false
    var mapRoomSecrets = 0
    var mapCenterRoomName = true
    var mapColorText = true
    var mapCheckmark = 1
    var mapCenterCheckmark = true

    // ===== Dungeon P5 =====
    var dragonHelper = false
    var splitManager = false
    var easySplitPower = 0
    var dragonBox = false

    // ===== Dungeon Blessing =====
    var blessingDisplay = false
    var displayPower = true
    var displayTime = true
    var displayStone = false
    var displayLife = false
    var displayWisdom = false
    var blessingX = 10
    var blessingY = 10

    // ===== Dungeon Color =====
    var colorBloodDoor = Color(231, 0, 0)
    var colorEntranceDoor = Color(20, 133, 0)
    var colorRoomDoor = Color(92, 52, 14)
    var colorWitherDoor = Color(0, 0, 0)
    var colorOpenWitherDoor = Color(92, 52, 14)
    var colorUnopenedDoor = Color(65, 65, 65)
    var colorBlood = Color(255, 0, 0)
    var colorEntrance = Color(20, 133, 0)
    var colorFairy = Color(224, 0, 255)
    var colorMiniboss = Color(254, 223, 0)
    var colorRoom = Color(107, 58, 17)
    var colorRoomMimic = Color(186, 66, 52)
    var colorPuzzle = Color(117, 0, 133)
    var colorRare = Color(255, 203, 89)
    var colorTrap = Color(216, 127, 51)
    var colorUnopened = Color(65, 65, 65)
    var colorTextCleared = Color(255, 255, 255)
    var colorTextUncleared = Color(170, 170, 170)
    var colorTextGreen = Color(85, 255, 85)
    var colorTextFailed = Color(255, 255, 255)
    var witherDoorNoKeyColor = Color(255, 0, 0)
    var witherDoorKeyColor = Color(0, 255, 0)

    // ===== Dungeon Score =====
    var scoreX = 10
    var scoreY = 10
    var scoreScale = 1.0f
    var scoreTotalScore = 2
    var scoreMinimizedName = false
    var scoreAssumeSpirit = true
    var scoreSecrets = 1
    var scoreCrypts = false
    var scoreMimic = false
    var scoreDeaths = false
    var scorePuzzles = 0

    // ===== Dungeon Run Information =====
    var runInformationScore = true
    var runInformationSecrets = 1
    var runInformationCrypts = true
    var runInformationMimic = true
    var runInformationDeaths = true

    // ===== QOL =====
    var equipmentHotKeyEnabled = false
    var wardrobeHotKeyEnabled = false

    // ===== Other Features =====
    var witherDoorOutlineWidth = 3f
    var apiKey = ""
    var teamInfo = false
    var witherDoorOutline = 1.0f
    var witherDoorFill = 0.25f

    // ===== Combat RagAxe =====
    var ragAxe = false
    var ragAxeAnnouncer = false

    // ==== QOL Autofisher ====
    var autofisher = false
    var autofisherTickBetweenFish = 20
    var autofisherPullBackTick = 20

    // ===== Debug =====
    var developerMode = false
    var testCommandEnabled = false
    var forceSkyblock = false
    var paulBonus = false
    var renderBeta = false
    var forceBlessingDisplay = false

    // ===== Config GUI =====

    /** Convert an ARGB int (as used by Cloth Config color fields) to our Color */
    private fun Int.fromArgb(): Color = Color((this shr 16) and 0xFF, (this shr 8) and 0xFF, this and 0xFF, (this shr 24) and 0xFF)

    fun createConfigScreen(parent: Screen?): Screen {
        val builder =
            ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Mage Addons"))

        val entryBuilder = builder.entryBuilder()

        // Dungeon General
        val dungeonGeneral = builder.getOrCreateCategory(Text.literal("Dungeon - General"))
        addDungeonGeneral(entryBuilder, dungeonGeneral)

        // Dungeon Message
        val dungeonMessage = builder.getOrCreateCategory(Text.literal("Dungeon - Message"))
        addDungeonMessage(entryBuilder, dungeonMessage)

        // Dungeon Map
        val dungeonMap = builder.getOrCreateCategory(Text.literal("Dungeon - Map"))
        addDungeonMap(entryBuilder, dungeonMap)

        // Dungeon Map Render
        val dungeonRender = builder.getOrCreateCategory(Text.literal("Dungeon - Map Render"))
        addDungeonMapRender(entryBuilder, dungeonRender)

        // Dungeon P5
        val dungeonP5 = builder.getOrCreateCategory(Text.literal("Dungeon - P5"))
        addDungeonP5(entryBuilder, dungeonP5)

        // Dungeon Blessing
        val dungeonBlessing = builder.getOrCreateCategory(Text.literal("Dungeon - Blessing"))
        addDungeonBlessing(entryBuilder, dungeonBlessing)

        // Dungeon Colors
        val dungeonColor = builder.getOrCreateCategory(Text.literal("Dungeon - Colors"))
        addDungeonColors(entryBuilder, dungeonColor)

        // Dungeon Score
        val dungeonScore = builder.getOrCreateCategory(Text.literal("Dungeon - Score"))
        addDungeonScore(entryBuilder, dungeonScore)

        // Dungeon Run Information
        val dungeonRunInfo = builder.getOrCreateCategory(Text.literal("Dungeon - Run Info"))
        addDungeonRunInfo(entryBuilder, dungeonRunInfo)

        // Combat
        val combat = builder.getOrCreateCategory(Text.literal("Combat"))
        addCombat(entryBuilder, combat)

        // QOL
        val qol = builder.getOrCreateCategory(Text.literal("QOL"))
        addQOL(entryBuilder, qol)

        // Other Features
        val other = builder.getOrCreateCategory(Text.literal("Other Features"))
        addOtherFeatures(entryBuilder, other)

        // Debug
        val debug = builder.getOrCreateCategory(Text.literal("Debug"))
        addDebug(entryBuilder, debug)

        builder.setSavingRunnable {
            // Config is already updated via the field references
            // Sync the Autofisher module state with the (possibly changed) config
            Autofisher.syncFromConfig()
        }

        return builder.build()
    }

    private fun addDungeonGeneral(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Map Enabled"), mapEnabled)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Render the map"))
                .setSaveConsumer { mapEnabled = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Auto Scan"), autoScan)
                .setDefaultValue(
                    true,
                ).setTooltip(Text.literal("Automatically scans when entering dungeon"))
                .setSaveConsumer { autoScan = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Chat Info"), scanChatInfo)
                .setDefaultValue(
                    true,
                ).setTooltip(Text.literal("Show dungeon overview info after scanning"))
                .setSaveConsumer { scanChatInfo = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Show Run Info"), mapShowRunInformation)
                .setDefaultValue(
                    true,
                ).setTooltip(Text.literal("Shows run information under map"))
                .setSaveConsumer { mapShowRunInformation = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Score Element"), scoreElementEnabled)
                .setDefaultValue(
                    false,
                ).setTooltip(Text.literal("Shows separate score element"))
                .setSaveConsumer { scoreElementEnabled = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Blood Camp Helper"), bloodCampHelper)
                .setDefaultValue(
                    false,
                ).setTooltip(Text.literal("Helps blood camp and announcements"))
                .setSaveConsumer { bloodCampHelper = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Wither Door ESP"), witherDoorESP, 0, 2)
                .setDefaultValue(0)
                .setTextGetter { value ->
                    Text.literal(
                        when (value) {
                            0 -> "Off"
                            1 -> "First"
                            2 -> "All"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { witherDoorESP = it }
                .build(),
        )
    }

    private fun addDungeonMessage(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Mimic Message"), mimicMessageEnabled)
                .setDefaultValue(false)
                .setSaveConsumer { mimicMessageEnabled = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startStrField(Text.literal("Mimic Text"), mimicMessage)
                .setDefaultValue("Mimic Killed!")
                .setSaveConsumer { mimicMessage = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Score Messages"), scoreMessage, 0, 2)
                .setDefaultValue(0)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "Off"
                            1 -> "300"
                            2 -> "270 and 300"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { scoreMessage = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Score Title"), scoreTitle, 0, 2)
                .setDefaultValue(0)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "Off"
                            1 -> "300"
                            2 -> "270 and 300"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { scoreTitle = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startStrField(Text.literal("270 Message"), message270)
                .setDefaultValue("270 Score")
                .setSaveConsumer { message270 = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startStrField(Text.literal("300 Message"), message300)
                .setDefaultValue("300 Score")
                .setSaveConsumer { message300 = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("300 Time"), timeTo300)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Shows time to reach 300 score"))
                .setSaveConsumer { timeTo300 = it }
                .build(),
        )
    }

    private fun addDungeonMap(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Hide In Boss"), mapHideInBoss)
                .setDefaultValue(false)
                .setSaveConsumer { mapHideInBoss = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Player Names"), playerHeads, 0, 2)
                .setDefaultValue(0)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "Off"
                            1 -> "Holding Leap"
                            2 -> "Always"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { playerHeads = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Name Scale"), playerNameScale)
                .setDefaultValue(0.8f)
                .setMin(0.1f)
                .setMax(2.0f)
                .setSaveConsumer { playerNameScale = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Head Scale"), playerHeadScale)
                .setDefaultValue(1.0f)
                .setMin(0.1f)
                .setMax(2.0f)
                .setSaveConsumer { playerHeadScale = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Room Names"), mapRoomNames, 0, 2)
                .setDefaultValue(2)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "None"
                            1 -> "Puzzles/Trap"
                            2 -> "All"
                            else -> "All"
                        },
                    )
                }.setSaveConsumer { mapRoomNames = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Vanilla Marker"), mapVanillaMarker)
                .setDefaultValue(false)
                .setSaveConsumer { mapVanillaMarker = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Text Scale"), textScale)
                .setDefaultValue(0.75f)
                .setMin(0.1f)
                .setMax(2.0f)
                .setSaveConsumer { textScale = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Map Scale"), mapScale)
                .setDefaultValue(1.25f)
                .setMin(0.1f)
                .setMax(4.0f)
                .setSaveConsumer { mapScale = it }
                .build(),
        )
    }

    private fun addDungeonMapRender(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startAlphaColorField(Text.literal("Background Color"), mapBackground.rgb)
                .setDefaultValue(Color(0, 0, 0, 100).rgb)
                .setSaveConsumer { mapBackground = it.fromArgb() }
                .build(),
        )
    }

    private fun addDungeonP5(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Dragon Helper"), dragonHelper)
                .setDefaultValue(false)
                .setSaveConsumer { dragonHelper = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Split Manager"), splitManager)
                .setDefaultValue(false)
                .setSaveConsumer { splitManager = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntField(Text.literal("Easy Split Power"), easySplitPower)
                .setDefaultValue(0)
                .setSaveConsumer { easySplitPower = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Dragon Box"), dragonBox)
                .setDefaultValue(false)
                .setSaveConsumer { dragonBox = it }
                .build(),
        )
    }

    private fun addDungeonBlessing(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Blessing Display"), blessingDisplay)
                .setDefaultValue(false)
                .setSaveConsumer { blessingDisplay = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Display Power"), displayPower)
                .setDefaultValue(true)
                .setSaveConsumer { displayPower = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Display Time"), displayTime)
                .setDefaultValue(true)
                .setSaveConsumer { displayTime = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Display Stone"), displayStone)
                .setDefaultValue(false)
                .setSaveConsumer { displayStone = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Display Life"), displayLife)
                .setDefaultValue(false)
                .setSaveConsumer { displayLife = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Display Wisdom"), displayWisdom)
                .setDefaultValue(false)
                .setSaveConsumer { displayWisdom = it }
                .build(),
        )
    }

    private fun addDungeonColors(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        // Room colors
        category.addEntry(
            entryBuilder
                .startColorField(Text.literal("Blood Room"), colorBlood.rgb)
                .setDefaultValue(Color(255, 0, 0).rgb)
                .setSaveConsumer { colorBlood = it.fromArgb() }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startColorField(Text.literal("Entrance Room"), colorEntrance.rgb)
                .setDefaultValue(Color(20, 133, 0).rgb)
                .setSaveConsumer { colorEntrance = it.fromArgb() }
                .build(),
        )
    }

    private fun addDungeonScore(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Score Display"), scoreTotalScore, 0, 2)
                .setDefaultValue(2)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "Off"
                            1 -> "On"
                            2 -> "Separate"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { scoreTotalScore = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Minimized Text"), scoreMinimizedName)
                .setDefaultValue(false)
                .setSaveConsumer { scoreMinimizedName = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Assume Spirit"), scoreAssumeSpirit)
                .setDefaultValue(true)
                .setSaveConsumer { scoreAssumeSpirit = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Secrets"), scoreSecrets, 0, 2)
                .setDefaultValue(1)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "Off"
                            1 -> "Total"
                            2 -> "Total and Missing"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { scoreSecrets = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Crypts"), scoreCrypts)
                .setDefaultValue(false)
                .setSaveConsumer { scoreCrypts = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Mimic"), scoreMimic)
                .setDefaultValue(false)
                .setSaveConsumer { scoreMimic = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Deaths"), scoreDeaths)
                .setDefaultValue(false)
                .setSaveConsumer { scoreDeaths = it }
                .build(),
        )
    }

    private fun addDungeonRunInfo(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Score"), runInformationScore)
                .setDefaultValue(true)
                .setSaveConsumer { runInformationScore = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Secrets"), runInformationSecrets, 0, 2)
                .setDefaultValue(1)
                .setTextGetter { v ->
                    Text.literal(
                        when (v) {
                            0 -> "Off"
                            1 -> "Total"
                            2 -> "Total and Missing"
                            else -> "Off"
                        },
                    )
                }.setSaveConsumer { runInformationSecrets = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Crypts"), runInformationCrypts)
                .setDefaultValue(true)
                .setSaveConsumer { runInformationCrypts = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Mimic"), runInformationMimic)
                .setDefaultValue(true)
                .setSaveConsumer { runInformationMimic = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Deaths"), runInformationDeaths)
                .setDefaultValue(true)
                .setSaveConsumer { runInformationDeaths = it }
                .build(),
        )
    }

    private fun addCombat(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("RagAxe Tracker"), ragAxe)
                .setDefaultValue(false)
                .setSaveConsumer { ragAxe = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("RagAxe Announcer"), ragAxeAnnouncer)
                .setDefaultValue(false)
                .setSaveConsumer { ragAxeAnnouncer = it }
                .build(),
        )
    }

    private fun addQOL(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Autofisher Enabled"), autofisher)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Automatically reels in and re-casts the fishing rod"))
                .setSaveConsumer { autofisher = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(
                    Text.literal("Ticks Between Reel & Recast"),
                    autofisherTickBetweenFish,
                    0,
                    200,
                ).setDefaultValue(20)
                .setTextGetter { v -> Text.literal("$v ticks") }
                .setTooltip(Text.literal("Ticks to wait after reeling in (收杆) before casting the rod again (放杆)"))
                .setSaveConsumer { autofisherTickBetweenFish = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startIntSlider(Text.literal("Bite Reel-In Delay"), autofisherPullBackTick, 0, 100)
                .setDefaultValue(20)
                .setTextGetter { v -> Text.literal("$v ticks") }
                .setTooltip(Text.literal("Ticks to wait after a bite is detected before reeling in"))
                .setSaveConsumer { autofisherPullBackTick = it }
                .build(),
        )
    }

    private fun addOtherFeatures(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startStrField(Text.literal("API Key"), apiKey)
                .setDefaultValue("")
                .setSaveConsumer { apiKey = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Team Info"), teamInfo)
                .setDefaultValue(false)
                .setSaveConsumer { teamInfo = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Door Outline Width"), witherDoorOutlineWidth)
                .setDefaultValue(3.0f)
                .setMin(1.0f)
                .setMax(10.0f)
                .setSaveConsumer { witherDoorOutlineWidth = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Door Outline Opacity"), witherDoorOutline)
                .setDefaultValue(1.0f)
                .setMin(0.0f)
                .setMax(1.0f)
                .setSaveConsumer { witherDoorOutline = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startFloatField(Text.literal("Door Fill Opacity"), witherDoorFill)
                .setDefaultValue(0.25f)
                .setMin(0.0f)
                .setMax(1.0f)
                .setSaveConsumer { witherDoorFill = it }
                .build(),
        )
    }

    private fun addDebug(
        entryBuilder: ConfigEntryBuilder,
        category: ConfigCategory,
    ) {
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Developer Mode"), developerMode)
                .setDefaultValue(false)
                .setSaveConsumer { developerMode = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Force Skyblock"), forceSkyblock)
                .setDefaultValue(false)
                .setSaveConsumer { forceSkyblock = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Paul Bonus"), paulBonus)
                .setDefaultValue(false)
                .setSaveConsumer { paulBonus = it }
                .build(),
        )
        category.addEntry(
            entryBuilder
                .startBooleanToggle(Text.literal("Beta Rendering"), renderBeta)
                .setDefaultValue(false)
                .setSaveConsumer { renderBeta = it }
                .build(),
        )
    }
}
