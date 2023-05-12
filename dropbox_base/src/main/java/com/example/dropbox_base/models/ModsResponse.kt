package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class ModsResponse(
    @field:SerializedName("p261_syyf") val p261_syyf: String? = null,
    @field:SerializedName("p261_z") val p261_z: String? = null,
    @field:SerializedName("p261_hm") val p261_hm: String? = null,
    @field:SerializedName("p261_5_") val p261_5_: String? = null,
    @field:SerializedName("p261_list") val p261_list: List<p261_list?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)

data class p261_list(
    @field:SerializedName("p261_dq0k") val p261_dq0k: String? = null,
    @field:SerializedName("p261d4") val name: String? = null,
    @field:SerializedName("p261_p") val p261_p: String? = null,
    @field:SerializedName("p261f2") val description: String? = null,
    @field:SerializedName("p261t3") val imagePath: String? = null,
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("p261_l64v") val p261_l64v: String? = null
)