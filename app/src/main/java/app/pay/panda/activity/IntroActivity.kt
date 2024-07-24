package app.pay.panda.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import app.pay.panda.BaseActivity2

import app.pay.panda.databinding.ActivitySplashBinding
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.retrofit.Constant


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class IntroActivity : BaseActivity2<ActivitySplashBinding>() {
    private lateinit var userSession: UserSession
    override fun getViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@IntroActivity)
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            navigateToActivity()

        }
    }

    private fun navigateToActivity() {
        if (userSession.isUserLoggedIn()) {
            if (userSession.getBoolData(Constant.ISAPPROVED)) {
                startActivity(
                    Intent(this@IntroActivity, DashBoardActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            } else {
                startActivity(
                    Intent(this@IntroActivity, OnBoardingActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }

        } else {
            startActivity(
                Intent(this@IntroActivity, LoginRegistrationActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }

    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun handleBackPress(): Boolean {
        return true
    }


    override fun onClick(v: View?) {

    }


}