package mageaddons.utils

import mageaddons.MageAddons.mc
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.Constants


/**
 * Returns the primary Strength value for an Item
 */
val strengthRegex = Regex("Strength: \\+(\\d+)")
val ItemStack?.getSBStrength: Int
    get() {
        return this?.lore?.firstOrNull { it.noControlCodes.startsWith("Strength:") }
            ?.let { loreLine -> strengthRegex.find(loreLine.noControlCodes)?.groups?.get(1)?.value?.toIntOrNull() } ?: 0
    }

/**
 * Returns the ExtraAttribute Compound
 */
val ItemStack?.extraAttributes: NBTTagCompound?
    get() = this?.getSubCompound("ExtraAttributes", false)

fun ItemStack.displayName(): String =
    this.tagCompound?.getCompoundTag("display")?.takeIf { it.hasKey("Name", 8) }?.getString("Name") ?: this.item.getItemStackDisplayName(this)

/**
 * Returns displayName without control codes.
 */
val ItemStack?.unformattedName: String
    get() = this?.displayName()?.noControlCodes ?: ""

/**
 * Returns the lore for an Item
 */
val ItemStack?.lore: List<String>
    get() = this?.tagCompound?.getCompoundTag("display")?.getTagList("Lore", 8)?.let {
        List(it.tagCount()) { i -> it.getStringTagAt(i) }
    }.orEmpty()

/**
 * Returns Item ID for an Item
 */
val ItemStack?.skyblockID: String
    get() = this?.extraAttributes?.getString("id") ?: ""

/**
 * Returns uuid for an Item
 */
val ItemStack?.uuid: String
    get() = this?.extraAttributes?.getString("uuid") ?: ""

/**
 * Returns if an item has an ability
 */
val ItemStack?.hasAbility: Boolean
    get() = this?.lore?.any { it.contains("Ability:") && it.endsWith("RIGHT CLICK") } == true

/**
 * Returns if an item is a shortbow
 */
val ItemStack?.isShortbow: Boolean
    get() =this?.lore?.any { it.contains("Shortbow: Instantly shoots!") } == true

/**
 * Returns if an item is a fishing rod
 */
val ItemStack?.isFishingRod: Boolean
    get() = this?.lore?.any { it.contains("FISHING ROD") } == true

/**
 * Returns if an item is Spirit leaps or an Infinileap
 */
val ItemStack?.isLeap: Boolean
    get() = this?.skyblockID?.equalsOneOf("INFINITE_SPIRIT_LEAP", "SPIRIT_LEAP") == true

val EntityPlayerSP.usingEtherWarp: Boolean
    get() {
        val item = heldItem ?: return false
        if (item.skyblockID == "ETHERWARP_CONDUIT") return true
        return isSneaking && item.extraAttributes?.getBoolean("ethermerge") == true
    }

fun getSkullValue(entity: Entity?): String? = entity?.inventory?.get(4)?.skullTexture

/**
 * Returns the ID of held item
 */
fun isHolding(vararg id: String): Boolean =
    mc.thePlayer?.heldItem?.skyblockID in id

/**
 * Returns first slot of an Item
 */
fun getItemSlot(item: String, ignoreCase: Boolean = true): Int? =
    mc.thePlayer?.inventory?.mainInventory?.indexOfFirst { it?.unformattedName?.contains(item, ignoreCase) == true }.takeIf { it != -1 }

/**
 * Gets index of an item in a chest.
 * @return null if not found.
 */
fun getItemIndexInContainerChest(container: ContainerChest, item: String, subList: IntRange = 0..container.inventory.size - 36): Int? {
    return container.inventorySlots.subList(subList.first, subList.last + 1).firstOrNull {
        it.stack?.unformattedName?.noControlCodes?.lowercase() == item.noControlCodes.lowercase()
    }?.slotIndex
}

fun getItemIndexInContainerChest(container: ContainerChest, item: String, subList: IntRange = 0..container.inventory.size - 36, ignoreCase: Boolean = false): Int? {
    return container.inventorySlots.subList(subList.first, subList.last + 1).firstOrNull {
        it.stack?.unformattedName?.contains(item, ignoreCase) == true
    }?.slotIndex
}

/**
 * Gets index of an item in a chest using its uuid.
 * @return null if not found.
 */
fun getItemIndexInContainerChestByUUID(container: ContainerChest, uuid: String, subList: IntRange = 0..container.inventory.size - 36, ignoreCase: Boolean = false): Int? {
    return container.inventorySlots.subList(subList.first, subList.last + 1).firstOrNull {
        it.stack?.uuid?.contains(uuid) == true
    }?.slotIndex
}

/**
 * Gets index of an item in a chest using its lore.
 * @return null if not found.
 */
fun getItemIndexInContainerChestByLore(container: ContainerChest, lore: String, subList: IntRange = 0..container.inventory.size - 36, ignoreCase: Boolean = false): Int? {
    return container.inventorySlots.subList(subList.first, subList.last + 1).firstOrNull {
        it.stack?.lore?.contains(lore) == true
    }?.slotIndex
}

val ItemStack.skullTexture: String? get() {
    return this.tagCompound
        ?.getCompoundTag("SkullOwner")
        ?.getCompoundTag("Properties")
        ?.getTagList("textures", Constants.NBT.TAG_COMPOUND)
        ?.getCompoundTagAt(0)
        ?.getString("Value")
}
