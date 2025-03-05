package com.lyell.opengllearn

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lyell.opengllearn.practice.PracticeGLSurfaceView

class MainActivity : AppCompatActivity() {

    private val mainLayout by lazy {
        findViewById<LinearLayout>(R.id.main)
    }


    private val view1: PracticeGLSurfaceView by lazy {
        findViewById(R.id.view1)
    }

    private val view2: PracticeGLSurfaceView by lazy {
        findViewById(R.id.view2)
    }

    private val view3: PracticeGLSurfaceView by lazy {
        findViewById(R.id.view3)
    }

    private val handlerThread = HandlerThread("PracticeGLSurfaceView").apply {
        start()
    }

    private val handler: Handler = Handler(handlerThread.looper)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        val view = PracticeGLSurfaceView(this)
//        addViewToMain(view)
//        val view1 = PracticeGLSurfaceView(this)
//        addViewToMain(view1)
        view1.handler = handler
        view2.handler = handler
        view3.handler = handler
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun addViewToMain(view: View) {
        mainLayout.addView(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}