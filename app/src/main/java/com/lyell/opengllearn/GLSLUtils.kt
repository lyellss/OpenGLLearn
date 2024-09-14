package com.lyell.opengllearn

import android.content.Context
import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object GLSLUtils {

    fun Context.readStringFromRaw(@RawRes resId: Int): String {
        return runCatching {
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(resources.openRawResource(resId)))
            var nextLine: String? = reader.readLine()
            while (nextLine != null) {
                builder.append(nextLine).append("\n")
                nextLine = reader.readLine()
            }
            reader.close()
            builder.toString()
        }.onFailure {
            when (it) {
                is IOException -> {
                    throw RuntimeException("Could not open resource: $resId", it)
                }

                is Resources.NotFoundException -> {
                    throw RuntimeException("Resource not found: $resId", it)
                }

                else -> {}
            }
        }.getOrThrow()
    }
}