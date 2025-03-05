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
import android.opengl.GLES20.glGetUniformLocation
import android.opengl.GLES20.glUniformMatrix4fv
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.lyell.opengllearn.R
import com.lyell.opengllearn.component.logger
import com.lyell.opengllearn.utils.GLSLUtils
import com.lyell.opengllearn.utils.ShaderUtils
import com.lyell.opengllearn.utils.VertexBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 第五章
 * 调整屏幕宽高比
 */
class Render5(private val context: Context) : GLSurfaceView.Renderer {

    companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = 3
        const val STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * VertexBuffer.FLOAT_TYPE
    }

    private val vertexPoints: FloatArray = floatArrayOf(
        // triangle fan, 坐标值 x,y,r,g,b
        0f, 0f, 1f, 1f, 1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

        // 线段点
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 0f, 0f, 1f,

        // 锤点
        0f, -0.25f, 0f, 0f, 1f,
        0f, 0.25f, 1f, 0f, 0f

    )

    private val projectionMatrix: FloatArray = FloatArray(16)

    private val vertexBuffer = VertexBuffer.createFloat(vertexPoints)

    private var aPosition: Int = 0

    private var aColor: Int = 0

    private var uMatrix: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0f)

        val vertexShaderSrc = GLSLUtils.readStringFromRaw(context, R.raw.render5_vertex_shader)
        val fragmentShaderSrc = GLSLUtils.readStringFromRaw(context, R.raw.render5_fragment_shader)
        val programId = ShaderUtils.createProgram(vertexShaderSrc, fragmentShaderSrc)
        glUseProgram(programId)
        aPosition = glGetAttribLocation(programId, "a_Position")
        aColor = glGetAttribLocation(programId, "a_Color")
        uMatrix = glGetUniformLocation(programId, "u_Matrix")

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
        val aspectRatio: Float = if (width > height) {
            (width / height.toFloat())
        } else {
            (height / width.toFloat())
        }
        logger.d("onSurfaceChanged: width=$width; height=$height")
        logger.d("onSurfaceChanged: aspectRatio=$aspectRatio")
        // 创建正交投影，屏幕短的一遍 设置为 -1 到 1；长的一遍按比例
        if (width > height) {
            // 横屏
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            // 竖屏
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
        // [
        // 1.0, 0.0, 0.0, 0.0,
        // 0.0, 0.5, 0.0, 0.0,
        // 0.0, 0.0, -1.0, 0.0,
        // -0.0, -0.0, -0.0, 1.0
        // ]
        logger.d("onSurfaceChanged: projectionMatrix=${projectionMatrix.toList()}")
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        glUniformMatrix4fv(uMatrix, 1, false, projectionMatrix, 0)

        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        glDrawArrays(GL_LINES, 6, 2)

        glDrawArrays(GL_POINTS, 8, 1)

        glDrawArrays(GL_POINTS, 9, 1)
    }

}