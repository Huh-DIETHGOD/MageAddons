package mageaddons.core

import mageaddons.features.dungeon.BloodCamp
import mageaddons.features.combat.RagAxe
import mageaddons.features.QOL.Autofisher

object ModuleManager {
    val modules: MutableList<ModuleFactory> = mutableListOf()

    fun init() {
        // Register all toggleable feature modules
        modules.add(BloodCamp)
        modules.add(RagAxe)
        modules.add(Autofisher)
    }

    fun onTick() {
        modules.forEach { if (it.enabled) it.onTick() }
    }
}
