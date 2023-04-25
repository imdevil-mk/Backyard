package windowsApi

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Structure

open class POINT(@JvmField var x: Int = 0, @JvmField var y: Int = 0) : Structure() {
    override fun getFieldOrder(): MutableList<String>? = mutableListOf("x", "y")
}

class LPPOINT : POINT(), Structure.ByReference

interface WinUser32 : Library {
    fun GetCursorPos(lpPoint: LPPOINT): Boolean
}

val LIBRARY: WinUser32 = Native.load("user32", WinUser32::class.java)