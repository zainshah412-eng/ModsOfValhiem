package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class ModsJsonResponse(

    @field:SerializedName("8-z_list") val jsonMember8ZList: JsonMember8ZList? = null
) {

    data class JsonMember8ZList(

        @field:SerializedName("Utilities") val utilities: List<UtilitiesItem?>? = null,

        @field:SerializedName("Miscellaneous") val miscellaneous: List<MiscellaneousItem?>? = null,

        @field:SerializedName("Models and Textures") val modelsAndTextures: List<ModelsAndTexturesItem?>? = null,

        @field:SerializedName("Top") val top: List<Any?>? = null,

        @field:SerializedName("Visuals") val visuals: ArrayList<VisualsItem>? = null,

        @field:SerializedName("Characters and outfits") val charactersAndOutfits: List<CharactersAndOutfitsItem?>? = null,

        @field:SerializedName("Favorites") val favorites: List<Any?>? = null
    )

    data class ModelsAndTexturesItem(

        @field:SerializedName("new") val jsonMemberNew: String? = null,

        @field:SerializedName("8-zi1") val jsonMember8Zi1: String? = null,

        @field:SerializedName("8-zf2") val jsonMember8Zf2: String? = null,

        @field:SerializedName("8-zd4") val jsonMember8Zd4: String? = null
    )

    data class VisualsItem(

        @field:SerializedName("new") val jsonMemberNew: String? = null,

        @field:SerializedName("8-zi1") val jsonMember8Zi1: String? = null,

        @field:SerializedName("8-zf2") val jsonMember8Zf2: String? = null,
        var imageUrl: String? = null,
        var isRequestedForImageUrl: Boolean? = false,

        @field:SerializedName("8-zd4") val jsonMember8Zd4: String? = null
    ) {
        override fun toString(): String {
            return jsonMember8Zd4!!
        }
    }

    data class UtilitiesItem(

        @field:SerializedName("new") val jsonMemberNew: String? = null,

        @field:SerializedName("8-zi1") val jsonMember8Zi1: String? = null,

        @field:SerializedName("8-zf2") val jsonMember8Zf2: String? = null,

        @field:SerializedName("8-zd4") val jsonMember8Zd4: String? = null
    )

    data class MiscellaneousItem(

        @field:SerializedName("new") val jsonMemberNew: String? = null,

        @field:SerializedName("8-zi1") val jsonMember8Zi1: String? = null,

        @field:SerializedName("8-zf2") val jsonMember8Zf2: String? = null,

        @field:SerializedName("8-zd4") val jsonMember8Zd4: String? = null
    )

    data class CharactersAndOutfitsItem(

        @field:SerializedName("new") val jsonMemberNew: String? = null,

        @field:SerializedName("8-zi1") val jsonMember8Zi1: String? = null,

        @field:SerializedName("8-zf2") val jsonMember8Zf2: String? = null,

        @field:SerializedName("8-zd4") val jsonMember8Zd4: String? = null
    )
}
