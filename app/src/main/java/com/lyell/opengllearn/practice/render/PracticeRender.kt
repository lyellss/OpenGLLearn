package com.lyell.opengllearn.practice.render

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_LINES
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.GL_TRIANGLES
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glLineWidth
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import com.lyell.opengllearn.R
import com.lyell.opengllearn.utils.GLSLUtils
import com.lyell.opengllearn.utils.ShaderUtils
import com.lyell.opengllearn.utils.VertexBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class PracticeRender(private val context: Context) : GLSurfaceView.Renderer {

    companion object {
        const val POSITION_COMPONENT_COUNT = 2
    }

    private val vertexPoints: FloatArray = floatArrayOf(
        // triangle1
        0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // triangle2
        0.5f, -0.5f,
        -0.5f, 0.5f,
        -0.5f, -0.5f,

        // 中间冰球点
        0.0f, 0.0f,

        // 锤球点
        0.0f, 0.25f,
        0.0f, -0.25f,

        // 分割线点
        -0.5f, 0.0f,
        0.5f, 0.0f,

        )

    private val vertexBuffer = VertexBuffer.createFloat(vertexPoints)

    private var aPosition: Int = 0

    private var uColor: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0f)

        val vertexShaderSrc = GLSLUtils.readStringFromRaw(context, R.raw.practice_vertex_shader)
        val fragmentShaderSrc = GLSLUtils.readStringFromRaw(context, R.raw.practice_fragment_shader)
        val programId = ShaderUtils.createProgram(vertexShaderSrc, fragmentShaderSrc)
        glUseProgram(programId)
        aPosition = glGetAttribLocation(programId, "a_Position")
        uColor = glGetUniformLocation(programId, "u_Color")
        glVertexAttribPointer(
            aPosition,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            0,
            vertexBuffer
        )
        glEnableVertexAttribArray(aPosition)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        glUniform4f(uColor, 1.0f, 1.0f, 1.0f, 1.0f)
        glDrawArrays(GL_TRIANGLES, 0, 6)

        glUniform4f(uColor, 0.0f, 0.0f, 0.0f, 0.0f)
        glDrawArrays(GL_POINTS, 6, 1)

        glUniform4f(uColor, 1.0f, 0.0f, 0.0f, 0.0f)
        glDrawArrays(GL_POINTS, 7, 1)

        glUniform4f(uColor, 0.0f, 1.0f, 0.0f, 0.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        glLineWidth(5.0f)
        glUniform4f(uColor, 0.0f, 0.0f, 1.0f, 0.0f)
        glDrawArrays(GL_LINES, 9, 2)
    }
}