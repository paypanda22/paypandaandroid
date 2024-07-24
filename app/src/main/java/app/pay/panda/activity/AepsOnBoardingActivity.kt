package app.pay.panda.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.databinding.ActivityAepsOnBoardingBinding
import app.pay.panda.fragments.aepsFragments.AadhaarPay
import app.pay.panda.fragments.aepsFragments.AepsAuthenticationReg
import app.pay.panda.fragments.aepsFragments.AepsBe
import app.pay.panda.fragments.aepsFragments.AepsCw
import app.pay.panda.fragments.aepsFragments.AepsMainFragment
import app.pay.panda.fragments.aepsFragments.AepsMs
import app.pay.panda.fragments.aepsFragments.AepsTransactionList
import app.pay.panda.fragments.aepsFragments.FragmentAepsOnboarding
import app.pay.panda.fragments.aepsFragments.FragmentDailyAuth
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.OnBackPressedListner


class AepsOnBoardingActivity : BaseActivity<ActivityAepsOnBoardingBinding>() {
    private lateinit var userSession: UserSession
    private var status = 0
    private val bundle = Bundle()
    private lateinit var active:Fragment
    private val  aepsOnBoarding=FragmentAepsOnboarding()
    private val aepsAuthReg=AepsAuthenticationReg()
    private val aepsDailyAuth=FragmentDailyAuth()
    private val aepsMainFragment=AepsMainFragment()
    private val container=R.id.fragmentContainer

    override fun getViewBinding(): ActivityAepsOnBoardingBinding = ActivityAepsOnBoardingBinding.inflate(layoutInflater)
    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@AepsOnBoardingActivity)
        status = intent.getStringExtra("status").toString().toInt()
        bundle.putString("status", status.toString())

        when (status) {
            1 -> {
                active = aepsOnBoarding
                supportFragmentManager.beginTransaction().replace(container, aepsOnBoarding).commit()
            }

            2 -> {
                active = aepsAuthReg
                supportFragmentManager.beginTransaction().replace(container, aepsAuthReg).commit()
            }

            3 -> {
                active = aepsDailyAuth
                supportFragmentManager.beginTransaction().replace(container, aepsDailyAuth)
                    .commit()
            }

            4 -> {
                startActivity(Intent(this@AepsOnBoardingActivity,AepsOnBoardingActivity::class.java).putExtra("status","4"))
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }

            else -> {
                active=aepsMainFragment
                supportFragmentManager.beginTransaction().replace(container, aepsMainFragment).commit()
            }

        }

    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        // val active=supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (currentFragment == active) {
//            Log.e("TAG", "handleBackPressCustom: ")
            val builder = AlertDialog.Builder(this@AepsOnBoardingActivity)
            builder.setMessage("Go to DashBoard ?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { _, _ ->
                    startActivity(Intent(this@AepsOnBoardingActivity, DashBoardActivity::class.java))
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                }
                .setPositiveButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        } else {
            if (currentFragment is OnBackPressedListner) {
                currentFragment.backPressed()
            } else {
                startActivity(Intent(this@AepsOnBoardingActivity, DashBoardActivity::class.java))
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            }

        }
        return true
    }

    override fun onClick(v: View?) {

    }


}