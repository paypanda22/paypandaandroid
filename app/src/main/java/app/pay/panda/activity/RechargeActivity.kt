package app.pay.panda.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.databinding.ActivityRechargeBinding
import app.pay.panda.helperclasses.UserSession

class RechargeActivity : BaseActivity<ActivityRechargeBinding>() {
    private lateinit var userSession: UserSession
    private var active:Int=0
    private var dest=""
    private lateinit var navController: NavController

    override fun getViewBinding(): ActivityRechargeBinding =ActivityRechargeBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession= UserSession(this@RechargeActivity)
        dest=intent.getStringExtra("dest").toString()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if (dest=="mobile"){
            navController.navigate(R.id.mobileRechargeFragment)
            active=R.id.mobileRechargeFragment
        }else if (dest=="dth"){
            navController.navigate(R.id.dthRechargeFragment)
            active=R.id.dthRechargeFragment
        }else {

        }

    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if (active==R.id.mobileRechargeFragment || active==R.id.dthRechargeFragment){
            startActivity(Intent(this@RechargeActivity, DashBoardActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }else{
            navController.popBackStack()
        }
       return true
    }

    override fun onClick(v: View?) {

    }

}