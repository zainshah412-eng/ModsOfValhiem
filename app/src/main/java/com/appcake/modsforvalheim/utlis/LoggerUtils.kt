package com.appcake.modsforvalheim.utlis

import android.annotation.SuppressLint
import android.util.Log

@SuppressLint("LogNotTimber")
object LoggerUtils {

    fun debug(tag: String?, message: String) {
        if (AppConstants.SHOW_CONSOLE_LOGS) {
            Log.d(tag, message)
        }
    }

    fun error(tag: String?, message: String) {
        error(tag, message, null)
    }

    fun error(tag: String?, message: String, th: Throwable?) {
        if (AppConstants.SHOW_CONSOLE_LOGS) {
            Log.e(tag, message, th)
        }
    }

    fun verbose(tag: String?, message: String) {
        if (AppConstants.SHOW_CONSOLE_LOGS) {
            Log.v(tag, message)
        }
    }
    fun info(tag: String?, message: String) {
        if (AppConstants.SHOW_CONSOLE_LOGS) {
            Log.i(tag, message)
        }
    }

}