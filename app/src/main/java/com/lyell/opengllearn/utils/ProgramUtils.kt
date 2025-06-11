package com.lyell.opengllearn.utils

import android.opengl.GLES20.GL_LINK_STATUS
import android.opengl.GLES20.glAttachShader
import android.opengl.GLES20.glCreateProgram
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glDeleteShader
import android.opengl.GLES20.glGetProgramInfoLog
import android.opengl.GLES20.glGetProgramiv
import android.opengl.GLES20.glLinkProgram
import com.lyell.opengllearn.utils.ShaderUtils.compileFragmentShader
import com.lyell.opengllearn.utils.ShaderUtils.compileVertexShader

object ProgramUtils {

    // 创建着色器程序
    fun createProgram(vertexSource: String, fragmentSource: String): Int {
        // 加载顶点着色器和片段着色器
        val vertexShader = compileVertexShader(vertexSource)
        val fragmentShader = compileFragmentShader(fragmentSource)

        // 创建程序
        val programId = glCreateProgram()
        // 附加着色器
        glAttachShader(programId, vertexShader)
        glAttachShader(programId, fragmentShader)
        // 链接程序
        glLinkProgram(programId)

        // 检查链接状态
        val linked = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, linked, 0)
        if (linked[0] == 0) {
            val error = glGetProgramInfoLog(programId)
            glDeleteProgram(programId)
            throw RuntimeException("Program link error: $error")
        }

        // 删除着色器，它们已经链接到程序中
        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)

        return programId
    }
}