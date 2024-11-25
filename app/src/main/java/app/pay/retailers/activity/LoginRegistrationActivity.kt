package app.pay.retailers.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat

import androidx.navigation.fragment.NavHostFragment
import app.pay.retailers.BaseActivity
import app.pay.retailers.R
import app.pay.retailers.databinding.ActivityLoginRegisterationBinding
import app.pay.retailers.interfaces.OnBackPressedListner


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
            val dialog = builder.setMessage("Are You Sure You Want to Exit?")
                .setTitle("Exit!")
                .setNegativeButton("Exit") { _, _ ->
                    finishAffinity()
                }
                .setPositiveButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.create()
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(this, R.color.black))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(this, R.color.black))  } else {
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