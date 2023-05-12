package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class MapsJsonResponse(

	@field:SerializedName("4_4mpxg_list")
	val maps_list: List<Maps_Model?>? = null
)

data class Maps_Model(
	@field:SerializedName("new") val newVal: String? = null,
	@field:SerializedName("4_4mpxgd4") val name: String? = null,
	@field:SerializedName("4_4mpxgi1") val description: String? = null,
	@field:SerializedName("4_4mpxgf2") val imagePath: String? = null
)