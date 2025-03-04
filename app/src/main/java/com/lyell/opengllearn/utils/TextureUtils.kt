package com.lyell.opengllearn.utils

import android.graphics.Bitmap
import android.opengl.GLES20.GL_LINEAR
import android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR
import android.opengl.GLES20.GL_TEXTURE_2D
import android.opengl.GLES20.GL_TEXTURE_MAG_FILTER
import android.opengl.GLES20.GL_TEXTURE_MIN_FILTER
import android.opengl.GLES20.glBindTexture
import android.opengl.GLES20.glGenTextures
import android.opengl.GLES20.glGenerateMipmap
import android.opengl.GLES20.glTexParameteri
import android.opengl.GLUtils
import com.lyell.opengllearn.component.logger

object TextureUtils {

    fun loadTexture(bitmap: Bitmap): Int {
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
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)

        // GL_TEXTURE_MAG_FILTER 放大的情况过滤参数
        // GL_LINEAR 双线性过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        // 加载位图进入 OpenGL
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

        // 生成纹理
        glGenerateMipmap(GL_TEXTURE_2D)

        // 解除纹理绑定，原来传入 id ，现在传入0表示没有
        glBindTexture(GL_TEXTURE_2D, 0)
        return id
    }
}