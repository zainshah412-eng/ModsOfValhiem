package com.appcake.modsforvalheim.core.adapter.maps

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.interfaces.OnCategoryItemListener

class MapsCategoryAdapter(
    var contextSeed: Context,
    var mapsCategoryItemListner: OnCategoryItemListener,
    var categoryList: Array<String>,
    var activePosition: Int
) : RecyclerView.Adapter<MapsCategoryAdapter.ViewHolderArmor>() {

    inner class ViewHolderArmor(viewSeed: View) : RecyclerView.ViewHolder(viewSeed) {
        var item_seed_category_tv: TextView = viewSeed.findViewById(R.id.item_maps_category_tv)
    }

    override fun onCreateViewHolder(parentSeed: ViewGroup, viewTypeSeed: Int): ViewHolderArmor {
        val viewSeed = LayoutInflater.from(parentSeed.context)
            .inflate(R.layout.item_maps_category, parentSeed, false)
        return ViewHolderArmor(viewSeed)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holderSeed: ViewHolderArmor, positionSeed: Int) {

        holderSeed.item_seed_category_tv.text = categoryList[positionSeed]

        if (activePosition == positionSeed) {
            holderSeed.item_seed_category_tv.setBackgroundResource(R.drawable.item_armor_category_background)
            holderSeed.item_seed_category_tv.setTextColor(Color.WHITE)
        } else {
            holderSeed.item_seed_category_tv.setBackgroundResource(R.drawable.item_armor_category_dark_bg)
            holderSeed.item_seed_category_tv.setTextColor(contextSeed.getColor(R.color.light_black_400))
        }

        holderSeed.itemView.setOnClickListener {
            mapsCategoryItemListner.onCategoryItemClicked(positionSeed, 1)
        }
    }
    open fun updatePosition(pos: Int){
        activePosition = pos
        notifyDataSetChanged()
    }
}