package com.example.dropbox_base.models

import com.google.gson.annotations.SerializedName

data class WallpapersJsonResponse(

	@field:SerializedName("cra_list")
	val craList: CraList? = null
)

data class CraList(

	@field:SerializedName("cra_fh8_v")
	val craFh8V: String? = null,

	@field:SerializedName("Wallpapers")
	val wallpapers: ArrayList<WallpapersItem>? = null,

	@field:SerializedName("Favorites")
	val favorites: List<Any?>? = null
)

data class WallpapersItem(

	@field:SerializedName("new")
	val jsonMemberNew: String? = null,

	var imageUrl: String? = null,
	var isRequestedForImageUrl: Boolean? = false,

	@field:SerializedName("craf2")
	val craf2: String? = null


)
