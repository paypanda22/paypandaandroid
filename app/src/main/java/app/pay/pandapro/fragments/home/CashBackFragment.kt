package app.pay.pandapro.fragments.home

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.ActivationPackages
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.databinding.FragmentCashBackBinding
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.MyGlide
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.retrofit.Constant


class CashBackFragment : BaseFragment<FragmentCashBackBinding>(FragmentCashBackBinding::inflate) {
    private lateinit var myActivity:FragmentActivity
    private lateinit var userSession: UserSession
    override fun init() {
        nullActivityCheck()

        userSession= UserSession(requireContext())
        val userType = userSession.getData(Constant.USER_TYPE_NAME)
        if (userType != "Super Distributor" && userType != "Distributor" && userType!="zsm" && userType!="asm") {
            binding.llAepsWallet.visibility=View.GONE

        }
        if (userType.equals("Super Distributor") || userType.equals("Distributor") || userType == "zsm" || userType == "asm") {
            binding.llAepsWallet.visibility=View.GONE
            binding.card3.visibility=View.GONE
            binding.card2.visibility=View.GONE
            binding.card5.visibility=View.GONE
            binding.card7.visibility=View.GONE
            binding.card10.visibility=View.GONE
            binding.packages.visibility=View.GONE
            binding.adharpay.visibility=View.GONE
            binding.aepsWalletReport.visibility=View.GONE
            binding.aepsPayoutReport.visibility=View.GONE
            binding.PackageHistoryReport.visibility=View.GONE
            binding.card10.visibility=View.GONE
        }
        if (userType.equals("Retailer")) {
            binding.earningreport.visibility=View.GONE
            binding.card13.visibility=View.GONE
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

//        binding.card1.setOnClickListener {
//            findNavController().navigate(R.id.action_fragment_cash_back_to_profileEditFragment)
//        }

        binding.card7.setOnClickListener{
            findNavController().navigate(R.id.dmtTransactionFragment)
        }
        binding.card3.setOnClickListener {
            findNavController().navigate(R.id.walletTransactionReport)
        }
        binding.card9.setOnClickListener {
            findNavController().navigate(R.id.walletRequestListFragment2)
        }
        binding.card11.setOnClickListener{findNavController().navigate(R.id.companyBankAccountList)}

        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.requestWalletFragment) }

    /*    binding.card12.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_cash_back_to_supportTicketHost)
        }*/
        binding.card2.setOnClickListener {
            findNavController().navigate(R.id.utilityTransactions)
        }
        binding.card13.setOnClickListener {
            findNavController().navigate(R.id.FragmentNetwork)
        }
        binding.earningreport.setOnClickListener {
            findNavController().navigate(R.id.FragmentEarningRepoat)
        }
        binding.packages.setOnClickListener {
            startActivity(Intent(activity, ActivationPackages::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        binding.card5.setOnClickListener{
            findNavController().navigate(R.id.AepsTransactionList)
        }
        binding.card10.setOnClickListener{
            findNavController().navigate(R.id.CMSTransactionReport)
        }
        binding.adharpay.setOnClickListener{
            findNavController().navigate(R.id.AadharPayReport)
        }
        binding.aepsWalletReport.setOnClickListener{
            findNavController().navigate(R.id.AepsWalletReport)
        }
        binding.aepsPayoutReport.setOnClickListener{
            findNavController().navigate(R.id.AepsPayoutReport)
        }
        binding.PackageHistoryReport.setOnClickListener{
            findNavController().navigate(R.id.PackageHistoryReport)
        }
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