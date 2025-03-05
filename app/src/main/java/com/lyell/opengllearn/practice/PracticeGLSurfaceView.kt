package com.lyell.opengllearn.practice

import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Handler
import android.util.AttributeSet
import com.lyell.opengllearn.practice.render.ImageRender

class PracticeGLSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : GLSurfaceView(context, attributeSet), Runnable {

    private lateinit var handler: Handler

    private val viewRender: ImageRender by lazy {
        ImageRender(context)
    }

    init {
        setEGLContextClientVersion(2)
        setRenderer(viewRender)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewRender.release()
    }

    fun setHandler(handler: Handler) {
        this.handler = handler
        handler.post(this)
    }

    override fun run() {
        requestRender()
        handler.postDelayed(this, 5)
    }
}