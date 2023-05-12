package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class KeysResponse(
    @field:SerializedName("ftzx_j") val ftzx_j: String? = null,
    @field:SerializedName("ftzx_jtoee") val ftzx_jtoee: String? = null,
    @field:SerializedName("ftzx_toc-8") val ftzx_toc: String? = null,
    @field:SerializedName("ftzx_x") val ftzx_x: String? = null,
    @field:SerializedName("ftzx_v") val ftzx_v: String? = null,
    @field:SerializedName("ftzx_7") val ftzx_7: String? = null,
    @field:SerializedName("ftzx_svp") val ftzx_svp: String? = null,
    @field:SerializedName("ftzx_list") val ftzx_list: List<ftzx_list?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)

data class ftzx_list(
    @field:SerializedName("ftzxf2") val name: String? = null,
    @field:SerializedName("ftzx_moe_") val ftzx_moe_: String? = null,
    @field:SerializedName("ftzxt3") val des: String? = null,
    @field:SerializedName("ftzxi1") val image: String? = null,
    @field:SerializedName("ftzx_trfq") val ftzx_trfq: String? = null,
    @field:SerializedName("new") val new: String? = null
)