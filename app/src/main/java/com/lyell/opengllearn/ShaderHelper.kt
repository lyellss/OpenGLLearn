package com.lyell.opengllearn

import android.opengl.GLES20
import android.util.Log
import com.lyell.opengllearn.view.ViewRender

object ShaderHelper {

    private const val TAG = "ShaderHelper"

    fun compileVertexShader(code: String) = compileShader(GLES20.GL_VERTEX_SHADER, code)

    fun compileFragmentShader(code: String) = compileShader(GLES20.GL_FRAGMENT_SHADER, code)

    fun compileShader(type: Int, code: String): Int {
        val shaderId = GLES20.glCreateShader(type)
        if (shaderId == 0) {
            Log.d(TAG, "compileShader: can`t create shader ")
            return 0
        }

        // 上传着色器编译代码
        GLES20.glShaderSource(shaderId, code)

        // 编译着色器代码
        GLES20.glCompileShader(shaderId)

        // 检查编译状态
        val compileStatus = IntArray(1)
        // offset 0 ,表示写入到数组的索引位置
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        val compileLog = GLES20.glGetShaderInfoLog(shaderId)
        Log.d(ViewRender.TAG, "onSurfaceCreated: compile log=$compileLog")
        if (compileStatus[0] == 0) {
            Log.d(ViewRender.TAG, "onSurfaceCreated: shader failed")
            GLES20.glDeleteShader(shaderId)
            return 0
        }

        return shaderId
    }

    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programId = GLES20.glCreateProgram()
        if (programId == 0) {
            Log.d(TAG, "linkProgram: can`t create program")
            return 0
        }
        GLES20.glAttachShader(programId, vertexShaderId)
        GLES20.glAttachShader(programId, fragmentShaderId)
        GLES20.glLinkProgram(programId)
        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        val linkLog = GLES20.glGetProgramInfoLog(programId)
        Log.d(TAG, "linkProgram: link log = $linkLog")
        if (linkStatus[0] == 0) {
            Log.d(TAG, "linkProgram: link program failed")
            return 0
        }
        return programId
    }

    fun validateProgram(programId: Int): Boolean {
        GLES20.glValidateProgram(programId)
        val validateStatus = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)
        if (validateStatus[0] == 0) {
            Log.d(TAG, "validateProgram: validate program failed")
            return false
        }
        return true
    }
}