package com.appcake.modsforvalheim

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.appcake.modsforvalheim.core.db.DbHelper
import com.appcake.modsforvalheim.utlis.SessionManager
import com.google.android.gms.ads.MobileAds
import com.pushwoosh.Pushwoosh
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AppApplication : Application() {
    private val TAG = AppApplication::class.simpleName

    companion object {
        lateinit var instance: Application
        lateinit var dbHelper: DbHelper
        lateinit var sessionManager: SessionManager

    }

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        instance = this
        dbHelper = DbHelper(applicationContext)
        sessionManager = SessionManager(applicationContext)
        MobileAds.initialize(this) {}

//        Pushwoosh.getInstance().registerForPushNotifications()

        dbHelper.initDB()

        val appToken = "{YourAppToken}"
        val environment = AdjustConfig.ENVIRONMENT_SANDBOX
        val config = AdjustConfig(this, appToken, environment)
        Adjust.onCreate(config)
        registerActivityLifecycleCallbacks(AdjustLifecycleCallbacks())
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    class AdjustLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        }


        override fun onActivityStarted(activity: Activity) {

        }


        override fun onActivityResumed(activity: Activity) {

            Adjust.onResume()

        }


        override fun onActivityPaused(activity: Activity) {

            Adjust.onPause()

        }


        override fun onActivityStopped(activity: Activity) {

        }


        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }


        override fun onActivityDestroyed(activity: Activity) {

        }

    }


}