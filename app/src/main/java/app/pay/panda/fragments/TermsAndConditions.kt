package app.pay.panda.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import app.pay.panda.BaseBottomFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentTermsAndConditionsBinding
import app.pay.panda.helperclasses.UserSession

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