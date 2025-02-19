package com.lyell.opengllearn.utils

import android.opengl.GLES30

object ShaderUtils {
    // 加载并编译着色器
    fun loadShader(type: Int, shaderCode: String): Int {
        // 创建着色器
        val shader = GLES30.glCreateShader(type)
        // 加载着色器源码
        GLES30.glShaderSource(shader, shaderCode)
        // 编译着色器
        GLES30.glCompileShader(shader)
        // 检查编译状态
        val compiled = IntArray(1)
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            val error = GLES30.glGetShaderInfoLog(shader)
            GLES30.glDeleteShader(shader)
            throw RuntimeException("Shader compile error: $error")
        }
        return shader
    }

    // 创建着色器程序
    fun createProgram(vertexSource: String, fragmentSource: String): Int {
        // 加载顶点着色器和片段着色器
        val vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource)
        val fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource)

        // 创建程序
        val program = GLES30.glCreateProgram()
        // 附加着色器
        GLES30.glAttachShader(program, vertexShader)
        GLES30.glAttachShader(program, fragmentShader)
        // 链接程序
        GLES30.glLinkProgram(program)

        // 检查链接状态
        val linked = IntArray(1)
        GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linked, 0)
        if (linked[0] == 0) {
            val error = GLES30.glGetProgramInfoLog(program)
            GLES30.glDeleteProgram(program)
            throw RuntimeException("Program link error: $error")
        }

        // 删除着色器，它们已经链接到程序中
        GLES30.glDeleteShader(vertexShader)
        GLES30.glDeleteShader(fragmentShader)

        return program
    }
}