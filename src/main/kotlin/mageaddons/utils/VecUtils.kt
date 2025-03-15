package mageaddons.utils

import mageaddons.utils.RenderUtils.outlineBounds
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3

/**
 * Clones a Vec3 object.
 */
fun Vec3.clone(): Vec3 = Vec3(this.xCoord, this.yCoord, this.zCoord)

/**
 * @param add Will determine the maximum bounds
 */
fun Vec3.toAABB(add: Double = 1.0): AxisAlignedBB {
    return AxisAlignedBB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + add, this.yCoord + add, this.zCoord + add).outlineBounds()
}
