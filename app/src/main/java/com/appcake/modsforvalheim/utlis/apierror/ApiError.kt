package com.appcake.modsforvalheim.utlis.apierror

import com.google.gson.annotations.SerializedName
import org.json.JSONArray

data class ApiError(
    @SerializedName("message")
    var message : String,
    @SerializedName("errors")
    var errors: JSONArray?= null,
    @SerializedName("documentation_url")
    var documentation_url: String
)
