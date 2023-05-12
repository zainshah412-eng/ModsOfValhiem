package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName


data class ToolsResponse(
    @field:SerializedName("b6e7t3i_c_")
    val b6e7t3i_c_: String? = null,
    @field:SerializedName("b6e7t3i_gi5")
    val b6e7t3i_gi5: String? = null,
    @field:SerializedName("b6e7t3i_list")
    val tools_list: List<Tools_Model?>? = null
)

data class Tools_Model(
    @field:SerializedName("new") val newVal: String? = null,
    @field:SerializedName("b6e7t3ii4") val name: String? = null,
    @field:SerializedName("b6e7t3id1") val description: String? = null,
    @field:SerializedName("b6e7t3if2") val imagePath: String? = null

)