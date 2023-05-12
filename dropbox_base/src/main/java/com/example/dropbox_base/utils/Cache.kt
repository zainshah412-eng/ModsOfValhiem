package com.example.dropbox_base.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class Cache(private val fContext: Context, var cacheDirName:String) {
    fun saveBitmpInCache(bitmap: Bitmap, fileName: String): String {
        var fos: OutputStream? = null
        val filePath = File(
            fContext.cacheDir, cacheDirName + File.separator + fileName
        )

        val imagesDir = File(
            fContext.cacheDir, cacheDirName
        )
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        val image = File(imagesDir, fileName)
        fos = FileOutputStream(image)

        fos.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return filePath.absolutePath
    }

    fun isImageAvailableInCache(fileName: String): Boolean {
        val filePath = File(
            fContext.cacheDir, cacheDirName + File.separator + fileName
        )

        return filePath.exists()
    }

    fun getCacheImagePath(fileName: String): String {
        val filePath = File(
            fContext.cacheDir, cacheDirName + File.separator + fileName
        )

        return filePath.absolutePath
    }
}