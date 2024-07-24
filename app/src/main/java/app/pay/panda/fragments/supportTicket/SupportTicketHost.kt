package app.pay.panda.fragments.supportTicket

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.ViewPagerAdapter
import app.pay.panda.databinding.FragmentSupportTicketHostBinding
import app.pay.panda.helperclasses.UserSession
import com.google.android.material.tabs.TabLayoutMediator


class SupportTicketHost : BaseFragment<FragmentSupportTicketHostBinding>(FragmentSupportTicketHostBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    override fun init() {
       nullActivityCheck()
        userSession= UserSession(requireContext())

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter=adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Add Ticket"
                1->"Support Ticket List"
                else -> "Support Ticket List"
            }
        }.attach()
    }

    private fun nullActivityCheck() {
        if(activity !=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

    }

    override fun setData() {

    }

}