package mageaddons.utils

import com.google.gson.JsonParser
import mageaddons.MageAddons
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion

object UpdateChecker {
    fun hasUpdate(): Int {
        val response = APIUtils.fetch("")
        val version = JsonParser().parse(response).toJsonArray()
            ?.get(0)?.toJsonObject()
            ?.getJsonPrimitive("tag_name")?.asString ?: return 0
        val current = DefaultArtifactVersion(MageAddons.MOD_VERSION.replace("pre", "beta"))
        val latest = DefaultArtifactVersion(version.replace("pre", "beta"))
        return latest.compareTo(current)
    }
}
