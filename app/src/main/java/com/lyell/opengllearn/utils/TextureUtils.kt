package com.lyell.opengllearn.utils

import android.graphics.Bitmap
import android.opengl.GLES20.GL_CLAMP_TO_EDGE
import android.opengl.GLES20.GL_LINEAR
import android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES20.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES20.GL_TEXTURE_WRAP_S
import android.opengl.GLES20.GL_TEXTURE_WRAP_T
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGenTextures
import android.opengl.GLES20.glGenerateMipmap
import android.opengl.GLES20.glTexParameteri
import android.opengl.GLUtils
import com.lyell.opengllearn.component.logger

object TextureUtils {

    /**
     * 创建纹理
     *
     * @param bitmap 纹理图片
     * @param minFilter 最小过滤类型
     * @param magFilter 最大过滤类型
     * @param wrapS S轴的环绕方式
     * @param wrapT T轴的环绕方式
     * @return textureId
     */
    fun createTexture(
        bitmap: Bitmap,
        minFilter: Int = GL_LINEAR_MIPMAP_LINEAR,
        magFilter: Int = GL_LINEAR,
        wrapS: Int = GL_CLAMP_TO_EDGE,
        wrapT: Int = GL_CLAMP_TO_EDGE,
    ): Int {
        val textureId = IntArray(1)
        glGenTextures(1, textureId, 0)
        val id = textureId[0]
        if (id == 0) {
            logger.d("Could not generate a new OpenGL texture id")
            return 0
        }
        glBindTexture(GL_TEXTURE_2D, id)

        // 设置默认过滤参数
        // GL_TEXTURE_MIN_FILTER 缩小的情况过滤参数
        // GL_LINEAR_MIPMAP_LINEAR 三线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter)

        // GL_TEXTURE_MAG_FILTER 放大的情况过滤参数
        // GL_LINEAR 双线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT)

        // 加载位图进入 OpenGL
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

        // 生成纹理
        glGenerateMipmap(GL_TEXTURE_2D)

        // 解除纹理绑定，原来传入 id ，现在传入0表示没有
        glBindTexture(GL_TEXTURE_2D, 0)

        bitmap.recycle()
        return id
    }
}