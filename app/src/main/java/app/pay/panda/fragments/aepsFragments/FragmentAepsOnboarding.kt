package app.pay.panda.fragments.aepsFragments

import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentAepsOnboardingBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.retrofit.Constant

class FragmentAepsOnboarding :BaseFragment<FragmentAepsOnboardingBinding>(FragmentAepsOnboardingBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
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