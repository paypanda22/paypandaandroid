package app.pay.panda.fragments.home

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.ActivationPackages
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentProfileBinding
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.retrofit.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    override fun init() {
        userSession= UserSession(requireContext())
        nullActivityCheck()
        val userType = userSession.getData(Constant.USERTYPE)
        if (userType.equals("Super Distributor") || userType.equals("Distributor")) {
            binding.llAepsWallet.visibility=View.GONE
   binding.card3.visibility=View.GONE
   binding.card2.visibility=View.GONE
   binding.card5.visibility=View.GONE
   binding.card7.visibility=View.GONE
   binding.card10.visibility=View.GONE
   binding.packages.visibility=View.GONE
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

        binding.llAllBalance.setOnClickListener {
            CommonClass.getDashBoardData(requireActivity(),userSession)
            binding.currBal.text=userSession.getData(Constant.MAIN_WALET).toString()
            binding.mainWallet.text=userSession.getData(Constant.MAIN_WALET).toString()
            binding.aepsWallet.text=userSession.getData(Constant.AEPS_WALLET) ?:"0.00"
            binding.cashBackWallet.text=userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
        }

        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure You Want to Logout ?")
                .setTitle("Logout !")
                .setNegativeButton("No") { _, _ ->
                    null
                }
                .setPositiveButton("Logout") { _, _ ->
                    activity?.let { it1 -> userSession.logoutUser(it1) }
                }.show()
        }

        binding.card1.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_profileEditFragment)
        }

        binding.card7.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_dmtTransactionFragment)
        }
        binding.card3.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_walletTransactionReport)
        }
        binding.card9.setOnClickListener {
            findNavController().navigate(R.id.action_global_walletRequestListFragment2)
        }
        binding.card11.setOnClickListener{findNavController().navigate(R.id.action_profileFragment_to_companyBankAccountList)}

        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.action_global_requestWalletFragment) }

        binding.card12.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_supportTicketHost)
        }
        binding.card2.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_utilityTransactions)
        }
        binding.card13.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_fragment_fragment_network)
        }
        binding.earningreport.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_fragment_earning_report)
        }
        binding.packages.setOnClickListener {
            startActivity(Intent(activity, ActivationPackages::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }

    override fun setData() {
        MyGlide.with(requireContext(), Uri.parse(Constant.PIMAGE_URL+userSession.getData(Constant.PROFILE_PIC).toString()),binding.ivUserProfileImage);
        binding.tvMobileNumber.text=userSession.getData(Constant.MOBILE).toString()
        binding.tvEmail.text=userSession.getData(Constant.EMAIL).toString()
        binding.tvName.text=userSession.getData(Constant.NAME).toString()

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