package mageaddons.features.combat

import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.Location
import mageaddons.utils.Utils.sendClientMessage

object RagAxe : ModuleFactory("RagAxe Tracker") {
    private var lastUseTime = 0L
    private var activeUsers = mutableMapOf<String, Long>()
    private var userStrength = mutableMapOf<String, Int>()

    private const val COOLDOWN_MS = 10_000L // 10 second cooldown
    private val ragAxeRegex = Regex("§r§..+ §r§eused §r§6Ragnarok Axe§r§e! (?:Gained )?§r§c(\\d+) §r§7strength")

    override fun onEnable() {
        activeUsers.clear()
        userStrength.clear()
    }

    override fun onTick() {
        if (!Location.inDungeons) return
    }

    fun onChatMessage(message: String) {
        if (!Config.ragAxe) return

        ragAxeRegex.find(message)?.let { match ->
            val strength = match.groupValues[1].toIntOrNull() ?: return
            val playerName = match.value
                .substringBefore(" used ")
                .replace(Regex("§[0-9a-fk-or]"), "")
                .trim()

            activeUsers[playerName] = System.currentTimeMillis()
            userStrength[playerName] = strength

            if (Config.ragAxeAnnouncer && strength > 0) {
                sendClientMessage("§6[BA] §f${playerName} gained §c${strength} ❁ Strength")
            }

            // Schedule cooldown end notification
            lastUseTime = System.currentTimeMillis()
        }
    }

    fun isOnCooldown(playerName: String): Boolean {
        val lastUse = activeUsers[playerName] ?: return false
        return System.currentTimeMillis() - lastUse < COOLDOWN_MS
    }

    fun getRemainingCooldown(playerName: String): Long {
        val lastUse = activeUsers[playerName] ?: return 0
        val remaining = COOLDOWN_MS - (System.currentTimeMillis() - lastUse)
        return remaining.coerceAtLeast(0)
    }

    fun getPlayerStrength(playerName: String): Int {
        return userStrength[playerName] ?: 0
    }
}
