package com.lyell.opengllearn.view.render

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
import android.opengl.GLES20.glUniform4f
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import com.lyell.opengllearn.R
import com.lyell.opengllearn.component.logger
import com.lyell.opengllearn.utils.GLSLUtils
import com.lyell.opengllearn.utils.ShaderUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class LearnGLRender(
    private val context: Context,
) : GLSurfaceView.Renderer {

    companion object {
        const val POSITION_COMPONENT_COUNT = 2

        /**
         * float 精度32位，一个字节8位精度，一个浮点数需要4个字节
         */
        const val BYTES_PER_FLOAT = 4
    }

    // 习惯用逆时针定义绘制顺序
    private val tableVerticesWithTriangles = floatArrayOf(
        // triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // 线段点
        -0.5f, 0f,
        0.5f, 0f,

        // 锤点
        0f, -0.25f,
        0f, 0.25f,
    )

    private var uColorLocation: Int = 0

    private var aPositionLocation: Int = 0

    private val vertexData: FloatBuffer =
        ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

    init {
        vertexData.put(tableVerticesWithTriangles)
    }

    /**
     * 可能多次调用
     */
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        val vertexShaderSource = GLSLUtils.readStringFromRaw(context, R.raw.simple_vertex_shader)
        val fragmentShaderSource =
            GLSLUtils.readStringFromRaw(context, R.raw.simple_fragment_shader)
        val programId = ShaderUtils.createProgram(vertexShaderSource, fragmentShaderSource)
        glUseProgram(programId)
        uColorLocation = glGetUniformLocation(programId, "u_Color")
        logger.d("onSurfaceCreated: uColorLocation=$uColorLocation")
        aPositionLocation = glGetAttribLocation(programId, "a_Position")
        logger.d("onSurfaceCreated: aPositionLocation=$aPositionLocation")

        // 将数据缓冲区的指正移动到0
        vertexData.position(0)
        glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            0,
            vertexData
        )
        glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        logger.d("width=$width; height=$height")
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 调用该代码后，清空屏幕，并用 glClearColor 的颜色填充屏幕
        glClear(GL_COLOR_BUFFER_BIT)

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
        glDrawArrays(GL_TRIANGLES, 0, 6)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_LINES, 6, 2)

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        glDrawArrays(GL_POINTS, 8, 1)

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        glDrawArrays(GL_POINTS, 9, 1)
    }
}