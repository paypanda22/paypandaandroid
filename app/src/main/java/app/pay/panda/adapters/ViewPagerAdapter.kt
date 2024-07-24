package app.pay.panda.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.pay.panda.fragments.aepsFragments.AadhaarPay
import app.pay.panda.fragments.aepsFragments.AepsAuthenticationReg
import app.pay.panda.fragments.aepsFragments.AepsBe
import app.pay.panda.fragments.aepsFragments.AepsCw
import app.pay.panda.fragments.aepsFragments.AepsMainFragment
import app.pay.panda.fragments.aepsFragments.AepsMs
import app.pay.panda.fragments.aepsFragments.AepsTransactionList
import app.pay.panda.fragments.aepsFragments.FragmentAepsOnboarding
import app.pay.panda.fragments.aepsFragments.FragmentDailyAuth
import app.pay.panda.fragments.supportTicket.AddSupportTicket
import app.pay.panda.fragments.supportTicket.SupportTicketListing

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