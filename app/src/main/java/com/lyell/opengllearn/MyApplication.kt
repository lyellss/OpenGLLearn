package com.lyell.opengllearn

import android.app.Application

open class MyApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}