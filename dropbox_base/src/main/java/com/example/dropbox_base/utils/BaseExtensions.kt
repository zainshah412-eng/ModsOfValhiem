package com.example.dropbox_base.utils

import android.content.Context

fun Context.getFileNameWithExtension(filePath: String): String {
    return filePath.substringAfterLast("/")
}