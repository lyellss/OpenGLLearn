package com.lyell.opengllearn.practice

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.lyell.opengllearn.practice.render.PracticeRender

class PracticeGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : GLSurfaceView(context, attributeSet) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(PracticeRender(context))
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}