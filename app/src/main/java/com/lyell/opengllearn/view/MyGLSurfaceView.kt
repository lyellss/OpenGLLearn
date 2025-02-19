package com.lyell.opengllearn.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class MyGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : GLSurfaceView(context, attributeSet) {

    init {
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        // 按需渲染
        renderMode = RENDERMODE_WHEN_DIRTY

        // 持续渲染 不断重绘
//        renderMode = RENDERMODE_CONTINUOUSLY

    }
}