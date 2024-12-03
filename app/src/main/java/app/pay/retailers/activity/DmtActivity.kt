package app.pay.retailers.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import app.pay.retailers.BaseActivity
import app.pay.retailers.R
import app.pay.retailers.databinding.ActivityDmtBinding
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.OnBackPressedListner

class DmtActivity : BaseActivity<ActivityDmtBinding>() {
    private lateinit var userSession: UserSession
    private lateinit var navController: NavController

    override fun getViewBinding(): ActivityDmtBinding = ActivityDmtBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@DmtActivity)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_navs) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_navs) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment =  navHostFragment.childFragmentManager.primaryNavigationFragment
        //val currentFragment=navHostFragment.childFragmentManager.fragments[0]
        if (navController.currentDestination!!.id == R.id.DMTFragment2) {
            startActivity(Intent(this@DmtActivity, DashBoardActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        } else {
            if (currentFragment is OnBackPressedListner) {
                currentFragment.backPressed()
            } else {
                navController.popBackStack()
            }

        }
        return true
    }

    override fun onClick(v: View?) {

    }

}