package com.appcake.modsforvalheim.core.ui.fragments.armor

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appcake.modsforvalheim.AppApplication
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.Armor.ArmorCategoryAdapter
import com.appcake.modsforvalheim.core.adapter.Armor.ArmorrAdapter
import com.appcake.modsforvalheim.core.db.DbHelper
import com.appcake.modsforvalheim.core.interfaces.OnCategoryItemListener
import com.appcake.modsforvalheim.core.interfaces.OnItemClickListener
import com.appcake.modsforvalheim.core.model.ArmorData
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.appcake.modsforvalheim.databinding.FragmentArmorBinding
import com.appcake.modsforvalheim.utlis.SpacesItemDecoration
import com.example.dropbox_base.models.armor.ArmorResponse
import com.example.dropbox_base.utils.Constantdropbox.ARMOR_JSON_PATH_DROPBOX
import com.example.dropbox_base.utils.DropboxServices
import com.google.android.gms.ads.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class Armor : Fragment(), OnCategoryItemListener, OnItemClickListener, View.OnClickListener {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var binding: FragmentArmorBinding
    private lateinit var navController: NavController
    private var armorCategoryAdapter: ArmorCategoryAdapter? = null
    private lateinit var armorAdapter: ArmorrAdapter
    private var armorPlaceHolderDataList: ArrayList<String> = ArrayList()
    private lateinit var namesJsonRes: ArmorResponse
    var filerList: Array<String>? = null
    var armorList: ArrayList<ArmorData> = ArrayList()
    var contentToShow: ArrayList<ArmorData> = ArrayList()
    var favList: ArrayList<ArmorData> = ArrayList()
    var selectedTab = 0
    lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArmorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        armorPlaceHolderDataList = ArrayList<String>()
        filerList = arrayOf(
            requireContext().resources.getString(R.string.all_lbl),
            requireContext().resources.getString(R.string.new_lbl),
            requireContext().resources.getString(R.string.favorites_lbl)
        )

        initView()

        armorList.clear()
        armorList = DbHelper(requireContext()).getAllData("Armor") as ArrayList<ArmorData>
        if (armorList.size == 0) {
            (activity as MainActivity).showLoader()
            setUpObserverForDropbox()
        } else {
            fetchDataFromDataBase()
            //test for Bitrise CI/CD
        }

        MobileAds.initialize(requireContext()) {}
        val adRequest = AdRequest.Builder().build()
        //   binding.adView.loadAd(adRequest)
        if (adRequest != null) {
            binding.adView.loadAd(adRequest)
            //  Toast.makeText(requireContext(),"Yes",Toast.LENGTH_LONG).show()

        } else {
            //  Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        binding.seedSideNavButton.setOnClickListener(this)
        binding.adView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

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

                armorAdapter?.filter?.filter(s.toString())
            }
        })

    }


    private fun setUpObserverForDropbox() {
        try {
            DropboxServices.getInstance(requireActivity()).loadFileDataDrop(
                jsonFilePathDropbox = ARMOR_JSON_PATH_DROPBOX,
                cacheDirPath = File(requireActivity().filesDir, "source${File.separator}armor")
            )
            DropboxServices.getInstance(requireContext())
                .getJsonResponse(object : DropboxServices.OnJsonResult {
                    override fun getJsonResult(jsonFilePath: String) {
                        if (jsonFilePath != "" && armorList.size == 0) {
                            namesJsonRes = DropboxServices.getInstance(requireContext()).getJsonRes(
                                jsonFilePath, ArmorResponse::class.java
                            )
                            for (json in namesJsonRes.Armor_oqnModel?.jsonArmorCapsList!!) {
                                var armor = ArmorData(
                                    0,
                                    "Armor",
                                    json?.name!!,
                                    json?.description!!,
                                    json?.imagePath!!,
                                    json?.newVal!!,
                                    0,
                                    null
                                )
                                DbHelper(requireContext()).insertInToTable(armor)
                            }

                            for (json in namesJsonRes.Armor_oqnModel?.jsonArmorChestList!!) {
                                var armor = ArmorData(
                                    0,
                                    "Armor",
                                    json?.name!!,
                                    json?.description!!,
                                    json?.imagePath!!,
                                    json?.newVal!!,
                                    0,
                                    null
                                )
                                DbHelper(requireContext()).insertInToTable(armor)
                                //    armorPlaceHolderDataList.add(json?.name!!)
                            }

                            for (json in namesJsonRes.Armor_oqnModel?.jsonArmorHelmetsList!!) {
                                var armor = ArmorData(
                                    0,
                                    "Armor",
                                    json?.name!!,
                                    json?.description!!,
                                    json?.imagePath!!,
                                    json?.newVal!!,
                                    0,
                                    null
                                )
                                DbHelper(requireContext()).insertInToTable(armor)
                                //    armorPlaceHolderDataList.add(json?.name!!)
                            }

                            for (json in namesJsonRes.Armor_oqnModel?.jsonArmorLegsList!!) {
                                var armor = ArmorData(
                                    0,
                                    "Armor",
                                    json?.name!!,
                                    json?.description!!,
                                    json?.imagePath!!,
                                    json?.newVal!!,
                                    0,
                                    null
                                )
                                DbHelper(requireContext()).insertInToTable(armor)
                                //    armorPlaceHolderDataList.add(json?.name!!)
                            }
                        }

                        fetchDataFromDataBase()

                    }

                    override fun getJsonError() {
                    }
                })
        } catch (e: Exception) {
            Log.wtf("cacheeArmor", e.message)
        }
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
    private fun setUpRVForItems(armorDataList: ArrayList<ArmorData>) {
//        var countValue: Double = 0.0
//        countValue = (41/100).toDouble()
//        countValue *= 15
//        val finaValue = countValue.toInt()
        armorAdapter =
            ArmorrAdapter(
                requireContext(),
                this,
                armorDataList,
                5,
                AppApplication.sessionManager.subscriptionCheck
            )

        binding.mainRV.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = armorAdapter
        }
        binding.mainRV.adapter = armorAdapter
    }

    private fun setUpVForCategory() {
        armorCategoryAdapter = ArmorCategoryAdapter(
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
            adapter = armorCategoryAdapter
        }
    }

    private fun fetchDataFromDataBase() {
        armorList.clear()
        armorList = DbHelper(requireContext()).getAllData("Armor") as ArrayList<ArmorData>
        armorPlaceHolderDataList = ArrayList()
        for (obj in armorList) {
            armorPlaceHolderDataList.add(obj.itemTitle)
        }
        Log.wtf("SizeOF", armorList.size.toString())
        setLockForContent(armorList)
        setUpRVForItems(contentToShow)
        setUpVForCategory()
        initSearchView()
        (activity as MainActivity).hideLoader()
    }


    override fun onCategoryItemClicked(positionSeed: Int, idSeed: Int) {
        armorCategoryAdapter?.updatePosition(positionSeed)
        selectedTab = positionSeed
        if (positionSeed == 0) {
            armorList.clear()
            armorList = DbHelper(requireContext()).getAllData("Armor") as ArrayList<ArmorData>
            armorList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        } else if (positionSeed == 1) {
            armorList.filter { x ->
                x.category == "true"
            }
            armorList.let { setUpRVForItems(it as ArrayList<ArmorData>) }

        } else if (positionSeed == 2) {
            favList.clear()
            favList = ArrayList()
            favList = DbHelper(requireContext()).getFavorite("Armor") as ArrayList<ArmorData>
            favList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        } else if (positionSeed == 3) {
            armorList.clear()
            armorList = DbHelper(requireContext()).getAllData("Armor") as ArrayList<ArmorData>
            Collections.shuffle(armorList)
            armorList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        } else if (positionSeed == 4) {
            armorList.clear()
            armorList = DbHelper(requireContext()).getAllData("Armor") as ArrayList<ArmorData>
            Collections.shuffle(armorList)
            armorList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
        }
    }

    override fun onItemClicked(model: ArmorData) {
        try {
            val direction = ArmorDirections.actionArmorToArmorDetailFragment(
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
                favList = DbHelper(requireContext()).getFavorite("Armor") as ArrayList<ArmorData>
                favList?.let { setUpRVForItems(it as ArrayList<ArmorData>) }
            } else {
                armorList.clear()
                armorList = DbHelper(requireContext()).getAllData("Armor") as ArrayList<ArmorData>
                armorAdapter.resetData(armorList)
            }
        }
    }

    override fun onSaveImageUrl(positionSeed: Int, url: String) {
        try {
            val item = armorAdapter?.dataList?.get(positionSeed)
            DbHelper(requireContext()).updateImageUrl(item.id!!, url)
        } catch (e: Exception) {

        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.seedSideNavButton -> {
                (activity as MainActivity).hideBottomNav()
                navController.navigate(R.id.drawer)
            }
        }
    }
}