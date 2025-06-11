package com.lyell.opengllearn.practice.render

import android.content.Context
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_LINES
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDrawArrays
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import com.lyell.opengllearn.R
import com.lyell.opengllearn.utils.GLSLUtils
import com.lyell.opengllearn.utils.ShaderUtils
import com.lyell.opengllearn.utils.VertexBufferUtils
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 第四章
 * 给每个顶点增加颜色
 */
class Render4(private val context: Context) : GLSurfaceView.Renderer {

    companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = 3
        const val STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * VertexBufferUtils.FLOAT_TYPE
    }

    private val vertexPoints: FloatArray = floatArrayOf(
        // triangle fan, 坐标值 x,y,r,g,b
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

        // 线段点
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 0f, 0f, 1f,

        // 锤点
        0f, -0.25f, 0f, 0f, 1f,
        0f, 0.25f, 1f, 0f, 0f

    )

    private val vertexBuffer = VertexBufferUtils.createFloat(vertexPoints)

    private var aPosition: Int = 0

    private var aColor: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0f)

        val vertexShaderSrc = GLSLUtils.readStringFromRaw(context, R.raw.render4_vertex_shader)
        val fragmentShaderSrc = GLSLUtils.readStringFromRaw(context, R.raw.render4_fragment_shader)
        val programId = ShaderUtils.createProgram(vertexShaderSrc, fragmentShaderSrc)
        glUseProgram(programId)
        aPosition = glGetAttribLocation(programId, "a_Position")
        aColor = glGetAttribLocation(programId, "a_Color")

        // stride 跨距 以字节为单位
        glVertexAttribPointer(
            aPosition,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        glEnableVertexAttribArray(aPosition)

        vertexBuffer.position(POSITION_COMPONENT_COUNT)
        glVertexAttribPointer(
            aColor,
            COLOR_COMPONENT_COUNT, GL_FLOAT,
            false,
            STRIDE,
            vertexBuffer
        )
        glEnableVertexAttribArray(aColor)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        glDrawArrays(GL_LINES, 6, 2)

        glDrawArrays(GL_POINTS, 8, 1)

        glDrawArrays(GL_POINTS, 9, 1)
    }

}