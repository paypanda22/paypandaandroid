package app.pay.retailers.activity

import android.app.AlertDialog
import android.os.Bundle

import android.view.View

import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import app.pay.retailers.BaseActivity
import app.pay.retailers.R
import app.pay.retailers.databinding.ActivityDashBoardBinding
import app.pay.retailers.helperclasses.UserSession

import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.OnBackPressedListner
import app.pay.retailers.responsemodels.dashBoardData.DashBoardData
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class DashBoardActivity : BaseActivity<ActivityDashBoardBinding>() {
    private lateinit var userSession: UserSession
    private lateinit var navController: NavController
    override fun getViewBinding(): ActivityDashBoardBinding = ActivityDashBoardBinding.inflate(layoutInflater)
    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@DashBoardActivity)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.chipNavigationBar.setItemSelected(R.id.dashboard, true)
        setUpBottomBar()
        getDashBoardData()

    }

    private fun getDashBoardData() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.dashBoardData(this@DashBoardActivity, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DashBoardData = Gson().fromJson(message, DashBoardData::class.java)
                response.data?.let { userSession.setUserData(it) }
            }

            override fun fail(from: String) {
            }
        })
    }
    private fun setUpBottomBar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.cashBackFragment, R.id.aepsHomeFragment, R.id.profileFragment -> binding.chipNavigationBar.visibility = View.VISIBLE
                else -> binding.chipNavigationBar.visibility = View.GONE
            }
        }
    }


    override fun addListeners() {
        val userType = userSession.getData(Constant.USER_TYPE_NAME)

        setMenuVisibility(userType.toString())

        binding.chipNavigationBar.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                when (id) {
                    R.id.dashboard -> {
                        navController.navigate(R.id.action_global_to_homeFragment)
                        binding.chipNavigationBar.setItemSelected(R.id.dashboard, true)
                    }

                    R.id.cashback -> {
                        if (userType != "Super Distributor" && userType != "Distributor" && userType!="zsm" && userType!="asm") {
                            navController.navigate(R.id.action_global_to_cashBackFragment)
                            binding.chipNavigationBar.setItemSelected(R.id.cashback, true)
                        }
                    }

                    R.id.aeps -> {
                        if (userType != "Super Distributor" && userType != "Distributor" && userType!="zsm" && userType!="asm") {
                            navController.navigate(R.id.action_global_to_aepsHomeFragment)
                            binding.chipNavigationBar.setItemSelected(R.id.aeps, true)
                        }
                    }

                    R.id.profile -> {
                        navController.navigate(R.id.action_global_to_profileFragment)
                        binding.chipNavigationBar.setItemSelected(R.id.profile, true)
                    }

                    else -> {
                        navController.navigate(R.id.action_global_to_homeFragment)
                        binding.chipNavigationBar.setItemSelected(R.id.dashboard, true)
                    }
                }
            }
        })
    }

    private fun setMenuVisibility(userType: String) {
        if (userType == "Super Distributor" || userType == "Distributor" || userType == "zsm" || userType == "asm") {
            binding.chipNavigationBar.findViewById<View>(R.id.cashback).visibility = View.GONE
            binding.chipNavigationBar.findViewById<View>(R.id.aeps).visibility = View.GONE
        } else {
            binding.chipNavigationBar.findViewById<View>(R.id.cashback).visibility = View.VISIBLE
            binding.chipNavigationBar.findViewById<View>(R.id.aeps).visibility = View.VISIBLE
        }
    }


    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val currentFragment=navController.currentDestination
        //val currentFragment=navHostFragment.childFragmentManager.fragments[0]
            if (currentFragment?.id == R.id.homeFragment ||
                currentFragment?.id == R.id.cashBackFragment ||
                currentFragment?.id == R.id.profileFragment ||
                currentFragment?.id == R.id.aepsHomeFragment) {
            val builder = AlertDialog.Builder(this@DashBoardActivity)
            builder.setMessage("Are You Sure You Want to Exit?")
                .setTitle("Exit !")
                .setNegativeButton("No") { dialog, which ->

                }
                .setPositiveButton("Exit") { dialog, which ->
                    finishAffinity()
                }.show()
        } else {
            if (currentFragment is OnBackPressedListner){
                currentFragment.backPressed();
            }else{
                navController.popBackStack()
            }

        }
        return true
    }

    override fun onClick(v: View?) {

    }

}