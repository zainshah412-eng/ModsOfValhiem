package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class SeedsResponse(
    @field:SerializedName("4914_list") val Seeds_List: List<SeedsList?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)


data class SeedsList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("4914i1") val name: String? = null,
    @field:SerializedName("4914f2") val description: String? = null,
    @field:SerializedName("4914n3") val imagePath: String? = null
)