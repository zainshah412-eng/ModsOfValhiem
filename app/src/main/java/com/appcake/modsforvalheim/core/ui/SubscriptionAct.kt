package com.appcake.modsforvalheim.core.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.android.billingclient.api.*
import com.appcake.modsforvalheim.AppApplication
import com.appcake.modsforvalheim.R
import com.appcake.modsforvalheim.core.adapter.AdapterPagerValhiem
import com.appcake.modsforvalheim.core.adapter.SubscriptionSlider
import com.appcake.modsforvalheim.core.model.SubscriptionModel
import com.appcake.modsforvalheim.databinding.ActivitySubscriptionBinding
import com.appcake.modsforvalheim.utlis.ConstantsSkf
import com.appcake.modsforvalheim.utlis.SessionManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.*

class SubscriptionAct : AppCompatActivity() {
    lateinit var binding: ActivitySubscriptionBinding
    var sliderList: ArrayList<SubscriptionModel> = ArrayList()
    var sliderAdapter: SubscriptionSlider? = null
    var secondaryProgressStatus = 0
    var secondaryHandler: Handler? = Handler()

    private lateinit var purchasesUpdatedListenerSkf: com.android.billingclient.api.PurchasesUpdatedListener
    private lateinit var inAppBillingClientSkf: com.android.billingclient.api.BillingClient
    private lateinit var billingResultSkf: com.android.billingclient.api.BillingResult
    private lateinit var productDetails: com.android.billingclient.api.ProductDetails

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var priceBilling: String
    private var currentItemSkf: Int = 0
    lateinit var displayMetricsBilling: android.util.DisplayMetrics
    lateinit var stringsArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    fun initView() {
        displayMetricsBilling = android.util.DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetricsBilling)
        exoPlayer = ExoPlayer.Builder(this).build()
//        com.bumptech.glide.Glide.with(this@SubscriptionAct).asGif().load(R.raw.video)
//            .placeholder(R.drawable.ic_armor).into(ivContinue)

        initPlayerSkf()
        setUpProgressBar()

        initializeBillingClient()
        binding.trailBtn.setOnClickListener {
            inAppBillingClientSkf.startConnection(object :
                com.android.billingclient.api.BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: com.android.billingclient.api.BillingResult) {
                    if (billingResult.responseCode == com.android.billingclient.api.BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        getActiveProductDetails()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })
        }
        binding.cross.setOnClickListener {
            if (lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.RESUMED)) {
                stopPlayer()
                val intent5d2 =
                    android.content.Intent(this@SubscriptionAct, MainActivity::class.java)
                startActivity(intent5d2)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            //      ConstantsSkf.instanceSkf()?.adjustEventSkf(ConstantsSkf.ADJUST_EXIT_SUB_EVENT_SKF)

        }
        binding.privacy.setOnClickListener {
            try {
                val urlIntent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse(getString(R.string._privacy_link))
                )
                startActivity(urlIntent)
            } catch (e: Exception) {
                android.util.Log.e(getString(R.string.billing_tag), e.toString())
            }
        }
        binding.term.setOnClickListener {
            try {
                val urlIntent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse(getString(R.string.tos_link))
                )
                startActivity(urlIntent)
            } catch (e: Exception) {
                android.util.Log.e(getString(R.string.billing_tag), e.toString())
            }
        }
        binding.trailBtn.setOnClickListener {
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
            inAppBillingClientSkf.queryPurchasesAsync(
                params.build()
            ) { _, purchases ->
                if (purchases.size > 0) {
                    val intent = Intent(
                        this@SubscriptionAct,
                        MainActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    )
                    AppApplication.sessionManager.putSubscriptionSkf(true)

                } else {
                    //Need to do here
                    android.widget.Toast.makeText(
                        this@SubscriptionAct,
                        getString(R.string.offer_not_subscribed_yetvalheim),
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        stringsArray = Array(5){getString(R.string.subText1);getString(R.string.subText2);
            getString(R.string.subText3);getString(R.string.subText4);getString(R.string.subText5)}
        binding.viewpagerSilder.adapter =
            AdapterPagerValhiem(stringsArray)
        binding.viewpagerSilder.orientation =
            androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL

//        binding.viewpagerSilder.adapter =
//            AdapterPagerValhiem(resources.getStringArray(R.array.subscription_content))
//        binding.viewpagerSilder.orientation =
//            androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL

        binding.viewpagerSilder.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        dotOne()
                    }
                    1 -> {
                        dotTwo()
                    }
                    2 -> {
                        dotThree()
                    }
                    3 -> {
                        dotFour()
                    }
                    4 -> {
                        dotFive()
                    }
                }
            }
        })
        val handlerSkf0 = android.os.Handler()
        val updateSkf = Runnable {
            if (currentItemSkf == 5) {
                currentItemSkf = 0
            }
            binding.viewpagerSilder.setCurrentItem(currentItemSkf++, true)
        }
        java.util.Timer().schedule(object : java.util.TimerTask() {
            override fun run() {
                handlerSkf0.post(updateSkf)
            }
        }, 2000, 2000)


        sliderAdapter = SubscriptionSlider(this, sliderList)
        binding!!.slider.setSliderAdapter(sliderAdapter!!)
        binding!!.slider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        binding!!.slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding!!.slider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
        binding!!.slider.scrollTimeInSec = 4
        binding!!.slider.startAutoCycle()

        binding.privacy.text= (Html.fromHtml(getString(R.string.privacy_policy)));
        binding.term.text= (Html.fromHtml(getString(R.string.terms)));
    }


    private fun initPlayerSkf() {

        binding.exoPlayervalheim.player = exoPlayer
        val mediaItemBillSkf = createMediaItem()
        exoPlayer.addMediaItem(mediaItemBillSkf)
        exoPlayer.prepare()
        exoPlayer.play()
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
    }

    private fun createMediaItem(): MediaItem {

        val yInchesSkf0: Float = displayMetricsBilling.heightPixels / displayMetricsBilling.ydpi
        val xInchesSkf1: Float = displayMetricsBilling.widthPixels / displayMetricsBilling.xdpi
        val diagonalInchesSkf1 =
            kotlin.math.sqrt((xInchesSkf1 * xInchesSkf1 + yInchesSkf0 * yInchesSkf0).toDouble())
        val mediaUri: android.net.Uri = if (diagonalInchesSkf1 >= 7) {
            (android.net.Uri.parse(
                getString(R.string._androidResource) + packageName + getString(
                    R.string.backSlashvalheim
                ) + R.raw.video
            ))
            //For Tab Screens
        } else {
            (android.net.Uri.parse(
                getString(R.string._androidResource) + packageName + getString(
                    R.string.backSlashvalheim
                ) + R.raw.video
            ))
            //For Mobile Screens
        }
        return MediaItem.fromUri(mediaUri)
    }

    private fun stopPlayer() {
        try {
            exoPlayer.playWhenReady = false
            exoPlayer.stop()
            exoPlayer.seekTo(0)
        } catch (e: Exception) {
            android.util.Log.d("ErrorInExo", e.toString())
        }

    }

    private fun setUpProgressBar() {
        //for testing purposes time is reduced to only 5 sec and interval of 1. Also update it in XML file Note: 1 minute=60000 and 10 sec=10000
        checkForClose()
        val timer = object : android.os.CountDownTimer(60000, 10000) {
            override fun onTick(everyTik: Long) {
                val progressBill0 = 60000 - everyTik
                binding.progressBbar.progress = progressBill0.toInt()
            }

            override fun onFinish() {
                binding.progressBbar.progress = 60000
                binding.cross.visibility = android.view.View.VISIBLE

            }
        }
        timer.start()
    }

    private fun checkForClose() {
        if (AppApplication.sessionManager.subscriptionCheck) {
            binding.cross.visibility = android.view.View.VISIBLE
        } else {
            binding.cross.visibility = android.view.View.GONE
        }
    }


    private fun getActiveProductDetails() {
        val queryProductDetailsParams =
            com.android.billingclient.api.QueryProductDetailsParams.newBuilder()
                .setProductList(
                    com.google.firebase.crashlytics.internal.model.ImmutableList.from(
                        com.android.billingclient.api.QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(getString(R.string.product_id_subvalheim))
                            .setProductType(com.android.billingclient.api.BillingClient.ProductType.SUBS)
                            .build()
                    )
                )
                .build()

        inAppBillingClientSkf.queryProductDetailsAsync(queryProductDetailsParams) { billingResult,
                                                                                    productDetailsListSkf ->
            launchBillingSequence(productDetailsListSkf[0])
        }

    }

    private fun launchBillingSequence(productDetails: com.android.billingclient.api.ProductDetails) {
        val productDetailsParamsListBill = listOf(
            com.android.billingclient.api.BillingFlowParams.ProductDetailsParams.newBuilder()
                .setOfferToken(productDetails.subscriptionOfferDetails!![0].offerToken)
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = com.android.billingclient.api.BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsListBill)
            .build()

        this.productDetails = productDetails
        billingResultSkf =
            inAppBillingClientSkf.launchBillingFlow(this@SubscriptionAct, billingFlowParams)
        priceBilling =
            productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice
    }


    private fun initializeBillingClient() {

        //Purchase availabe or not if yes -> home no-> this screen
        purchasesUpdatedListenerSkf =
            com.android.billingclient.api.PurchasesUpdatedListener { billingResult0, purchasesSkf ->
                if (billingResult0.responseCode == com.android.billingclient.api.BillingClient.BillingResponseCode.OK && purchasesSkf != null) {
                    for (purchaseBillSkf in purchasesSkf) {
                        handlePurchase(purchaseBillSkf)
                    }
                } else if (billingResult0.responseCode == com.android.billingclient.api.BillingClient.BillingResponseCode.USER_CANCELED) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.canceledvalheim),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.errorvalheim),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        inAppBillingClientSkf =
            com.android.billingclient.api.BillingClient.newBuilder(this@SubscriptionAct)
                .setListener(purchasesUpdatedListenerSkf)
                .enablePendingPurchases()
                .build()
    }

    private fun handlePurchase(purchase: com.android.billingclient.api.Purchase) {

        if (purchase.purchaseState === com.android.billingclient.api.Purchase.PurchaseState.PURCHASED) {
            productDetails.let { productDetails ->
                AppApplication.sessionManager.putSubscriptionSkf(true)

                ConstantsSkf.logSubscriptionEvent(
                    productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceAmountMicros,
                    productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].priceCurrencyCode,
                    productDetails.productId,
                    purchase.orderId,
                    purchase.signature,
                    purchase.purchaseToken,
                    purchase.purchaseTime
                )
                priceBilling =
                    productDetails.subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice

            }
            ConstantsSkf.pushWooshTagsForSubscription(true)
            val intentvalheim0 =
                android.content.Intent(this@SubscriptionAct, MainActivity::class.java)
            startActivity(intentvalheim0)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        } else {
            AppApplication.sessionManager.putSubscriptionSkf(false)
            Toast.makeText(
                applicationContext,
                getString(R.string.some_thing_went_wrongvalheim),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun dotOne() {
        binding.circleOne.setImageDrawable(getDrawable(R.drawable.ellipse_filled))
        binding.circleTwo.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleThree.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFour.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFive.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
    }

    private fun dotTwo() {
        binding.circleOne.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleTwo.setImageDrawable(getDrawable(R.drawable.ellipse_filled))
        binding.circleThree.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFour.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFive.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
    }

    private fun dotThree() {
        binding.circleOne.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleTwo.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleThree.setImageDrawable(getDrawable(R.drawable.ellipse_filled))
        binding.circleFour.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFive.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
    }

    private fun dotFour() {
        binding.circleOne.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleTwo.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleThree.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFour.setImageDrawable(getDrawable(R.drawable.ellipse_filled))
        binding.circleFive.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
    }

    private fun dotFive() {
        binding.circleOne.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleTwo.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleThree.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFour.setImageDrawable(getDrawable(R.drawable.ellipse_empty))
        binding.circleFive.setImageDrawable(getDrawable(R.drawable.ellipse_filled))
    }

    override fun onResume() {
        super.onResume()
        initPlayerSkf()
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPlayer()
    }

    override fun onStart() {
        super.onStart()
        initPlayerSkf()
    }


    override fun onBackPressed() {
        if (AppApplication.sessionManager.subscriptionCheck) {
            super.onBackPressed()
            stopPlayer()
        } else {
            callForNotification()
        }
    }

    private fun callForNotification() {
//        val notificationHelperSkf = NotificationHelperSkf(this@SubscriptionAct)
//        notificationHelperSkf.notificationForDiscount()
        ConstantsSkf.adjustEvent(ConstantsSkf.ADJUST_NOTIFICATION_SHOWED_EVENT)
    }
}



