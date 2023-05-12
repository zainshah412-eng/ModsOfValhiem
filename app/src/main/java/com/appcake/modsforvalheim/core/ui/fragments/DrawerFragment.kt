package com.appcake.modsforvalheim.core.ui.fragments

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class DrawerFragment : androidx.fragment.app.Fragment(), android.view.View.OnClickListener {
    private lateinit var binding: com.appcake.modsforvalheim.databinding.FragmentDrawerBinding
    private lateinit var navController: androidx.navigation.NavController
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        // Inflate the layout for this fragment
        binding = com.appcake.modsforvalheim.databinding.FragmentDrawerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        adRequest = AdRequest.Builder().build()

        initView()
        addMob()
    }

    private fun initView() {
        binding.imgBtnExit.setOnClickListener(this)
        binding.seedsBtn.setOnClickListener(this)
        binding.cheatBtn.setOnClickListener(this)
        binding.toolsBtn.setOnClickListener(this)
        binding.keysBtn.setOnClickListener(this)
        binding.recipesBtn.setOnClickListener(this)
        binding.creaturesBtn.setOnClickListener(this)
        binding.commandsBtn.setOnClickListener(this)
        binding.guidesBtn.setOnClickListener(this)
        binding.buildsBtn.setOnClickListener(this)

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
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

    override fun onClick(p0: android.view.View?) {
        when (p0) {
            binding.imgBtnExit -> {
                (activity as MainActivity).showBottomNav()
                navController.popBackStack()
            }
            binding.seedsBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.seedsFragment)
            }
            binding.cheatBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.cheatFragment)
            }
            binding.toolsBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.toolFragment)
            }
            binding.keysBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.keysFragment)
            }
            binding.recipesBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.recipeFragment)
            }
            binding.creaturesBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.creatureFragment)
            }
            binding.commandsBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.commandsFragment)
            }
            binding.guidesBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.guideFragment)
            }
            binding.buildsBtn -> {
                showInterstitial()
                (activity as MainActivity).showBottomNav()
                navController.navigate(R.id.buildsFragment)
            }
        }
    }

    fun addMob() {
        InterstitialAd.load(
            requireContext(),
            getString(R.string.admob_interstitial_id_valhiem),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    private fun showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d(TAG, "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mInterstitialAd = null
                        addMob()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d(TAG, "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        mInterstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(TAG, "Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }
            mInterstitialAd?.show(requireActivity())
        } else {
            Toast.makeText(requireActivity(), "Ad wasn't loaded.", Toast.LENGTH_SHORT).show()
        }
    }

}