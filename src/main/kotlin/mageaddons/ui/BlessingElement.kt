package mageaddons.ui

import mageaddons.MageAddons.mc
import mageaddons.config.Config
import mageaddons.utils.Location.inDungeons
import mageaddons.utils.impl.Blessing
import net.minecraft.client.gui.FontRenderer

class BlessingElement : MovableGuiElement() {
    override var x: Int by Config::blessingX
    override var y: Int by Config::blessingY
    override val h: Int
        get() = if (Config.mapShowRunInformation) 142 else 128
    override val w: Int
        get() = 128
    override var scale: Float by Config::mapScale
    override var x2: Int = (x + w * scale).toInt()
    override var y2: Int = (y + h * scale).toInt()

    override fun render() {
        val lines = 5
    }

    override fun shouldRender(): Boolean {
        if (Config.forceBlessingDisplay) return true
        if (!Config.blessingDisplay || !inDungeons) return false
        return super.shouldRender()
    }

    companion object{
        val fr: FontRenderer = mc.fontRendererObj

        fun getBlessingLines(): List<String>{
            val list: MutableList<String> = mutableListOf()
            if (!Config.blessingDisplay) return emptyList()
            if (Config.displayPower) list.add(Blessing.POWER.displayString)
            if (Config.displayTime) list.add(Blessing.TIME.displayString)
            if (Config.displayWisdom) list.add(Blessing.WISDOM.displayString)
            if (Config.displayStone) list.add(Blessing.STONE.displayString)
            if (Config.displayLife) list.add(Blessing.LIFE.displayString)
            return list
        }
    }
}