package mageaddons.utils.impl

enum class DungeonClass(val displayName: String) {
    EMPTY(""),
    HEALER("Healer"),
    MAGE("Mage"),
    BERSERK("Berserk"),
    ARCHER("Archer"),
    TANK("Tank");

    companion object {
        fun fromName(name: String): DungeonClass = entries.find {
            it.displayName.equals(name, ignoreCase = true)
        } ?: EMPTY
    }
}
