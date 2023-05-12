package com.appcake.modsforvalheim.core.ui.fragments.biome

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appcake.modsforvalheim.AppApplication
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.biome.BiomeCategoryAdapter
import com.appcake.modsforvalheim.core.adapter.biome.BiomeeAdapter
import com.appcake.modsforvalheim.core.db.DbHelper
import com.appcake.modsforvalheim.core.interfaces.OnCategoryItemListener
import com.appcake.modsforvalheim.core.interfaces.OnItemClickListener
import com.appcake.modsforvalheim.core.model.ArmorData
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.appcake.modsforvalheim.databinding.FragmentBiomeBinding
import com.appcake.modsforvalheim.utlis.SpacesItemDecoration
import com.example.dropbox_base.models.BiomeResponse
import com.example.dropbox_base.utils.Constantdropbox
import com.example.dropbox_base.utils.DropboxServices
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class BiomeFragment : Fragment(), OnCategoryItemListener, OnItemClickListener,
    View.OnClickListener {

    lateinit var binding: FragmentBiomeBinding
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var navController: NavController


    private lateinit var biomeAdapter: BiomeeAdapter
    private var biomeCategoryAdapter: BiomeCategoryAdapter? = null
    private var biomeDataList: ArrayList<ArmorData> = ArrayList()
    private lateinit var namesJsonRes: BiomeResponse

    private var armorPlaceHolderDataList: ArrayList<String> = ArrayList()
    var filerList: Array<String>? = null
    var armorList: ArrayList<ArmorData> = ArrayList()
    var contentToShow: ArrayList<ArmorData> = ArrayList()
    var favList: ArrayList<ArmorData> = ArrayList()
    var selectedTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBiomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        filerList = arrayOf(
            requireContext().resources.getString(R.string.all_lbl),
            requireContext().resources.getString(R.string.new_lbl),
            requireContext().resources.getString(R.string.favorites_lbl)
        )
        initView()
        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        armorList.clear()
        armorList = DbHelper(requireContext()).getAllData("Biome") as ArrayList<ArmorData>
        if (armorList.size == 0) {
            (activity as MainActivity).showLoader()
            setUpObserverForDropbox()
        } else {
            fetchDataFromDataBase()
        }

    }

    private fun initView() {
        binding.seedsSideNavButton.setOnClickListener(this)
        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.mainRV.layoutManager = layoutManager
        binding.mainRV.addItemDecoration(SpacesItemDecoration(14))
    }


    private fun initSearchView() {
        val adapterSearchItems = ArrayAdapter(
            requireContext(),
            R.layout.custom_search_items_armor,
            R.id.txtSuggestNameSeed,
            armorPlaceHolderDataList
        )
        binding!!.edtSearchViewSeed.setAdapter(adapterSearchItems)

        binding!!.edtSearchViewSeed.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    if (event.rawX >= (binding!!.edtSearchViewSeed.right -
                                binding!!.edtSearchViewSeed.compoundDrawables[2].bounds.width())
                    ) {
                        binding!!.edtSearchViewSeed.setText("")
                        return true
                    }
                }
                return false
            }
        })

        binding.edtSearchViewSeed.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                biomeAdapter?.filter?.filter(s.toString())
            }
        })
    }

    private fun setUpObserverForDropbox() {
        DropboxServices.getInstance(requireActivity()).loadFileDataDrop(
            jsonFilePathDropbox = Constantdropbox.BIOME_JSON_PATH_DROPBOX,
            cacheDirPath = File(requireActivity().filesDir, "source${File.separator}biomes")
        )
        DropboxServices.getInstance(requireContext())
            .getJsonResponse(object : DropboxServices.OnJsonResult {
                override fun getJsonResult(jsonFilePath: String) {
                    if (jsonFilePath != "") {
                        namesJsonRes = DropboxServices.getInstance(requireContext()).getJsonRes(
                            jsonFilePath, BiomeResponse::class.java
                        )

                        for (json in namesJsonRes.Biome_pkList!!) {

                            var armor = ArmorData(
                                0,
                                "Biome",
                                json?.name!!,
                                json?.description!!,
                                json?.imagePath!!,
                                json?.newVal!!,
                                0,
                                null
                            )
                            DbHelper(requireContext()).insertInToTable(armor)


                        }
                        fetchDataFromDataBase()
                    }

                }

                override fun getJsonError() {
                }
            })

        //     mapsAdapter.notifyDataSetChanged()
    }

    private fun fetchDataFromDataBase() {
        armorList.clear()
        armorList = DbHelper(requireContext()).getAllData("Biome") as ArrayList<ArmorData>
        armorPlaceHolderDataList = ArrayList()
        for (obj in armorList) {
            armorPlaceHolderDataList.add(obj.itemTitle)
        }
        Log.wtf("SizeOF", armorList.size.toString())
        setLockForContent(armorList!!)
        setUpRVForItems(contentToShow!!)
        setUpVForCategory()
        initSearchView()
        (activity as MainActivity).hideLoader()
    }

    private fun setLockForContent(newDataList: ArrayList<ArmorData>){
        if (AppApplication.sessionManager.subscriptionCheck){
            for (obj in newDataList){
                contentToShow.add(ArmorData(
                        obj.id,
                        obj.itemType,
                        obj.itemTitle,
                        obj.itemDes,
                        obj.imageId,
                        obj.category,
                        obj.favorite,
                        obj.imageUrl,
                        obj.isRequestedForImageUrl,
                        false
                    ))
            }
        }else{
            var count = newDataList.size*15
            count /= 100
            for ((index, value) in newDataList.withIndex()){
                if (count<index){
                    contentToShow.add(ArmorData(
                        value.id,
                        value.itemType,
                        value.itemTitle,
                        value.itemDes,
                        value.imageId,
                        value.category,
                        value.favorite,
                        value.imageUrl,
                        value.isRequestedForImageUrl,
                        true
                    ))
                }else{
                    contentToShow.add(ArmorData(
                        value.id,
                        value.itemType,
                        value.itemTitle,
                        value.itemDes,
                        value.imageId,
                        value.category,
                        value.favorite,
                        value.imageUrl,
                        value.isRequestedForImageUrl,
                        false
                    ))
                }
            }
        }


    }

    //TODO: Recycler View
    private fun setUpVForCategory() {
        biomeCategoryAdapter = BiomeCategoryAdapter(
            requireContext(),
            this,
            filerList!!,
            0
        )
        binding!!.seedCategoryRv.apply {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            setHasFixedSize(true)
            adapter = biomeCategoryAdapter
        }
    }

    private fun setUpRVForItems(biomeDataList: ArrayList<ArmorData>) {


        biomeAdapter =
            BiomeeAdapter(
                requireContext(),
                this,
                biomeDataList,
                2,
                AppApplication.sessionManager.subscriptionCheck
            )
        binding.mainRV.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = biomeAdapter
        }

        /*spanCount is the number of grids, for instance, (2 = 2*2 grid, 3 = 3*3)*/
        // binding.mainRV.addItemDecoration(GridItemOffsetDecoration(2, 10))

//        val layoutManager = GridLayoutManager(requireContext(), 2)
//        binding.mainRV.layoutManager = layoutManager

        binding.mainRV.adapter = biomeAdapter
        (activity as MainActivity).hideLoader()
    }

    override fun onCategoryItemClicked(positionSeed: Int, idSeed: Int) {
        biomeCategoryAdapter?.updatePosition(positionSeed)
        selectedTab = positionSeed
        armorList.clear()
        armorList = DbHelper(requireContext()).getAllData("Biome") as ArrayList<ArmorData>
        if (positionSeed == 0) {
            armorList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        } else if (positionSeed == 1) {
            armorList.filter { x ->
                x.category == "true"
            }
            armorList.let { setUpRVForItems(it as ArrayList<ArmorData>) }

        } else if (positionSeed == 2) {
            favList.clear()
            favList = ArrayList()
            favList = DbHelper(requireContext()).getFavorite("Biome") as ArrayList<ArmorData>
            favList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        } else if (positionSeed == 3) {
            Collections.shuffle(armorList)
            armorList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        } else if (positionSeed == 4) {
            Collections.shuffle(armorList)
            armorList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        }
    }

    override fun onItemClicked(model: ArmorData) {
        try {
            val direction = BiomeFragmentDirections.actionBiomeToBiomeDetailFragment(
                model
            )
            findNavController().navigate(direction)
        } catch (ee: Exception) {

        }
    }

    override fun onFavClicked(model: ArmorData, image: ImageView) {
        val item = model
        item.let {
            if (item.favorite == 0) {
                DbHelper(requireContext()).updateFavorite(1, item.id!!)
                image.setImageDrawable(activity?.getDrawable(R.drawable.favortie_icon))
            } else {
                image.setImageDrawable(activity?.getDrawable(R.drawable.unfavorite_icon))
                DbHelper(requireContext()).updateFavorite(0, item.id!!)
            }
            if (selectedTab == 2) {
                favList.clear()
                favList = ArrayList()
                favList = DbHelper(requireContext()).getFavorite("Biome") as ArrayList<ArmorData>
                favList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
            } else {
                armorList.clear()
                armorList = DbHelper(requireContext()).getAllData("Biome") as ArrayList<ArmorData>
                biomeAdapter.resetData(armorList)
            }
        }
    }

    override fun onSaveImageUrl(positionSeed: Int, url: String) {
        try {
            val item = biomeAdapter?.dataList?.get(positionSeed)
            DbHelper(requireContext()).updateImageUrl(item.id!!, url)
        } catch (ee: Exception) {

        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.seedsSideNavButton -> {
                (activity as MainActivity).hideBottomNav()
                navController.navigate(R.id.drawer)
            }
        }
    }
}