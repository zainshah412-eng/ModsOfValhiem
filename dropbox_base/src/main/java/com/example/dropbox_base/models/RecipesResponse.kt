package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @field:SerializedName("th8-p_list") val list_th8: List<list_th8?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)


data class list_th8(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("th8-pd4") val name: String? = null,
    @field:SerializedName("th8-pi1") val description: String? = null,
    @field:SerializedName("th8-pf2") val imagePath: String? = null
)