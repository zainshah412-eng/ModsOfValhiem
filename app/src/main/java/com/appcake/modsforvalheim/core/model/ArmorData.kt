package com.appcake.modsforvalheim.core.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArmorData(
    var id:Int? = 0,
    var itemType: String,
    var itemTitle: String,
    var itemDes: String,
    var imageId: String,
    var category: String,
    var favorite: Int,
    var imageUrl: String? = null,
    var isRequestedForImageUrl: Boolean? = false,
    var isLocked: Boolean? = false,
) : Parcelable
