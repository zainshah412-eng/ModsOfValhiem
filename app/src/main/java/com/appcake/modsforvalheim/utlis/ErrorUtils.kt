package com.appcake.modsforvalheim.utlis

import com.appcake.modsforvalheim.utlis.apierror.ApiError
import org.json.JSONObject

class ErrorUtils {
    companion object {
        fun parseError(mjson: String?): ApiError? {
            return try {
                val json = JSONObject(mjson)
                ApiError(
                    json.optString("message", ""),
                    json.optJSONArray("errors"),
                    json.optString("errors", "")
                )
            } catch (ex: Exception) {
                ApiError("", null, "401")
            }
        }

        fun parseError(t: Throwable): ApiError? {
            return try {
                t.message?.let { ApiError(it, null, t.localizedMessage) }
            } catch (ex: Exception) {
                ex.printStackTrace()
                t.message?.let { ApiError(it, null, t.localizedMessage) }
            }
        }
    }
}