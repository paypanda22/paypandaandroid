package app.pay.pandapro.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.pay.pandapro.fragments.supportTicket.AddSupportTicket
import app.pay.pandapro.fragments.supportTicket.SupportTicketListing

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