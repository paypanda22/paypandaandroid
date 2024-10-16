package app.pay.paypanda.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.pay.paypanda.fragments.supportTicket.AddSupportTicket
import app.pay.paypanda.fragments.supportTicket.SupportTicketListing

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AddSupportTicket()
            1->SupportTicketListing()
            else -> AddSupportTicket()
        }
    }
}