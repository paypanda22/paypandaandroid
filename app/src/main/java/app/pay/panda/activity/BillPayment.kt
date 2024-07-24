package app.pay.panda.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.databinding.ActivityTestBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.OnBackPressedListner

class BillPayment : BaseActivity<ActivityTestBinding>() {
    private lateinit var userSession: UserSession
    private var status=0
    private var  bundle=Bundle()
    private lateinit var service: String
    private lateinit var catID: String
    override fun getViewBinding(): ActivityTestBinding =ActivityTestBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession=UserSession(this@BillPayment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment = navController.currentDestination
        val fragmentId=currentFragment?.id

        status=intent.getStringExtra("status").toString().toInt()
        service=intent.getStringExtra("service").toString()
        catID=intent.getStringExtra("catID").toString()
        if (status==1){
            bundle.putString("service",service)
            bundle.putString("catID",catID)
            navController.navigate(R.id.operatorListFragment2,bundle)
        }


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
        if (currentFragment!!.id == R.id.operatorListFragment2) {
            startActivity(Intent(this@BillPayment, DashBoardActivity::class.java))
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