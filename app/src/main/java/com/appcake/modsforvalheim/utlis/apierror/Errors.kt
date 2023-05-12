package com.appcake.modsforvalheim.utlis.apierror

import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("resource")
    var resource: String,
    @SerializedName("field")
    var field: String,
    @SerializedName("code")
    var code: String
)
