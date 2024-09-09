package app.pay.panda.fragments.aepsFragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentAepsOnboardingBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.retrofit.Constant
import com.paysprint.onboardinglib.activities.HostActivity

class FragmentAepsOnboarding :BaseFragment<FragmentAepsOnboardingBinding>(FragmentAepsOnboardingBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        startActivity(Intent(myActivity, DashBoardActivity::class.java))
        myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
//        if (result.resultCode == Activity.RESULT_OK) {
//            val data = result.data
//            // Process the result here
//            val resultData = data?.getStringExtra("resultKey") // Replace "resultKey" with the actual key
//            // Handle the result data
//        } else if (result.resultCode == Activity.RESULT_CANCELED) {
//            // Handle the cancellation
//        }
    }
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
    }

    private fun nullActivityCheck() {
        if (activity!=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.btnProceed.setOnClickListener {
            val mobile=userSession.getData(Constant.MOBILE).toString()
            val intent = Intent(myActivity, HostActivity::class.java)
            intent.putExtra("pId", "PS003227")
            intent.putExtra("pApiKey", "UFMwMDMyMjc5MjdlNTE5MjRjMTFiOTI5NDkxOTQ2YjFkMDMwZTU0Mw==")
            intent.putExtra("mCode", userSession.getData(Constant.MERCHANT_CODE).toString())
            intent.putExtra("mobile",mobile.substring(3))
            intent.putExtra("lat", userSession.getData(Constant.M_LAT).toString())
            intent.putExtra("pipe", "1")
            intent.putExtra("lng", userSession.getData(Constant.M_LONG).toString())
            intent.putExtra("firm", userSession.getData(Constant.BUSINESS_NAME).toString())
            intent.putExtra("email", userSession.getData(Constant.EMAIL).toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            val bundle: Bundle? = intent.extras
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    val value = bundle.get(key)
                    Log.e("IntentExtras", "Key: $key, Value: $value")
                }
            } else {
                Log.e("IntentExtras", "No extras found in the Intent")
            }
            startForResult.launch(intent)

        }
        binding.ivBack.setOnClickListener {
            val builder = AlertDialog.Builder(myActivity)
            builder.setMessage("Go to DashBoard ?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { _, _ ->
                    startActivity(Intent(myActivity, DashBoardActivity::class.java))
                    myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                .setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
    }

    override fun setData() {
        binding.tvName.text=userSession.getData(Constant.NAME).toString()
        binding.tvEmail.text=userSession.getData(Constant.EMAIL).toString()
        val mobile=userSession.getData(Constant.MOBILE).toString()
        binding.tvMobile.text=mobile.substring(3)
        binding.tvMerchaentCode.text=userSession.getData(Constant.MERCHANT_CODE)
        binding.tvLong.text=userSession.getData(Constant.M_LONG).toString()
        binding.tvLat.text=userSession.getData(Constant.M_LAT).toString()
        binding.tvBusinessName.text=userSession.getData(Constant.BUSINESS_NAME).toString()
    }
}