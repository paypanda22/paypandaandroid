package app.pay.pandapro.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import app.pay.pandapro.BaseActivity
import app.pay.pandapro.R
import app.pay.pandapro.databinding.ActivityActivationPackagesBinding
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.OnBackPressedListner

class ActivationPackages : BaseActivity<ActivityActivationPackagesBinding>() {
    private lateinit var userSession: UserSession
    override fun getViewBinding(): ActivityActivationPackagesBinding = ActivityActivationPackagesBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession= UserSession(this@ActivationPackages)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment = navController.currentDestination
    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment=navController.currentDestination
        //val currentFragment=navHostFragment.childFragmentManager.fragments[0]
        if (currentFragment!!.id == R.id.packageListFragment) {
            startActivity(Intent(this@ActivationPackages, DashBoardActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        } else {
            if (currentFragment is OnBackPressedListner){
                currentFragment.backPressed()
            }else{
                navController.popBackStack()
            }

        }

        return true
    }

    override fun onClick(v: View?) {

    }

}