import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object BloodCamp {
    @SubscribeEvent
    fun scanBloodRoom(){
        var hasCamped = false
        var mobNum = 0
    }

    @SubscribeEvent
    fun onTick(event:ClientTickEvent){
    }
}
