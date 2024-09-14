package com.lyell.opengllearn.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class OpenGLView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(ViewRender())
    }
}