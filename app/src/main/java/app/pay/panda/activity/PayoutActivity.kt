package app.pay.panda.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.databinding.ActivityPayoutBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.OnBackPressedListner

class PayoutActivity : BaseActivity<ActivityPayoutBinding>() {
    private lateinit var userSession: UserSession
    private var status=0
    private var  bundle=Bundle()

    override fun getViewBinding(): ActivityPayoutBinding =ActivityPayoutBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession=UserSession(this@PayoutActivity)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment = navController.currentDestination
        val fragmentId=currentFragment?.id
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
            val builder = AlertDialog.Builder(this@PayoutActivity)
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