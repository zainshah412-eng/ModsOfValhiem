package com.appcake.modsforvalheim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.appcake.modsforvalheim.core.ui.MainActivity
import com.appcake.modsforvalheim.core.ui.SubscriptionAct
import com.appcake.modsforvalheim.databinding.ActivitySplashAcitvityBinding
import com.appcake.modsforvalheim.databinding.FragmentArmorDetailBinding
import kotlinx.coroutines.*
import okhttp3.Dispatcher

class SplashActivity : AppCompatActivity() {
    private var coroutineScope: Job? = null
    private val NAVIGATION_DELAY = 1 * 1000L
    lateinit var binding: ActivitySplashAcitvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_acitvity)
        binding = com.appcake.modsforvalheim.databinding.ActivitySplashAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressOneFL.setImageResource(R.drawable.loadbar1)
        }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressOneFL.setImageResource(R.drawable.loadbar2)
        }, 2000)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressOneFL.setImageResource(R.drawable.loadbar3)
        }, 3000)
        Handler(Looper.getMainLooper()).postDelayed({
//            val intent = Intent(this, BottomNavMainActivity::class.java)
            if (AppApplication.sessionManager.subscriptionCheck) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 4000)

    }
}