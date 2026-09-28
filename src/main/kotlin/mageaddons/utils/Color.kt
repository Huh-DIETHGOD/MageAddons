package mageaddons.utils

// Custom Color class to replace java.awt.Color (not available on all platforms with LWJGL 3)
data class Color(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255) {
    constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, 255)

    val rgb: Int
        get() = (alpha shl 24) or (red shl 16) or (green shl 8) or blue

    fun withAlpha(newAlpha: Float): Color =
        Color(red, green, blue, (newAlpha * 255).toInt().coerceIn(0, 255))

    fun darker(factor: Float): Color = Color(
        (red * (1 - factor)).toInt().coerceIn(0, 255),
        (green * (1 - factor)).toInt().coerceIn(0, 255),
        (blue * (1 - factor)).toInt().coerceIn(0, 255),
        alpha
    )

    companion object {
        val WHITE = Color(255, 255, 255)
        val BLACK = Color(0, 0, 0)
        val RED = Color(255, 0, 0)
        val GREEN = Color(0, 255, 0)
        val BLUE = Color(0, 0, 255)
        val YELLOW = Color(255, 255, 0)
        val TRANSPARENT = Color(0, 0, 0, 0)
    }
}
