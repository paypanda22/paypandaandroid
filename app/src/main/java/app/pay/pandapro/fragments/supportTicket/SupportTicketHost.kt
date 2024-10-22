package app.pay.pandapro.fragments.supportTicket

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.ViewPagerAdapter
import app.pay.pandapro.databinding.FragmentSupportTicketHostBinding
import app.pay.pandapro.helperclasses.UserSession
import com.google.android.material.tabs.TabLayoutMediator

class SupportTicketHost : BaseFragment<FragmentSupportTicketHostBinding>(FragmentSupportTicketHostBinding::inflate) {

    private lateinit var userSession: UserSession

    private lateinit var myActivity: FragmentActivity

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())

        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Add Ticket"
                1 -> "Support Ticket List"
                else -> "Support Ticket List"
            }
        }.attach()

        /*binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> binding.ivMenu.visibility = View.GONE
                    else -> binding.ivMenu.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
*/
    }
    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
      /*  binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }*/


    }

    override fun setData() {

    }


}
