package app.pay.pandapro.fragments

import android.annotation.SuppressLint
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import app.pay.pandapro.BaseBottomFragment
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.databinding.FragmentTermsAndConditionsBinding
import app.pay.pandapro.helperclasses.UserSession

class TermsAndConditions : BaseBottomFragment<FragmentTermsAndConditionsBinding>(FragmentTermsAndConditionsBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity

    @SuppressLint("SetJavaScriptEnabled")
    override fun init() {
        nullActivityCheck()
        userSession= UserSession(requireContext())
        binding.webView.settings.javaScriptEnabled = true
        // Load the HTML file
        binding.webView.loadUrl("file:///android_asset/termsacondiyon.html")

    }

    private fun nullActivityCheck() {
       if (activity!=null){
           myActivity=activity as FragmentActivity
       }else{
           startActivity(Intent(context,IntroActivity::class.java))
       }
    }

    override fun addListeners() {
        binding.ivCancel.setOnClickListener { dismiss() }
        binding.btnOk.setOnClickListener { dismiss() }

    }

    override fun setData() {

    }

}