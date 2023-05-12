package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class BiomeResponse(
    @field:SerializedName("pk-_list") val Biome_pkList: List<BiomPkList?>? = null,
    @field:SerializedName("Favorites") val Favorites: List<String?>? = null,
)


data class BiomPkList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("pk-d4") val name: String? = null,
    @field:SerializedName("pk-i1") val description: String? = null,
    @field:SerializedName("pk-f2") val imagePath: String? = null
)