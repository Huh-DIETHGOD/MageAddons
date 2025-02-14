package mageaddons.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.*
import mageaddons.MageAddons
import mageaddons.ui.EditLocationGui
import java.awt.Color
import java.io.File

object Config : Vigilant(File("./config/mageaddons/config.toml"), "Mage Addons", sortingBehavior = CategorySorting) {
// Dungeon General
    @Property(
        name = "Map Enabled",
        type = PropertyType.SWITCH,
        description = "Render the map",
        category = "Dungeon",
        subcategory = "General",
    )
    var mapEnabled = true

    @Property(
        name = "Auto Scan",
        type = PropertyType.SWITCH,
        description = "Automatically scans when entering dungeon. Manual scan can be done with /ma scan.",
        category = "Dungeon",
        subcategory = "General",
    )
    var autoScan = true

    @Property(
        name = "Chat Info",
        type = PropertyType.SWITCH,
        description = "Show dungeon overview information after scanning.",
        category = "Dungeon",
        subcategory = "General",
    )
    var scanChatInfo = true

    @Property(
        name = "Show Run Information",
        type = PropertyType.SWITCH,
        description = "Shows run information under map.",
        category = "Dungeon",
        subcategory = "General",
    )
    var mapShowRunInformation = true

    @Property(
        name = "Show Score",
        type = PropertyType.SWITCH,
        description = "Shows separate score element.",
        category = "Dungeon",
        subcategory = "General",
    )
    var scoreElementEnabled = false

    @Property(
        name = "Show Personal Best",
        type = PropertyType.SWITCH,
        description = "Shows personal best",
        category = "Dungeon",
        subcategory = "General",
    )
    var personalBestEnabled = true

    @Property(
        name = "Wither Door ESP",
        description = "Boxes unopened wither doors.",
        type = PropertyType.SELECTOR,
        category = "Dungeon",
        subcategory = "General",
        options = ["Off", "First", "All"],
    )
    var witherDoorESP = 0

    @Property(
        name = "Map Position",
        type = PropertyType.BUTTON,
        category = "Dungeon",
        subcategory = "General",
        placeholder = "Edit",
    )
    fun openMoveMapGui() {
        MageAddons.display = EditLocationGui()
    }

    @Property(
        name = "Reset Map Position",
        type = PropertyType.BUTTON,
        category = "Dungeon",
        subcategory = "General",
        placeholder = "Reset",
    )
    fun resetMapLocation() {
        mapX = 10
        mapY = 10
    }

// Dungeon Message
    @Property(
        name = "Mimic Message",
        type = PropertyType.SWITCH,
        description = "Sends party message when a mimic is killed. Detects most instant kills.",
        category = "Dungeon",
        subcategory = "Message",
    )
    var mimicMessageEnabled = false

    @Property(
        name = "Mimic Message Text",
        type = PropertyType.TEXT,
        category = "Dungeon",
        subcategory = "Message",
    )
    var mimicMessage = "Mimic Killed!"

    @Property(
        name = "Score Messages",
        type = PropertyType.SELECTOR,
        category = "Dungeon",
        subcategory = "Message",
        options = ["Off", "300", "270 and 300"],
    )
    var scoreMessage = 0

    @Property(
        name = "Score Title",
        type = PropertyType.SELECTOR,
        description = "Shows score messages as a title notification.",
        category = "Dungeon",
        subcategory = "Message",
        options = ["Off", "300", "270 and 300"],
    )
    var scoreTitle = 0

    @Property(
        name = "270 Message",
        type = PropertyType.TEXT,
        category = "Dungeon",
        subcategory = "Message",
    )
    var message270 = "270 Score"

    @Property(
        name = "300 Message",
        type = PropertyType.TEXT,
        category = "Dungeon",
        subcategory = "Message",
    )
    var message300 = "300 Score"

    @Property(
        name = "300 Time",
        type = PropertyType.SWITCH,
        description = "Shows time to reach 300 score.",
        category = "Dungeon",
        subcategory = "Message",
    )
    var timeTo300 = false

    // Dungeon Map
    @Property(
        name = "Hide In Boss",
        type = PropertyType.SWITCH,
        description = "Hides the map in boss.",
        category = "Dungeon",
        subcategory = "Map",
    )
    var mapHideInBoss = false

    @Property(
        name = "Show Player Names",
        type = PropertyType.SELECTOR,
        description = "Show player name under player head",
        category = "Dungeon",
        subcategory = "Map",
        options = ["Off", "Holding Leap", "Always"],
    )
    var playerHeads = 0

    @Property(
        name = "Hide in Boss",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Map",
    )
    var scoreHideInBoss = false

    @Property(
        name = "Player Name Scale",
        type = PropertyType.DECIMAL_SLIDER,
        description = "Scale of player names relative to head size.",
        category = "Dungeon",
        subcategory = "Map",
        maxF = 2f,
        decimalPlaces = 2,
    )
    var playerNameScale = .8f

    @Property(
        name = "Player Heads Scale",
        type = PropertyType.DECIMAL_SLIDER,
        description = "Scale of player heads relative to map size.",
        category = "Dungeon",
        subcategory = "Map",
        maxF = 2f,
        decimalPlaces = 2,
    )
    var playerHeadScale = 1f

    @Property(
        name = "Room Names",
        type = PropertyType.SELECTOR,
        description = "Shows names of rooms on map.",
        category = "Dungeon",
        subcategory = "Map",
        options = ["None", "Puzzles / Trap", "All"],
    )
    var mapRoomNames = 2

    @Property(
        name = "Vanilla Head Marker",
        type = PropertyType.SWITCH,
        description = "Uses the vanilla head marker for yourself.",
        category = "Dungeon",
        subcategory = "Map",
    )
    var mapVanillaMarker = false

    @Property(
        name = "Map X",
        type = PropertyType.NUMBER,
        category = "Dungeon",
        subcategory = "Map",
        hidden = true,
    )
    var mapX = 10

    @Property(
        name = "Map Y",
        type = PropertyType.NUMBER,
        category = "Dungeon",
        subcategory = "Map",
        hidden = true,
    )
    var mapY = 10

    @Property(
        name = "Map Size",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Dungeon",
        subcategory = "Map",
        minF = 0.1f,
        maxF = 4f,
        decimalPlaces = 2,
        hidden = true,
    )
    var mapScale = 1.25f

    @Property(
        name = "Map Text Scale",
        type = PropertyType.DECIMAL_SLIDER,
        description = "Scale of room names and secret counts relative to map size.",
        category = "Dungeon",
        subcategory = "Map",
        maxF = 2f,
        decimalPlaces = 2,
    )
    var textScale = 0.75f

// Dungeon Map Render
    @Property(
        name = "Map Background Color",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Map Render",
        allowAlpha = true,
    )
    var mapBackground = Color(0, 0, 0, 100)

    @Property(
        name = "Map Border Color",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Map Render",
        allowAlpha = true,
    )
    var mapBorder = Color(0, 0, 0, 255)

    @Property(
        name = "Border Thickness",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Dungeon",
        subcategory = "Map Render",
        maxF = 10f,
    )
    var mapBorderWidth = 3f

    @Property(
        name = "Dark Undiscovered Rooms",
        type = PropertyType.SWITCH,
        description = "Darkens unentered rooms.",
        category = "Dungeon",
        subcategory = "Map Render",
    )
    var mapDarkenUndiscovered = true

    @Property(
        name = "Darken Multiplier",
        type = PropertyType.PERCENT_SLIDER,
        description = "How much to darken undiscovered rooms.",
        category = "Dungeon",
        subcategory = "Map Render",
    )
    var mapDarkenPercent = 0.4f

    @Property(
        name = "Gray Undiscovered Rooms",
        type = PropertyType.SWITCH,
        description = "Grayscale unentered rooms.",
        category = "Dungeon",
        subcategory = "Map Render",
    )
    var mapGrayUndiscovered = false

    @Property(
        name = "Room Secrets",
        type = PropertyType.SELECTOR,
        description = "Shows total secrets of rooms on map.",
        category = "Dungeon",
        subcategory = "Map Render",
        options = ["Off", "On", "Replace Checkmark"],
    )
    var mapRoomSecrets = 0

    @Property(
        name = "Center Room Names",
        type = PropertyType.SWITCH,
        description = "Center room names.",
        category = "Dungeon",
        subcategory = "Map Render",
    )
    var mapCenterRoomName = true

    @Property(
        name = "Color Text",
        type = PropertyType.SWITCH,
        description = "Colors name and secret count based on room state.",
        category = "Dungeon",
        subcategory = "Map Render",
    )
    var mapColorText = true

    @Property(
        name = "Room Checkmarks",
        type = PropertyType.SELECTOR,
        description = "Adds room checkmarks based on room state.",
        category = "Dungeon",
        subcategory = "Map Render",
        options = ["None", "Default", "NEU", "Legacy"],
    )
    var mapCheckmark = 1

    @Property(
        name = "Center Room Checkmarks",
        type = PropertyType.SWITCH,
        description = "Center room checkmarks.",
        category = "Dungeon",
        subcategory = "Map Render",
    )
    var mapCenterCheckmark = true

// Dungeon P5
    @Property(
        name = "Dragon Helper",
        type = PropertyType.SWITCH,
        description = "Help P5 fight",
        category = "Dungeon",
        subcategory = "P5"
    )
    var dragonHelper = false

    @Property(
        name = "Split Manager",
        type = PropertyType.SWITCH,
        description = "Manage Split",
        category = "Dungeon",
        subcategory = "P5"
    )
    var splitManager = false

    @Property(
        name = "Esat Split Power",
        type = PropertyType.NUMBER,
        description = "Easy Split Power",
        category = "Dungeon",
        subcategory = "P5"
    )
    var easySplitPower = 0

    @Property(
        name = "Dragon Box",
        description = "Shows dragons Boxes",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "P5"
    )
    var dragonBox = false

// Dungeon Color
    @Property(
        name = "Blood Door",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorBloodDoor = Color(231, 0, 0)

    @Property(
        name = "Entrance Door",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorEntranceDoor = Color(20, 133, 0)

    @Property(
        name = "Normal Door",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorRoomDoor = Color(92, 52, 14)

    @Property(
        name = "Wither Door",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorWitherDoor = Color(0, 0, 0)

    @Property(
        name = "Opened Wither Door",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorOpenWitherDoor = Color(92, 52, 14)

    @Property(
        name = "Unopened Door",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorUnopenedDoor = Color(65, 65, 65)

    @Property(
        name = "Blood Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorBlood = Color(255, 0, 0)

    @Property(
        name = "Entrance Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorEntrance = Color(20, 133, 0)

    @Property(
        name = "Fairy Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorFairy = Color(224, 0, 255)

    @Property(
        name = "Miniboss Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorMiniboss = Color(254, 223, 0)

    @Property(
        name = "Normal Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorRoom = Color(107, 58, 17)

    @Property(
        name = "Mimic Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorRoomMimic = Color(186, 66, 52)

    @Property(
        name = "Puzzle Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorPuzzle = Color(117, 0, 133)

    @Property(
        name = "Rare Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorRare = Color(255, 203, 89)

    @Property(
        name = "Trap Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorTrap = Color(216, 127, 51)

    @Property(
        name = "Unopened Room",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorUnopened = Color(65, 65, 65)

    @Property(
        name = "Cleared Room Text",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorTextCleared = Color(255, 255, 255)

    @Property(
        name = "Uncleared Room Text",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorTextUncleared = Color(170, 170, 170)

    @Property(
        name = "Green Room Text",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorTextGreen = Color(85, 255, 85)

    @Property(
        name = "Failed Room Text",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var colorTextFailed = Color(255, 255, 255)

    @Property(
        name = "No Key Color",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var witherDoorNoKeyColor = Color(255, 0, 0)

    @Property(
        name = "Has Key Color",
        type = PropertyType.COLOR,
        category = "Dungeon",
        subcategory = "Color",
        allowAlpha = true,
    )
    var witherDoorKeyColor = Color(0, 255, 0)

// Dungeon Score
    @Property(
        name = "Score Calc X",
        type = PropertyType.NUMBER,
        category = "Dungeon",
        subcategory = "Score",
        hidden = true,
    )
    var scoreX = 10

    @Property(
        name = "Score Calc Y",
        type = PropertyType.NUMBER,
        category = "Dungeon",
        subcategory = "Score",
        hidden = true,
    )
    var scoreY = 10

    @Property(
        name = "Score Calc Size",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Dungeon",
        subcategory = "Score",
        minF = 0.1f,
        maxF = 4f,
        decimalPlaces = 2,
        hidden = true,
    )
    var scoreScale = 1f

    @Property(
        name = "Score",
        type = PropertyType.SELECTOR,
        category = "Dungeon",
        subcategory = "Score",
        options = ["Off", "On", "Separate"],
    )
    var scoreTotalScore = 2

    @Property(
        name = "Minimized Text",
        description = "Shortens description for score elements.",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Score",
    )
    var scoreMinimizedName = false

    @Property(
        name = "Assume Spirit",
        type = PropertyType.SWITCH,
        description = "Assume everyone has a legendary spirit pet.",
        category = "Dungeon",
        subcategory = "Score",
    )
    var scoreAssumeSpirit = true

    @Property(
        name = "Secrets",
        type = PropertyType.SELECTOR,
        category = "Dungeon",
        subcategory = "Score",
        options = ["Off", "Total", "Total and Missing"],
    )
    var scoreSecrets = 1

    @Property(
        name = "Crypts",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Score",
    )
    var scoreCrypts = false

    @Property(
        name = "Mimic",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Score",
    )
    var scoreMimic = false

    @Property(
        name = "Deaths",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Score",
    )
    var scoreDeaths = false

    @Property(
        name = "Puzzles",
        type = PropertyType.SELECTOR,
        category = "Dungeon",
        subcategory = "Score",
        options = ["Off", "Total", "Completed and Total"],
    )
    var scorePuzzles = 0

// Dungeon Run Information
    @Property(
        name = "Score",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Run Information",
    )
    var runInformationScore = true

    @Property(
        name = "Secrets",
        type = PropertyType.SELECTOR,
        category = "Dungeon",
        subcategory = "Run Information",
        options = ["Off", "Total", "Total and Missing"],
    )
    var runInformationSecrets = 1

    @Property(
        name = "Crypts",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Run Information",
    )
    var runInformationCrypts = true

    @Property(
        name = "Mimic",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Run Information",
    )
    var runInformationMimic = true

    @Property(
        name = "Deaths",
        type = PropertyType.SWITCH,
        category = "Dungeon",
        subcategory = "Run Information",
    )
    var runInformationDeaths = true

// QOL Hot key
    @Property(
        name = "Enable Equipment Hot Key",
        description = "/equipment command hot key",
        type = PropertyType.SWITCH,
        category = "QOL",
        subcategory = "Hot Key",
    )
    var equipmentHotKeyEnabled = false

    @Property(
        name = "Equipment Hot Key",
        description = "/equipment command hot key",
        type = PropertyType.TEXT,
        category = "QOL",
        subcategory = "Hot Key",
        hidden = true
    )
    var equipmentHotKey = "R"

    @Property(
        name = "Enable Wardrobe Hot Key",
        description = "/wardrobe command hot key",
        type = PropertyType.SWITCH,
        category = "QOL",
        subcategory = "Hot Key",
    )
    var wardrobeHotKeyEnabled = false

    @Property(
        name = "Wardrobe Hot Key",
        description = "/wardrobe command hot key",
        type = PropertyType.TEXT,
        category = "QOL",
        subcategory = "Hot Key",
        hidden = true
    )
    var wardrobeHotKey = "X"

// Other Features
    @Property(
        name = "Door Outline Width",
        type = PropertyType.DECIMAL_SLIDER,
        category = "Other Features",
        subcategory = "Wither Door",
        minF = 1f,
        maxF = 10f,
    )
    var witherDoorOutlineWidth = 3f

    @Property(
        name = "Hypixel API Key",
        type = PropertyType.TEXT,
        category = "Other Features",
        protectedText = true,
    )
    var apiKey = ""

    @Property(
        name = "Show Team Info",
        type = PropertyType.SWITCH,
        description = "Shows team member secrets and room times at end of run. Requires a valid API key.",
        category = "Other Features",
    )
    var teamInfo = false

    @Property(
        name = "Door Outline Opacity",
        type = PropertyType.PERCENT_SLIDER,
        category = "Other Features",
        subcategory = "Wither Door",
    )
    var witherDoorOutline = 1f

    @Property(
        name = "Door Fill Opacity",
        type = PropertyType.PERCENT_SLIDER,
        category = "Other Features",
        subcategory = "Wither Door",
    )
    var witherDoorFill = 0.25f

// Debug
    @Property(
        name = "Developer Mode",
        type = PropertyType.SWITCH,
        description = "Enables Dev Mode",
        category = "Debug",
    )
    var developerMode = false

    @Property(
        name = "Enable Test Command",
        type = PropertyType.SWITCH,
        description = "Enables test command",
        category = "Debug",
    )
    var testCommandEnabled = false

    @Property(
        name = "Force Skyblock",
        type = PropertyType.SWITCH,
        description = "Disables in skyblock and dungeon checks. Don't enable unless you know what you're doing.",
        category = "Debug",
    )
    var forceSkyblock = false

    @Property(
        name = "Paul Score",
        type = PropertyType.SWITCH,
        description = "Assumes paul perk is active to give 10 bonus score.",
        category = "Debug",
    )
    var paulBonus = false

    @Property(
        name = "Beta Rendering",
        type = PropertyType.SWITCH,
        category = "Debug",
    )
    var renderBeta = false

// Combat RagAxe
    @Property(
        name = "RagAxe Tracker",
        description = "Tracks rag axe cooldowns.",
        type = PropertyType.SWITCH,
        category = "Combat",
        subcategory = "RagAxe",
    )
    var ragAxe = false

    @Property(
        name = "RagAxe Announcer",
        description = "Announce strength gained from rag axe.",
        type = PropertyType.SWITCH,
        category = "Combat",
        subcategory = "RagAxe",
    )
    var ragAxeAnnouncer = false

    init {
        initialize()
        setCategoryDescription(
            "General",
            "&f&l Mage Addons\n&7Big thanks to &Funny Map&r&7 by Harry282",
        )
    }

    private object CategorySorting : SortingBehavior() {
        private val configCategories =
            listOf(
                "General",
                "Dungeon",
                "Combat",
                "QOL",
                "Statistics",
                "Other Features",
                "Debug",
            )

        private val configSubcategories =
            listOf(
                "General",
                "Hot Key",
                "Message",
                "Map",
                "Map Render",
                "Color",
                "Score",
                "Run Information",
            )

        override fun getCategoryComparator(): Comparator<in Category> = compareBy { configCategories.indexOf(it.name) }

        override fun getSubcategoryComparator(): Comparator<in Map.Entry<String, List<PropertyData>>> =
            compareBy { configSubcategories.indexOf(it.key) }
    }
}
