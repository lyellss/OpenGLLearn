package com.lyell.opengllearn.practice

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.lyell.opengllearn.practice.render.Render4
import com.lyell.opengllearn.practice.render.Render5

class PracticeGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : GLSurfaceView(context, attributeSet) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(Render5(context))
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}