package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class CommandsResponse(
    @field:SerializedName("glx__list") val jsonCommandsglxList: List<CommandsglxList?>? = null
)

data class CommandsglxList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("glx_d4") val glx_d4: String? = null,
    @field:SerializedName("glx_i1") val glx_i1: String? = null
)