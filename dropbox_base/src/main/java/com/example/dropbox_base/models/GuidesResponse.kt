package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class GuidesResponse(
    @field:SerializedName("p178_list") val p178_list: List<p178_list?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)


data class p178_list(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("p178d4") val name: String? = null,
    @field:SerializedName("p178i1") val description: String? = null,
    @field:SerializedName("p178f2") val imagePath: String? = null
)