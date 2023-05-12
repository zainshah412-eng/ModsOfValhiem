package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class CheatsResponse(
    @field:SerializedName("2g8f_vw") val val2g8f_vw: String? = null,
    @field:SerializedName("2g8f_udmq2") val val2g8f_udmq2: String? = null,
    @field:SerializedName("2g8f_c5") val val2g8f_c5: String? = null,
    @field:SerializedName("2g8f_list") val val2g8f_list: List<val2g8f_list?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)

data class val2g8f_list(
    @field:SerializedName("2g8f_ek") val v2g8f_ek: String? = null,
    @field:SerializedName("2g8ft3") val name: String? = null,
    @field:SerializedName("2g8fi1") val des: String? = null,
    @field:SerializedName("2g8fd4") val image: String? = null,
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("2g8f_-y") val v2g8f: String? = null
)