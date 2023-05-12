package com.appcake.modsforvalheim.core.adapter

import com.appcake.modsforvalheim.R

class AdapterPagerValhiem (private val dataItemsSkf:Array<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterPagerValhiem.PagerViewHolderSkf>(){

    override fun onCreateViewHolder(parentAp3: android.view.ViewGroup, viewTypeAp4: Int): PagerViewHolderSkf
    {
        return PagerViewHolderSkf(android.view.LayoutInflater.from(parentAp3.context).inflate(R.layout.item_text_skins_pager, parentAp3, false))
    }

    override fun getItemCount(): Int {
        return dataItemsSkf.size
    }

    override fun onBindViewHolder(holderObh5: PagerViewHolderSkf, positionObh4: Int) {
        holderObh5.bindSkf(dataItemsSkf[positionObh4])
    }

    inner class PagerViewHolderSkf(itemViewAps: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemViewAps){
        private val tvTitleText:android.widget.TextView = itemViewAps.findViewById(R.id.iv_text_slider)

        fun bindSkf(dataText:String){
            tvTitleText.text = dataText
        }
    }
}