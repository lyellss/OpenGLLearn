package com.lyell.opengllearn.practice.render

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLES20.GL_FLOAT
import android.opengl.GLES20.GL_TEXTURE0
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
import com.lyell.opengllearn.utils.TextureUtils
import com.lyell.opengllearn.utils.VertexBufferUtils
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

    private val textureBuffer = VertexBufferUtils.createFloat(textureVertexData)

    private val squareBuffer = VertexBufferUtils.createFloat(squareVertexData)

    private val drawOrderBuffer = VertexBufferUtils.createShort(drawOrder)

    private var textureId1: Int = 0
    private var textureId2: Int = 0
    private var tempTextureId: Int = 0

    private var programId: Int = 0

    private var aPosition: Int = 0

    private var uMatrix: Int = 0

    private var matrix: FloatArray = FloatArray(16)

    private var aTextureCoordinate: Int = 0

    /**
     * 移动步长
     */
    private var step: Float = 1f

    private var translateX: Float = 0f
    private val translateY = 0.0f
    private val translateSpeed = 0.02f

    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        logger.d("onSurfaceCreated: ")
        glClearColor(1f, 1f, 1f, 1f)

        val vertexSrc = GLSLUtils.readStringFromRaw(context, R.raw.image_vertex_shader)
        val fragmentSrc = GLSLUtils.readStringFromRaw(context, R.raw.image_fragment_shader)
        programId = ShaderUtils.createProgram(vertexSrc, fragmentSrc)
        glUseProgram(programId)

        aPosition = glGetAttribLocation(programId, "a_Position")
        aTextureCoordinate = glGetAttribLocation(programId, "a_TextureCoordinate")
        uMatrix = glGetUniformLocation(programId, "u_Matrix")

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

        var bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image1)
        textureId1 = TextureUtils.createTexture(bitmap)
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.image2)
        textureId2 = TextureUtils.createTexture(bitmap)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        logger.d("onSurfaceChanged: ")
        viewWidth = width
        viewHeight = height
        glViewport(0, 0, viewWidth, viewHeight)
    }

    override fun onDrawFrame(gl: GL10?) {
        logger.d("onDrawFrame: ")
        glClear(GL_COLOR_BUFFER_BIT)

        // 绘制第一张图
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, translateX, 0f, 0f)
        glUniformMatrix4fv(uMatrix, 1, false, matrix, 0)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId1)
        glDrawElements(GL_TRIANGLE_STRIP, drawOrder.size, GL_UNSIGNED_SHORT, drawOrderBuffer)

        // 绘制第二张图
        Matrix.setIdentityM(matrix, 0)
        Matrix.translateM(matrix, 0, translateX - 2f, 0f, 0f)
        glUniformMatrix4fv(uMatrix, 1, false, matrix, 0)

        glBindTexture(GL_TEXTURE_2D, textureId2)
        glDrawElements(GL_TRIANGLE_STRIP, drawOrder.size, GL_UNSIGNED_SHORT, drawOrderBuffer)

        // 更新位移
        translateX += translateSpeed
        if (translateX > 2f) {
            translateX -= 2f
            // 交换纹理
            tempTextureId = textureId1
            textureId1 = textureId2
            textureId2 = tempTextureId
        }
    }

    fun release() {
        glDisableVertexAttribArray(aPosition)
        glDisableVertexAttribArray(aTextureCoordinate)
        // 解除纹理绑定，原来传入 id ，现在传入0表示没有
        glBindTexture(GL_TEXTURE_2D, 0)
        glDeleteTextures(2, intArrayOf(textureId1, textureId2), 0)
        glDeleteProgram(programId)
    }

}