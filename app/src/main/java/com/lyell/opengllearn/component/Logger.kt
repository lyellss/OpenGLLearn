package com.lyell.opengllearn.component

import timber.log.Timber

val logger = GLLogger.logger

class GLLogger private constructor() {
    companion object {

        fun debugInit() {
            logger.plant(Timber.DebugTree())
        }

        fun init(tree: Timber.Tree) {
            logger.plant(tree)
        }

        internal val logger = Timber
    }
}