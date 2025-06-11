package com.lyell.opengllearn.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

object VertexBufferUtils {

    const val FLOAT_TYPE: Int = 4

    const val DOUBLE_TYPE: Int = 8

    const val SHORT_TYPE: Int = 2

    const val INT_TYPE: Int = 4

    const val LONG_TYPE: Int = 8

    fun createFloat(floatArray: FloatArray): FloatBuffer {
        return ByteBuffer.allocateDirect(FLOAT_TYPE * floatArray.size)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().apply {
                put(floatArray)
                position(0)
            }
    }

    fun createShort(shortArray: ShortArray): ShortBuffer {
        return ByteBuffer.allocateDirect(SHORT_TYPE * shortArray.size)
            .order(ByteOrder.nativeOrder()).asShortBuffer().apply {
                put(shortArray)
                position(0)
            }
    }
}