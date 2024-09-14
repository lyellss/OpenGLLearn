package com.lyell.opengllearn.view

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import com.lyell.opengllearn.GLSLUtils.readStringFromRaw
import com.lyell.opengllearn.MyApplication
import com.lyell.opengllearn.R
import com.lyell.opengllearn.ShaderHelper
import com.otaliastudios.opengl.types.ByteBuffer
import com.otaliastudios.opengl.types.FloatBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ViewRender : GLSurfaceView.Renderer {
    companion object {
        const val TAG = "ViewRender"

        const val POSITION_COMPONENT_COUNT = 2

        const val BYTES_PER_FLOAT = 4
    }


    private val tableVertices = floatArrayOf(
        // 三角形1
        0f, 0f,
        9f, 14f,
        0f, 14f,

        // 三角形2
        0f, 0f,
        9f, 0f,
        9f, 14f,

        // 中线
        -0.5f, 0f,
        0.5f, 0f,

        // 顶点
        0f, -0.25f,
        0f, 0.25f,

//        -0.5F, -0.5F,
//        0.5F, 0.5F,
//        -0.5F, 0.5F,
//
//        -0.5F, -0.5F,
//        0.5F, -0.5F,
//        0.5F, 0.5F,
//        // 中线
//        -0.5F, 0F,
//        0.5F, 0F,
//        // 顶点
//        0F, -0.25F,
//        0F, 0.25F,

        )


    private val vertexData: FloatBuffer =
        ByteBuffer.allocateDirect(tableVertices.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(tableVertices)

    private var uColorLocation: Int = 0
    private var aPosition: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        Log.d(TAG, "onSurfaceCreated: ")
//        GLES20.glClearColor(1F, 0F, 0F, 1F)

        // 读取着色器代码
        val vertexShaderCode = MyApplication.instance.readStringFromRaw(R.raw.simple_vertex_shader)
        val fragmentShaderCode =
            MyApplication.instance.readStringFromRaw(R.raw.simple_fragment_shader)

        // 创建着色器 id
        val vertexShaderId = ShaderHelper.compileVertexShader(vertexShaderCode)
        val fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShaderCode)

        // 链接程序
        val programId = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId)

        // 检查节目有效
        val validateResult = ShaderHelper.validateProgram(programId)
        Log.d(TAG, "onSurfaceCreated: validate result = $validateResult")

        // 绘制表面
        GLES20.glUseProgram(programId)

        uColorLocation = GLES20.glGetUniformLocation(programId, "u_Color")
        aPosition = GLES20.glGetAttribLocation(programId, "a_Position")

        // 从0位置开始读取数据
        vertexData.position(0)
        GLES20.glVertexAttribPointer(
            aPosition,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            0,
            vertexData
        )

        GLES20.glEnableVertexAttribArray(aPosition)


    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        Log.d(TAG, "onSurfaceChanged: ")
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
//        Log.d(TAG, "onDrawFrame: ")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

        GLES20.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        GLES20.glUniform4f(uColorLocation, 0F, 0F, 1F, 1F)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
    }

}