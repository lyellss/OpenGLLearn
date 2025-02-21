package com.lyell.opengllearn.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.lyell.opengllearn.view.render.LearnGLRender

class LearnGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : GLSurfaceView(context, attributeSet) {

    init {
        setEGLContextClientVersion(2)
        setRenderer(LearnGLRender(context))

        renderMode = RENDERMODE_WHEN_DIRTY
    }

}