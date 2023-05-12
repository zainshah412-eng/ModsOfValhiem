package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class SkinEditorJsonResponse(

    @field:SerializedName("ruc8b01_b4xr7") val ruc8b01B4xr7: String? = null,

    @field:SerializedName("ruc8b01_list") val ruc8b01List: Ruc8b01List? = null
)

data class Girl(

    @field:SerializedName("model") val model: String? = null,

    @field:SerializedName("hair") val hair: List<String?>? = null,

    @field:SerializedName("accessories") val accessories: List<String?>? = null,

    @field:SerializedName("legs") val legs: List<String?>? = null,

    @field:SerializedName("body") val body: List<String?>? = null
)

data class Ruc8b01List(

    @field:SerializedName("ruc8b01_dalg") val ruc8b01Dalg: String? = null,

    @field:SerializedName("Boy") val boy: Boy? = null,

    @field:SerializedName("Girl") val girl: Girl? = null,

    @field:SerializedName("Favorites") val favorites: List<Any?>? = null
)

data class Boy(

    @field:SerializedName("model") val model: String? = null,

    @field:SerializedName("hair") val hair: List<String?>? = null,

    @field:SerializedName("accessories") val accessories: List<String?>? = null,

    @field:SerializedName("legs") val legs: List<String?>? = null,

    @field:SerializedName("body") val body: List<String?>? = null
)
