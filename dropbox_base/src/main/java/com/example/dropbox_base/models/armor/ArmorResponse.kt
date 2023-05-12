package com.example.dropbox_base.models.armor

import com.google.gson.annotations.SerializedName

data class ArmorResponse(
    @field:SerializedName("_oqn-__list") val Armor_oqnModel: Armor_oqnResponse? = null
)

data class Armor_oqnResponse(
    @field:SerializedName("Capes") val jsonArmorCapsList: List<ArmorCapesList?>? = null,
    @field:SerializedName("Helmets") val jsonArmorHelmetsList: List<ArmorHelmetsList?>? = null,
    @field:SerializedName("Chest Armor") val jsonArmorChestList: List<ArmorChestList?>? = null,
    @field:SerializedName("Leg Armor") val jsonArmorLegsList: List<ArmorLegList?>? = null,
    @field:SerializedName("Favorites") val jsonFavoriteList: List<ArmorFavoriteList?>? = null
)


data class ArmorCapesList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("_oqn-_d4") val name: String? = null,
    @field:SerializedName("_oqn-_i1") val description: String? = null,
    @field:SerializedName("_oqn-_t3") val imagePath: String? = null
)
data class ArmorHelmetsList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("_oqn-_d4") val name: String? = null,
    @field:SerializedName("_oqn-_i1") val description: String? = null,
    @field:SerializedName("_oqn-_t3") val imagePath: String? = null
)
data class ArmorChestList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("_oqn-_d4") val name: String? = null,
    @field:SerializedName("_oqn-_i1") val description: String? = null,
    @field:SerializedName("_oqn-_t3") val imagePath: String? = null
)
data class ArmorLegList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("_oqn-_d4") val name: String? = null,
    @field:SerializedName("_oqn-_i1") val description: String? = null,
    @field:SerializedName("_oqn-_t3") val imagePath: String? = null
)
data class ArmorFavoriteList(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("_oqn-_d4") val name: String? = null,
    @field:SerializedName("_oqn-_i1") val description: String? = null,
    @field:SerializedName("_oqn-_t3") val imagePath: String? = null
)