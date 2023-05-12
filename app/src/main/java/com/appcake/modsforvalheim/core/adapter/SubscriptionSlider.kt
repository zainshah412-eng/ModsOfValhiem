package com.appcake.modsforvalheim.core.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.model.SubscriptionModel
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SubscriptionSlider(var contextSeed: Context,
                         var mSliderItemsSeed: MutableList<SubscriptionModel>) :
    SliderViewAdapter<SubscriptionSlider.SliderAdapterVHSeed>() {

    fun renewItems(sliderItems: MutableList<SubscriptionModel>) {
        mSliderItemsSeed = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItemSeed(positionSeed: Int) {
        mSliderItemsSeed.removeAt(positionSeed)
        notifyDataSetChanged()
    }

    fun addItemSeed(sliderItemSeed: SubscriptionModel) {
        mSliderItemsSeed.add(sliderItemSeed)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parentSeed: ViewGroup): SliderAdapterVHSeed {
        val inflateSeed: View =
            LayoutInflater.from(parentSeed.context).inflate(R.layout.item_subscription_slider, null)
        return SliderAdapterVHSeed(inflateSeed)
    }

    override fun onBindViewHolder(viewHolderSeed: SliderAdapterVHSeed, positionSeed: Int) {
        val sliderItemSeed: SubscriptionModel = mSliderItemsSeed[positionSeed]
        viewHolderSeed.textSlider.text = sliderItemSeed.text
    }

    override fun getCount(): Int {
        return mSliderItemsSeed.size
    }

    inner class SliderAdapterVHSeed(itemViewSeed: View) : ViewHolder(itemViewSeed) {
        var textSlider: TextView = itemViewSeed.findViewById(R.id.iv_text_slider)
        var imageGifContainer: ImageView = itemViewSeed.findViewById(R.id.iv_gif_container)
        var textViewDescriptionSeed: TextView = itemViewSeed.findViewById(R.id.tv_auto_image_slider)

    }
}