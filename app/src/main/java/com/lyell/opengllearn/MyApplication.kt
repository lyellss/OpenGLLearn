package com.lyell.opengllearn

import android.app.Application
import com.lyell.opengllearn.component.GLLogger

open class MyApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        GLLogger.debugInit()
    }
}