package com.lyell.opengllearn.practice.render

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TRIANGLE_STRIP
import android.opengl.GLES20.GL_UNSIGNED_SHORT
import android.opengl.GLES20.glActiveTexture
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glClear
import android.opengl.GLES20.glClearColor
import android.opengl.GLES20.glDeleteProgram
import android.opengl.GLES20.glDeleteTextures
import android.opengl.GLES20.glDisableVertexAttribArray
import android.opengl.GLES20.glDrawElements
import android.opengl.GLES20.glEnableVertexAttribArray
import android.opengl.GLES20.glGetAttribLocation
import android.opengl.GLES20.glUseProgram
import android.opengl.GLES20.glVertexAttribPointer
import android.opengl.GLES20.glViewport
import android.opengl.GLSurfaceView
import com.lyell.opengllearn.R
import com.lyell.opengllearn.component.logger
import com.lyell.opengllearn.utils.GLSLUtils
import com.lyell.opengllearn.utils.ShaderUtils
import com.lyell.opengllearn.utils.TextureUtils
import com.lyell.opengllearn.utils.VertexBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ImageRender(val context: Context) : GLSurfaceView.Renderer {

    /**
     * 纹理绘制顶点
     */
    private val textureVertexData = floatArrayOf(
        0f, 0f,      // top left
        0f, 1f,      // bottom left
        1f, 1f,       // bottom right
        1f, 0f     // top right
    )

    /**
     * 绘制区域顶点
     */
    private val squareVertexData = floatArrayOf(
        -1f, 1f, 0.0f,      // top left
        -1f, -1f, 0.0f,      // bottom left
        1f, -1f, 0.0f,      // bottom right
        1f, 1f, 0.0f       // top right
    )

    // 四个顶点的绘制顺序数组
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

    private val textureBuffer = VertexBuffer.createFloat(textureVertexData)

    private val squareBuffer = VertexBuffer.createFloat(squareVertexData)

    private val drawOrderBuffer = VertexBuffer.createShort(drawOrder)

    private var textureId: Int = 0

    private var programId: Int = 0

    private var aPosition: Int = 0

    private var aTextureCoordinate: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        logger.d("onSurfaceCreated: ")
        glClearColor(0f, 0f, 0f, 1f)

        val vertexSrc = GLSLUtils.readStringFromRaw(context, R.raw.image_vertex_shader)
        val fragmentSrc = GLSLUtils.readStringFromRaw(context, R.raw.image_fragment_shader)
        programId = ShaderUtils.createProgram(vertexSrc, fragmentSrc)
        glUseProgram(programId)

        aPosition = glGetAttribLocation(programId, "a_Position")
        aTextureCoordinate = glGetAttribLocation(programId, "a_TextureCoordinate")

        glVertexAttribPointer(
            aPosition,
            3,
            GL_FLOAT,
            false,
            0,
            squareBuffer,
        )
        glEnableVertexAttribArray(aPosition)

        glVertexAttribPointer(
            aTextureCoordinate,
            2, GL_FLOAT,
            false,
            0,
            textureBuffer
        )
        glEnableVertexAttribArray(aTextureCoordinate)


        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.desk)
        textureId = TextureUtils.createTexture(bitmap)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        logger.d("onSurfaceChanged: ")
        glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        logger.d("onDrawFrame: ")
        glClear(GL_COLOR_BUFFER_BIT)
        drawTexture(textureId)
    }

    fun drawTexture(textureId: Int) {
        glActiveTexture(textureId)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glDrawElements(GL_TRIANGLE_STRIP, drawOrder.size, GL_UNSIGNED_SHORT, drawOrderBuffer)
    }

    fun release() {
        glDisableVertexAttribArray(aPosition)
        glDisableVertexAttribArray(aTextureCoordinate)
        glDeleteTextures(1, intArrayOf(textureId), 0)
        glDeleteProgram(programId)
    }

}