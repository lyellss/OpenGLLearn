package com.lyell.opengllearn.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object VertexBuffer {

    fun createFloat(vertexLength: Int, byteSize: Int): FloatBuffer {
        return ByteBuffer.allocateDirect(vertexLength * byteSize)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
    }
}