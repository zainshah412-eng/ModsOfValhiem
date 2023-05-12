package com.appcake.modsforvalheim.core.ui

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.appcake.modsforvalheim.AppApplication
import com.appcake.modsforvalheim.R
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import verifyPermissions


@AndroidEntryPoint
class MainActivity : androidx.appcompat.app.AppCompatActivity() {
    private lateinit var binding: com.appcake.modsforvalheim.databinding.ActivityMainBinding
    private var fragmentManager: androidx.fragment.app.FragmentManager? = null
    private lateinit var navController: androidx.navigation.NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var appCloseCheck: Boolean = false

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = com.appcake.modsforvalheim.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        MobileAds.initialize(
            this
        ) { }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyPermissions(this)
        }

    }


    private fun initView() {
        fragmentManager = supportFragmentManager
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as androidx.navigation.fragment.NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

    }

    fun showBottomNav(){
        binding.bottomNav.visibility = View.VISIBLE
    }

    fun hideBottomNav(){
        binding.bottomNav.visibility = View.GONE
        binding.bottomNav.uncheckAllItems()
    }

    fun showLoader() {
        binding.secondLoadingScreen.visibility = View.VISIBLE
        binding.mainLayout.visibility = View.GONE
        binding.progressOneFL.setImageResource(R.drawable.loadbar1)
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
            binding.secondLoadingScreen.visibility = View.GONE
            binding.mainLayout.visibility = View.VISIBLE
        }, 4000)
    }

    fun hideLoader() {
//        binding.secondLoadingScreen.visibility = View.GONE
//        binding.mainLayout.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showBottomNav()
    }

    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }

}