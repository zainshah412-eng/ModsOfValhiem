package com.appcake.modsforvalheim.core.adapter.Armor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.biome.BiomeeAdapter
import com.appcake.modsforvalheim.core.interfaces.OnItemClickListener
import com.appcake.modsforvalheim.core.model.ArmorData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.dropbox_base.utils.Cache
import com.example.dropbox_base.utils.Constantdropbox
import com.example.dropbox_base.utils.DropboxServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ArmorrAdapter(
    var contextSeed: Context,
    var onItemClickListner: OnItemClickListener,
    var dataList: java.util.ArrayList<ArmorData>,
    var count: Int,
    var subscription: Boolean
) :
    RecyclerView.Adapter<ArmorrAdapter.ViewHolder>(), Filterable {
    private var dataListFilter = ArrayList<ArmorData>()
    val mCacheMods = Cache(contextSeed, cacheDirName = "armorimage")

    init {
        dataListFilter = dataList
    }

    open fun resetData(list: java.util.ArrayList<ArmorData>) {
        dataList.clear()
        dataListFilter.clear()
        dataListFilter = list
        dataList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_armor,
                null
            )
        )
    }

    override fun getItemCount(): Int {
        return dataListFilter.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataListFilter[position]
        with(viewHolder)
        {
            nameTv.text = dataListFilter[position].itemTitle
            if (dataListFilter[position].category == "true") {
                newTv.visibility = View.VISIBLE
            } else {
                newTv.visibility = View.GONE
            }
            if (dataListFilter[position].favorite == 1) {
                favImg.setImageDrawable(contextSeed.getDrawable(R.drawable.favortie_icon))
            } else {
                favImg.setImageDrawable(contextSeed.getDrawable(R.drawable.unfavorite_icon))
            }

            if (mCacheMods.isImageAvailableInCache(dataListFilter[position].imageId)) {
                val cacheImagePath =
                    mCacheMods.getCacheImagePath(dataListFilter[position].imageId)
                Glide.with(contextSeed).load(cacheImagePath).into(seedImg)
                dataListFilter[position].imageId = cacheImagePath
           //     dataList[position].imageId = cacheImagePath

                Log.wtf("FROM CACHE", dataListFilter[position].imageId)

            } else {
                if (dataListFilter[position].imageUrl == null ||
                    dataListFilter[position].imageUrl == "") {

                    val imageName = dataListFilter[position].imageId
                    if (!dataListFilter[position].isRequestedForImageUrl!!) {
                        dataListFilter[position].isRequestedForImageUrl = true
                        Log.wtf("GET_ID", dataListFilter[position].imageId)
                        Log.wtf("GET_URL", dataListFilter[position].imageUrl)
                        CoroutineScope(Dispatchers.IO).launch {
                            val imgUrl: String =
                                DropboxServices.getInstance(contextSeed.applicationContext)
                                    .loadImageToView(
                                        imageName!!,
                                        Constantdropbox.ARMOR_IMAGE_JSON_PATH_DROPBOX
                                    ) as String
                            Log.wtf("URL_FOUND_AFTER_API", imgUrl)
                            onItemClickListner.onSaveImageUrl(position, imgUrl)

                            withContext(Dispatchers.Main) {
                                Glide.with(contextSeed.applicationContext).asBitmap().load(imgUrl)
                                    .into(object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(
                                            resource: Bitmap, transition: Transition<in Bitmap>?
                                        ) {
                                            try {
                                                mCacheMods.saveBitmpInCache(resource, imageName)
                                                seedImg.setImageBitmap(resource)
                                            } catch (ee: java.lang.Exception) {
                                                Log.wtf("Error",ee.message.toString())
                                            }
                                        }
                                        override fun onLoadCleared(placeholder: Drawable?) {
                                        }

                                    })
                            }
                        }
                    } else {
                        Glide.with(contextSeed).load(R.drawable.progress_animation).into(seedImg)
                    }
                } else {
                    Log.wtf("URL_FOUND", dataListFilter[position].imageUrl)
                    Glide.with(contextSeed).load(dataListFilter[position].imageUrl)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.progress_animation)
                        .into(seedImg)
                }

            }
            if (item.isLocked == false){
                seedImgTwo.visibility = View.GONE
                newTv.visibility = View.VISIBLE
                favImg.visibility = View.VISIBLE
                itemView.setOnClickListener {
                    onItemClickListner.onItemClicked(item)}
                favImg.setOnClickListener {
                    onItemClickListner.onFavClicked(item, favImg)
                }
            }else{
                seedImgTwo.visibility = View.VISIBLE
                newTv.visibility = View.GONE
                favImg.visibility = View.GONE
            }
//            if (subscription){
//                seedImgTwo.visibility = View.GONE
//                newTv.visibility = View.VISIBLE
//                favImg.visibility = View.VISIBLE
//                itemView.setOnClickListener {
//                    onItemClickListner.onItemClicked(item)}
//                favImg.setOnClickListener {
//                    onItemClickListner.onFavClicked(item, favImg)
//                }
//            }else{
//                if (count < position){
//                    seedImgTwo.visibility = View.VISIBLE
//                    newTv.visibility = View.GONE
//                    favImg.visibility = View.GONE
//                }else{
//                    seedImgTwo.visibility = View.GONE
//                    newTv.visibility = View.VISIBLE
//                    favImg.visibility = View.VISIBLE
//                    itemView.setOnClickListener {
//                        onItemClickListner.onItemClicked(item)}
//                    favImg.setOnClickListener {
//                        onItemClickListner.onFavClicked(item, favImg)
//                    }
//                }
//            }
        }

        //
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var favImg: ImageView = itemView.findViewById(R.id.item_seed_fav_iv)
        var seedImg: ImageView = itemView.findViewById(R.id.item_seed_iv)
        var seedImgTwo: ImageView = itemView.findViewById(R.id.item_seed_iv_two)
        var descriptionTv: TextView = itemView.findViewById(R.id.item_seed_desc_tv)
        var nameTv: TextView = itemView.findViewById(R.id.item_seed_name_tv)
        var newTv: TextView = itemView.findViewById(R.id.item_seed_new_tv)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    dataListFilter = dataList
                } else {
                    val resultList = ArrayList<ArmorData>()
                    for (row in dataList) {
                        if (row.itemTitle?.lowercase(Locale.ROOT)?.contains(
                                charSearch.lowercase(
                                    Locale.ROOT
                                )
                            )!!
                        ) {
                            resultList.add(row)
                        }
                    }
                    dataListFilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = dataListFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataListFilter = results?.values as ArrayList<ArmorData>
                notifyDataSetChanged()
            }

        }
    }
}