package app.pay.panda.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.databinding.ActivityDmtBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.OnBackPressedListner

class DmtActivity : BaseActivity<ActivityDmtBinding>() {
    private lateinit var userSession: UserSession
    private lateinit var navController: NavController

    override fun getViewBinding(): ActivityDmtBinding =ActivityDmtBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession= UserSession(this@DmtActivity)
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
        val currentFragment=navController.currentDestination
        //val currentFragment=navHostFragment.childFragmentManager.fragments[0]
        if (currentFragment!!.id == R.id.DMTFragment2) {
            startActivity(Intent(this@DmtActivity, DashBoardActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        } else {
            navController.popBackStack()
        }
       return true
    }

    override fun onClick(v: View?) {

    }

}