package mageaddons.features.QOL

import mageaddons.MageAddons
import mageaddons.MageAddons.mc
import mageaddons.config.Config
import mageaddons.core.ModuleFactory
import mageaddons.utils.Utils
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import org.lwjgl.glfw.GLFW

object Autofisher : ModuleFactory("Autofisher") {
    /** Squared radius around the bobber in which a splash sound counts as a bite. */
    private const val SPLASH_RADIUS_SQ = 16.0

    /** Ticks to wait without a bobber before re-casting. */
    private const val RECAST_TIMEOUT_TICKS = 40

    private enum class State {
        IDLE,
        WAITING_FOR_BITE,
        PULL_BACK_DELAY,
        RECAST_DELAY,
    }

    private var state = State.IDLE
    private var tickCounter = 0
    private var noBobberTicks = 0
    private var biteDetected = false
    private var warnedNoRod = false

    var fishCaught = 0
        private set

    init {
        // Keybind can be bound in Controls -> Mage Addons (no default key to avoid conflicts)
        registerKeyBinding("autofisher", GLFW.GLFW_KEY_UNKNOWN)
    }

    override fun onEnable() {
        Config.autofisher = true
        fishCaught = 0
        warnedNoRod = false
        if (AutofisherUtils.getBobber() != null) {
            // A rod is already cast - simply wait for the first bite
            state = State.WAITING_FOR_BITE
            noBobberTicks = 0
        } else {
            state = State.RECAST_DELAY
            tickCounter = 20
        }
        Utils.sendClientMessage("${MageAddons.CHAT_PREFIX}§aAutofisher enabled.")
    }

    override fun onDisable() {
        Config.autofisher = false
        reset()
        Utils.sendClientMessage("${MageAddons.CHAT_PREFIX}§cAutofisher disabled.")
    }

    /** Syncs the module toggle with the config value (called when the config screen is saved). */
    fun syncFromConfig() {
        if (Config.autofisher == enabled) return
        enabled = Config.autofisher
        if (enabled) onEnable() else onDisable()
    }

    private fun reset() {
        state = State.IDLE
        tickCounter = 0
        noBobberTicks = 0
        biteDetected = false
    }

    override fun onTick() {
        if (!Config.autofisher) return
        if (mc.player == null) {
            reset()
            return
        }
        // Pause the state machine while any GUI is open so the simulated
        // mouse input is not queued behind the screen.
        if (mc.currentScreen != null) return

        when (state) {
            State.IDLE -> {}

            State.WAITING_FOR_BITE -> {
                tickWaitingForBite()
            }

            State.PULL_BACK_DELAY -> {
                if (--tickCounter <= 0) reelIn()
            }

            State.RECAST_DELAY -> {
                if (--tickCounter <= 0) {
                    if (AutofisherUtils.isHoldingRod()) {
                        castRod()
                    } else {
                        tickCounter = 20
                        if (!warnedNoRod) {
                            warnedNoRod = true
                            Utils.sendClientMessage("${MageAddons.CHAT_PREFIX}§eAutofisher waiting: hold a fishing rod!")
                        }
                    }
                }
            }
        }
    }

    private fun tickWaitingForBite() {
        val bobber = AutofisherUtils.getBobber()
        if (bobber == null) {
            // Bobber gone (manual reel-in or failed cast) - retry after a short timeout
            if (++noBobberTicks >= RECAST_TIMEOUT_TICKS) {
                state = State.RECAST_DELAY
                tickCounter = 5
                noBobberTicks = 0
            }
            return
        }
        noBobberTicks = 0

        // Fallback detection: the bobber is yanked downward when something bites
        val velocityBite = bobber.age > 10 && bobber.velocity.y < -0.05

        if (biteDetected || velocityBite) {
            biteDetected = false
            state = State.PULL_BACK_DELAY
            tickCounter = Config.autofisherPullBackTick
        }
    }

    /**
     * Called from MixinClientPlayNetworkHandler when a sound packet arrives.
     * Primary bite detection: the fishing bobber splash sound.
     */
    fun onSoundPacket(packet: PlaySoundS2CPacket) {
        if (!enabled || state != State.WAITING_FOR_BITE) return
        if (!AutofisherUtils.isBobberSplash(packet)) return
        val bobber = AutofisherUtils.getBobber() ?: return
        val dx = packet.x - bobber.x
        val dy = packet.y - bobber.y
        val dz = packet.z - bobber.z
        if (dx * dx + dy * dy + dz * dz <= SPLASH_RADIUS_SQ) biteDetected = true
    }

    /** 放杆 - cast the rod via simulated right click. */
    private fun castRod() {
        AutofisherUtils.simulateUseClick()
        state = State.WAITING_FOR_BITE
        noBobberTicks = 0
        biteDetected = false
    }

    /** 收杆 - reel in via simulated right click, then wait the configured interval. */
    private fun reelIn() {
        AutofisherUtils.simulateUseClick()
        fishCaught++
        state = State.RECAST_DELAY
        // Custom tick interval between reeling in (收杆) and casting again (放杆)
        tickCounter = Config.autofisherTickBetweenFish
    }
}
