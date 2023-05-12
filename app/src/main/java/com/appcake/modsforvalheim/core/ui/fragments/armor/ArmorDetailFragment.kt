package com.appcake.modsforvalheim.core.ui.fragments.armor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.Armor.SliderAdapterFullSize
import com.appcake.modsforvalheim.core.db.DbHelper
import com.appcake.modsforvalheim.core.model.SliderItem
import com.appcake.modsforvalheim.databinding.FragmentArmorDetailBinding
import com.appcake.modsforvalheim.utlis.FileUtilsSkins
import toast

import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.appcake.modsforvalheim.core.adapter.SliderAdapterFixSize
import com.example.dropbox_base.utils.Constantdropbox
import com.example.dropbox_base.utils.DropboxServices
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ArmorDetailFragment : Fragment() {
    val STORAGE_REQUEST_PERMISSION = 0

    lateinit var binding: FragmentArmorDetailBinding
    var sliderAdapter: SliderAdapterFixSize? = null
    private val args: ArmorDetailFragmentArgs by navArgs()
    private lateinit var navController: NavController
    var sliderList: ArrayList<SliderItem> = ArrayList()
    lateinit var adRequest: AdRequest
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"

    private companion object {
        //PERMISSION request constant, assign any value
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "PERMISSION_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArmorDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initView()

        MobileAds.initialize(requireContext()) {}
        adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
//
//        RequestConfiguration configuration =
//        RequestConfiguration(testDeviceIds: testDeviceIds);
//        MobileAds.instance.updateRequestConfiguration(configuration);
//        var configuration = RequestConfiguration()
//        configuration.setR = Arrays.asList("8C1DFF82CA4DE4D45F110242196FFDB3")

        showAds()
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
            sliderAdapter = SliderAdapterFixSize(requireContext(), sliderList)
            binding!!.seedDetailSlider.setSliderAdapter(sliderAdapter!!)
            binding!!.seedDetailSlider.startAutoCycle()
        } else {
            val imageName = args.model.imageId
            CoroutineScope(Dispatchers.IO).launch {
                val imgUrl: String =
                    DropboxServices.getInstance(requireContext())
                        .loadImageToView(
                            imageName!!,
                            Constantdropbox.ARMOR_IMAGE_JSON_PATH_DROPBOX
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
                    sliderAdapter = SliderAdapterFixSize(requireContext(), sliderList)
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
                            requireContext(), "${System.currentTimeMillis()}_armor", bitmapSkins
                        )

                        showRewardedVideo()
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
        if (rewardedAd == null) {

            var adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                requireActivity(),
                getString(R.string.admob_rewarded_valhiem),
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d(TAG, adError?.message)
                        //    isLoading = false
                        rewardedAd = null
                    }

                    override fun onAdLoaded(ad: RewardedAd) {
                        Log.d(TAG, "Ad was loaded.")
                        rewardedAd = ad
                        //     isLoading = false
                    }
                }
            )

//            var adRequest = AdRequest.Builder().build()
//            RewardedAd.load(
//                requireContext(),
//                getString(R.string.admob_rewarded_valhiem),
//                adRequest,
//                object : RewardedAdLoadCallback() {
//                    override fun onAdFailedToLoad(adError: LoadAdError) {
//                        Log.d(TAG, adError?.toString())
//                        rewardedAd = null
//                    }
//
//                    override fun onAdLoaded(ad: RewardedAd) {
//                        Log.d(TAG, "Ad was loaded.")
//                        rewardedAd = ad
//                    }
//                })
        }
    }

    private fun showRewardedVideo() {
        //   binding.showVideoButton.visibility = View.INVISIBLE
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null
                        showAds()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d(TAG, "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }

            rewardedAd?.show(
                requireActivity(),
                OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    //  addCoins(rewardAmount)
                    Log.d("TAG", "User earned the reward.")
                }
            )
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11(R) or above
            try {
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            } catch (e: Exception) {
                Log.e(TAG, "requestPermission: ", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            //Android is below 11(R)
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private val storageActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d(TAG, "storageActivityResultLauncher: ")
            //here we will handle the result of our intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //Android is 11(R) or above
                if (Environment.isExternalStorageManager()) {
                    //Manage External Storage Permission is granted
                    Log.d(
                        TAG,
                        "storageActivityResultLauncher: Manage External Storage Permission is granted"
                    )
                    downLoadImageFunction()
                } else {
                    //Manage External Storage Permission is denied....
                    Log.d(
                        TAG,
                        "storageActivityResultLauncher: Manage External Storage Permission is denied...."
                    )
                    requireContext().toast("Manage External Storage Permission is denied....")
                }
            } else {
                //Android is below 11(R)
            }
        }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        } else {
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                //check each permission if granted or not
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read) {
                    //External Storage Permission granted
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission granted")
                    downLoadImageFunction()
                } else {
                    //External Storage Permission denied...
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission denied...")
                    requireContext().toast("External Storage Permission denied...")
                }
            }
        }
    }

    fun downLoadImageFunction() {
        val bitmapSkins =
            FileUtilsSkins.getScreenShotFromView(binding.seedDetailSlider)
        if (bitmapSkins != null) {
            val filePth = FileUtilsSkins.saveMediaToStorageSkins(
                requireContext(), "${System.currentTimeMillis()}_skins", bitmapSkins
            )
        } else {
            requireContext().toast("Image not downloaded.")
        }
    }

}