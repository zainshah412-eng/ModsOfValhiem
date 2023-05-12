package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class BuildsResponse(
    @field:SerializedName("xhts_list") val xhts_list: List<xhts_list?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)


data class xhts_list(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("xhtsd4") val name: String? = null,
    @field:SerializedName("xhtsf2") val imagePath: String? = null
)