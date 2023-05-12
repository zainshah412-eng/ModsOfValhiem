package com.appcake.modsforvalheim.core.interfaces

import android.widget.ImageView
import com.appcake.modsforvalheim.core.model.ArmorData

interface OnItemClickListener {
    fun onItemClicked(model: ArmorData)
    fun onFavClicked(model: ArmorData, image: ImageView)

    fun onSaveImageUrl(positionSeed: Int,url:String)
}