package mageaddons.features.QOL

import mageaddons.config.Config
import org.lwjgl.input.Keyboard

object HotKeys {
    fun transformKey(type:String): Int {
        if (type.length == 0) return Keyboard.KEY_NONE
        var key = type[0]
        return when (key) {
            '1' -> Keyboard.KEY_1
            '2' -> Keyboard.KEY_2
            '3' -> Keyboard.KEY_3
            '4' -> Keyboard.KEY_4
            '5' -> Keyboard.KEY_5
            '6' -> Keyboard.KEY_6
            '7' -> Keyboard.KEY_7
            '8' -> Keyboard.KEY_8
            '9' -> Keyboard.KEY_9
            '0' -> Keyboard.KEY_0
            'A' -> Keyboard.KEY_A
            'B' -> Keyboard.KEY_B
            'C' -> Keyboard.KEY_C
            'D' -> Keyboard.KEY_D
            'E' -> Keyboard.KEY_E
            'F' -> Keyboard.KEY_F
            'G' -> Keyboard.KEY_G
            'H' -> Keyboard.KEY_H
            'I' -> Keyboard.KEY_I
            'J' -> Keyboard.KEY_J
            'K' -> Keyboard.KEY_K
            'L' -> Keyboard.KEY_L
            'M' -> Keyboard.KEY_M
            'N' -> Keyboard.KEY_N
            'O' -> Keyboard.KEY_O
            'P' -> Keyboard.KEY_P
            'Q' -> Keyboard.KEY_Q
            'R' -> Keyboard.KEY_R
            'S' -> Keyboard.KEY_S
            'T' -> Keyboard.KEY_T
            'U' -> Keyboard.KEY_U
            'V' -> Keyboard.KEY_V
            'W' -> Keyboard.KEY_W
            'X' -> Keyboard.KEY_X
            'Y' -> Keyboard.KEY_Y
            'Z' -> Keyboard.KEY_Z
            else -> Keyboard.KEY_NONE
        }
    }
}