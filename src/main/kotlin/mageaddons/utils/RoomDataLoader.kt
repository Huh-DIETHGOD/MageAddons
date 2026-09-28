package mageaddons.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mageaddons.MageAddons.logger
import mageaddons.core.RoomData
import mageaddons.core.map.Puzzle
import mageaddons.core.map.RoomType

object RoomDataLoader {
    private val gson = Gson()
    private var roomList: List<RoomData> = emptyList()

    fun loadRoomData(jsonString: String) {
        try {
            val type = object : TypeToken<List<RoomDataJson>>() {}.type
            val jsonList: List<RoomDataJson> = gson.fromJson(jsonString, type)
            roomList = jsonList.map { json ->
                RoomData(
                    name = json.name,
                    type = json.type?.let { RoomType.valueOf(it.uppercase()) } ?: RoomType.NORMAL,
                    core = json.core ?: 0,
                    secrets = json.secrets ?: 0,
                    puzzle = json.puzzle?.let { Puzzle.fromRoomName(it) }
                )
            }
            logger.info("Loaded ${roomList.size} room data entries")
        } catch (e: Exception) {
            logger.error("Failed to load room data: ${e.message}")
            roomList = emptyList()
        }
    }

    fun findRoomData(core: Int): RoomData? = roomList.find { it.core == core }

    fun findRoomData(name: String): RoomData? = roomList.find {
        it.name.equals(name, ignoreCase = true)
    }

    data class RoomDataJson(
        val name: String,
        val type: String?,
        val core: Int?,
        val secrets: Int?,
        val puzzle: String?,
        val crypts: List<Pair<Int, Int>>? = null
    )
}
