package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName


data class CharacterResponse(
    @field:SerializedName("l-6_fx9") val l_6_fx9: String? = null,
    @field:SerializedName("l-6_r3c") val l_6_r3c: String? = null,
    @field:SerializedName("l-6__puaz") val l_6__puaz: String? = null,
    @field:SerializedName("l-6_list") val character_list: List<Character_Model?>? = null
)

data class Character_Model(

    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("l-6d4") val name: String? = null,
    @field:SerializedName("l-6i1") val description: String? = null,
    @field:SerializedName("l-6f2") val imagePath: String? = null
)