package com.appcake.modsforvalheim.core.ui.fragments.mods

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.Armor.SliderAdapterFullSize
import com.appcake.modsforvalheim.core.adapter.SliderAdapterFixSize
import com.appcake.modsforvalheim.core.db.DbHelper
import com.appcake.modsforvalheim.core.model.SliderItem
import com.appcake.modsforvalheim.core.ui.fragments.armor.ArmorDetailFragmentArgs
import com.appcake.modsforvalheim.databinding.FragmentModsDetailBinding
import com.appcake.modsforvalheim.utlis.FileUtilsSkins
import com.example.dropbox_base.utils.DropboxServices
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import toast

class ModsDetailFragment : Fragment() {
    lateinit var binding: FragmentModsDetailBinding
    var sliderAdapter: SliderAdapterFullSize? = null
    private lateinit var navController: NavController
    private val args: ArmorDetailFragmentArgs by navArgs()
    var sliderList: ArrayList<SliderItem> = ArrayList()
    lateinit var adRequest: AdRequest
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initView()
        MobileAds.initialize(requireContext()) {}
        adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    fun initView() {

        binding.title.text = args.model.itemTitle
        binding.description.text = args.model.itemDes
        if (args.model.category == "true") {
            binding.seedDetailSliderNewTv.visibility = View.VISIBLE
        } else {

            binding.seedDetailSliderNewTv.visibility = View.GONE
        }
        if (args.model.favorite == 1) {
            binding.seedDetailSliderFavIv.setImageDrawable(requireContext().getDrawable(R.drawable.favortie_icon))
        } else {
            binding.seedDetailSliderFavIv.setImageDrawable(requireContext().getDrawable(R.drawable.unfavorite_icon))
        }

        sliderList.clear()
        sliderList = ArrayList()
        if (args.model.imageUrl != null) {
            var model = SliderItem(
                args.model.id!!,
                args.model.itemTitle,
                args.model.imageUrl ?: ""
            )
            sliderList.add(model)
            sliderList.add(model)
            sliderList.add(model)
            binding.seedDetailSliderNewTv
            sliderAdapter = SliderAdapterFullSize(requireContext(), sliderList)
            binding!!.seedDetailSlider.setSliderAdapter(sliderAdapter!!)
            binding!!.seedDetailSlider.startAutoCycle()
        } else {
            val imageName = args.model.imageId
            CoroutineScope(Dispatchers.IO).launch {
                val imgUrl: String =
                    DropboxServices.getInstance(requireContext())
                        .loadImageToView1(
                            imageName!!,
                            //   Constantdropbox.ARMOR_IMAGE_JSON_PATH_DROPBOX
                        ) as String
                withContext(Dispatchers.Main) {
                    var model = SliderItem(
                        args.model.id!!,
                        args.model.itemTitle,
                        imgUrl ?: ""
                    )
                    sliderList.add(model)
                    sliderList.add(model)
                    sliderList.add(model)
                    binding.seedDetailSliderNewTv
                    sliderAdapter = SliderAdapterFullSize(requireContext(), sliderList)
                    binding!!.seedDetailSlider.setSliderAdapter(sliderAdapter!!)
                    binding!!.seedDetailSlider.startAutoCycle()
                }
            }
        }
        binding!!.seedDetailSliderFavIv.setOnClickListener {
            val item = args.model

            item.let {
                if (item.favorite == 0) {
                    DbHelper(requireContext()).updateFavorite(1, item.id!!)
                    args.model.favorite = 1
                    val check_fav = item.favorite
                    binding.seedDetailSliderFavIv.setImageDrawable(activity?.getDrawable(R.drawable.favortie_icon))
                } else {
                    binding.seedDetailSliderFavIv.setImageDrawable(activity?.getDrawable(R.drawable.unfavorite_icon))
                    DbHelper(requireContext()).updateFavorite(0, item.id!!)
                    args.model.favorite = 0
                }
            }
        }

        binding!!.seedDetailBackBtn.setOnClickListener {
            navController.popBackStack()
        }

        binding.downLoad.setOnClickListener {
            FileUtilsSkins.askForPermission(requireContext(), binding.root) {
                if (it) {
                    val bitmapSkins =
                        FileUtilsSkins.getScreenShotFromView(binding.seedDetailSlider)
                    if (bitmapSkins != null) {
                        val filePth = FileUtilsSkins.saveMediaToStorageSkins(
                            requireContext(), "${System.currentTimeMillis()}_mods", bitmapSkins
                        )

                        showAds()
                    } else {
                        requireContext().toast("Image not downloaded.")
                    }
                }
            }

        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
            }


            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

    fun showAds() {
        RewardedAd.load(
            requireContext(),
            getString(R.string.admob_rewarded_valhiem),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString())
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })
    }
}