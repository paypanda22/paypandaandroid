package app.pay.panda.fragments.home

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentCashBackBinding
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.retrofit.Constant


class CashBackFragment : BaseFragment<FragmentCashBackBinding>(FragmentCashBackBinding::inflate) {
    private lateinit var myActivity:FragmentActivity
    private lateinit var userSession: UserSession
    override fun init() {
        nullActivityCheck()

        userSession= UserSession(requireContext())
        val userType = userSession.getData(Constant.USER_TYPE)
        if (userType != "Super Distributor" && userType != "Distributor") {
            binding.llAepsWallet.visibility=View.GONE

        }
    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.rlNotifications.setOnClickListener { findNavController().navigate(R.id.action_global_notificationsFragment) }
        binding.llAllBalance.setOnClickListener {
            CommonClass.getDashBoardData(requireActivity(),userSession)
            binding.currBal.text=userSession.getData(Constant.MAIN_WALET).toString()
            binding.mainWallet.text=userSession.getData(Constant.MAIN_WALET).toString()
            binding.aepsWallet.text=userSession.getData(Constant.AEPS_WALLET) ?:"0.00"
            binding.cashBackWallet.text=userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
        }
        binding.rlBalance.setOnClickListener {
            if (binding.walletDetails.visibility == View.GONE) {
                binding.walletDetails.visibility = View.VISIBLE
                binding.walletDetails.animate().translationY(0F)
                binding.upArrow.visibility = View.VISIBLE
                binding.downArrow.visibility = View.GONE
            } else {
                binding.upArrow.visibility = View.GONE
                binding.downArrow.visibility = View.VISIBLE
                binding.walletDetails.visibility = View.GONE
                binding.walletDetails.animate().translationY(0F)

            }
        }
        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.action_global_requestWalletFragment) }

    }

    override fun setData() {
        binding.currBal.text=userSession.getData(Constant.MAIN_WALET).toString()
        binding.mainWallet.text=userSession.getData(Constant.MAIN_WALET).toString()
        binding.aepsWallet.text=userSession.getData(Constant.AEPS_WALLET) ?:"0.00"
        binding.cashBackWallet.text=userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
        binding.tvCart.text=userSession.getIntData(Constant.NOTIFICATION_COUNT).toString()
        MyGlide.with(
            requireContext(),
            Uri.parse(Constant.PIMAGE_URL + userSession.getData(Constant.PROFILE_PIC).toString()),
            binding.profileImage
        )
    }

}