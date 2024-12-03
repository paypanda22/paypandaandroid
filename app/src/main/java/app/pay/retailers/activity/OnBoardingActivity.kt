package app.pay.retailers.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import app.pay.retailers.BaseActivity
import app.pay.retailers.R
import app.pay.retailers.databinding.ActivityOnBoardingBinding


import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.dashBoardData.DashBoardData
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson

class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>() {
    private lateinit var userSession: UserSession
    override fun getViewBinding(): ActivityOnBoardingBinding = ActivityOnBoardingBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@OnBoardingActivity)
        getDashBoardData()
    }

    private fun getDashBoardData() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.dashBoardData(this@OnBoardingActivity, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DashBoardData = Gson().fromJson(message, DashBoardData::class.java)
                response.data?.let { userSession.setUserData(it) }
            }

            override fun fail(from: String) {
            }
        })
    }


    override fun addListeners() {

    }

    override fun setData() {

    }


    override fun handleBackPressCustom(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment = navController.currentDestination
        if (currentFragment!!.id == R.id.onboardingStatusFragment) {
            val builder = AlertDialog.Builder(this@OnBoardingActivity)
            builder.setMessage("Are You Sure You Want to Exit?")
                .setTitle("Exit !")
                .setNegativeButton("Logout") { dialog, which ->
                    userSession.logoutUser(this@OnBoardingActivity)
                }
                .setPositiveButton("Exit") { dialog, which ->
                    finishAffinity()
                }.show()
        }  else {
            navController.popBackStack()
        }
        return true
    }

    override fun onClick(v: View?) {

    }

}

