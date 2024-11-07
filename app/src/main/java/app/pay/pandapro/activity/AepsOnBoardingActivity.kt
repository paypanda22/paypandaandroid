package app.pay.pandapro.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import app.pay.pandapro.BaseActivity
import app.pay.pandapro.R
import app.pay.pandapro.commonclass.ServiceChecker
import app.pay.pandapro.databinding.ActivityAepsOnBoardingBinding
import app.pay.pandapro.fragments.aepsFragments.AepsAuthenticationReg
import app.pay.pandapro.fragments.aepsFragments.FragmentAepsOnboarding
import app.pay.pandapro.fragments.aepsFragments.FragmentDailyAuth
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.OnBackPressedListner


class AepsOnBoardingActivity : BaseActivity<ActivityAepsOnBoardingBinding>() {
    private lateinit var userSession: UserSession
    private var status = 0
    private val bundle = Bundle()
    private lateinit var active:Fragment
    private val  aepsOnBoarding=FragmentAepsOnboarding()
    private val aepsAuthReg=AepsAuthenticationReg()
    private val aepsDailyAuth=FragmentDailyAuth()
  //  private val aepsMainFragment=AepsMainFragment()
    private val container=R.id.fragmentContainer
    private var selectedAepsType: String = ""
    var catId=""
    var title=""
    var bank="aeps2"
    override fun getViewBinding(): ActivityAepsOnBoardingBinding = ActivityAepsOnBoardingBinding.inflate(layoutInflater)
    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@AepsOnBoardingActivity)
        status = intent.getStringExtra("status").toString().toInt()
        catId = intent.getStringExtra("catId").toString()
        title = intent.getStringExtra("title").toString()
        selectedAepsType = intent.getStringExtra("selectedAepsType").toString()
        bundle.putString("status", status.toString())
        bundle.putString("catId", catId.toString())
        bundle.putString("title", title.toString())
        bundle.putString("selectedAepsType", selectedAepsType.toString())

        when (status) {
            1 -> {
                active = aepsOnBoarding
                aepsOnBoarding.arguments = bundle
                supportFragmentManager.beginTransaction().replace(container, aepsOnBoarding).commit()
            }

            2 -> {
                active = aepsAuthReg
                aepsAuthReg.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsAuthReg).commit()
            }

            3 -> {
                active = aepsDailyAuth
                aepsDailyAuth.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsDailyAuth)
                    .commit()
            }

            4 -> {
                startActivity(Intent(this@AepsOnBoardingActivity,AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "4")
                    putExtra("title", title)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                })
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }

            else -> {
                startActivity(Intent(this@AepsOnBoardingActivity,AepsAllActions::class.java).apply {
                    putExtra("status", "4")
                    putExtra("title", title)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                })
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
              /*  active=aepsMainFragment
                supportFragmentManager.beginTransaction().replace(container, aepsMainFragment).commit()*/
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