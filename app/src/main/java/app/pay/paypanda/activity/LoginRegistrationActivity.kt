package app.pay.paypanda.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View

import androidx.navigation.fragment.NavHostFragment
import app.pay.paypanda.BaseActivity
import app.pay.paypanda.R
import app.pay.paypanda.databinding.ActivityLoginRegisterationBinding
import app.pay.paypanda.interfaces.OnBackPressedListner


class LoginRegistrationActivity : BaseActivity<ActivityLoginRegisterationBinding>() {
    override fun getViewBinding(): ActivityLoginRegisterationBinding =ActivityLoginRegisterationBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {

    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_navs) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment=navController.currentDestination
       // val active=supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (currentFragment!!.id == R.id.loginFragment) {
            val builder = AlertDialog.Builder(this@LoginRegistrationActivity)
            builder.setMessage("Are You Sure You Want to Exit?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { dialog, which ->
                   finishAffinity()
                }
                .setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }.show()
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