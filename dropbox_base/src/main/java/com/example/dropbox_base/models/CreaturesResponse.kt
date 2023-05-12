package com.example.dropbox_base.models

import com.example.dropbox_base.models.armor.ArmorFavoriteList
import com.google.gson.annotations.SerializedName

data class CreaturesResponse(
    @field:SerializedName("bemvc_list") val Creature_Model: Creature_Model? = null
)

data class Creature_Model(
    @field:SerializedName("Passive Creatures") val Passive_Creatures: List<Passive_Creatures?>? = null,
    @field:SerializedName("Aggressive Creatures") val Aggressive_Creatures: List<Aggressive_Creatures?>? = null,
    @field:SerializedName("Bosses") val Bosses: List<Bosses?>? = null,
    @field:SerializedName("NPCs") val jsonArmorLegsList: List<NPCs?>? = null,
    @field:SerializedName("Favorites") val jsonFavoriteList: List<ArmorFavoriteList?>? = null
)


data class Passive_Creatures(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("bemvcd4") val name: String? = null,
    @field:SerializedName("bemvci1") val description: String? = null,
    @field:SerializedName("bemvcf2") val imagePath: String? = null
)
data class Aggressive_Creatures(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("bemvcd4") val name: String? = null,
    @field:SerializedName("bemvci1") val description: String? = null,
    @field:SerializedName("bemvcf2") val imagePath: String? = null
)
data class Bosses(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("bemvcd4") val name: String? = null,
    @field:SerializedName("bemvci1") val description: String? = null,
    @field:SerializedName("bemvcf2") val imagePath: String? = null
)
data class NPCs(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("bemvcd4") val name: String? = null,
    @field:SerializedName("bemvci1") val description: String? = null,
    @field:SerializedName("bemvcf2") val imagePath: String? = null
)
