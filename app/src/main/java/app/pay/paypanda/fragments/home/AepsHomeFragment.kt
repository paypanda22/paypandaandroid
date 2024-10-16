package app.pay.paypanda.fragments.home

import android.content.Intent
import android.net.Uri
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.activity.PayoutActivity
import app.pay.paypanda.databinding.FragmentAepsHomeBinding
import app.pay.paypanda.helperclasses.AepsWalletActions
import app.pay.paypanda.helperclasses.CommonClass
import app.pay.paypanda.helperclasses.CustomProgressBar
import app.pay.paypanda.helperclasses.MyGlide
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.retrofit.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AepsHomeFragment : BaseFragment<FragmentAepsHomeBinding>(FragmentAepsHomeBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private lateinit var wallet:AepsWalletActions
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        wallet= AepsWalletActions(myActivity,userSession)


    }

    private fun nullActivityCheck() {
        userSession= UserSession(requireContext())
        if (activity !=null){
            myActivity= activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
        val userType = userSession.getData(Constant.USER_TYPE_NAME)
        if (userType == "Super Distributor" || userType == "Distributor" || userType=="zsm" || userType=="asm") {
            binding.llAepsWallet.visibility=View.GONE

        }
    }

    override fun addListeners() {
        binding.llAllBalance.setOnClickListener {
            CommonClass.getDashBoardData(requireActivity(), userSession)
            binding.currBal.text = userSession.getData(Constant.MAIN_WALET).toString()
            binding.mainWallet.text = userSession.getData(Constant.MAIN_WALET).toString()
            binding.aepsWallet.text = userSession.getData(Constant.AEPS_WALLET) ?: "0.00"
            binding.cashBackWallet.text = userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
            binding.tvAepsBalance.text=userSession.getData(Constant.AEPS_WALLET) ?: "0.00"
        }
        binding.rlBalance.setOnClickListener {
            toggleBalanceLayout()
        }

        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.action_global_requestWalletFragment) }
        binding.rlNotifications.setOnClickListener { findNavController().navigate(R.id.action_global_notificationsFragment) }
        binding.ivMore.setOnClickListener { showPopMenu() }

        binding.rlTransferToBank.setOnClickListener {
            startActivity(Intent(activity,PayoutActivity::class.java))
        }
        binding.rlToWallet.setOnClickListener {
            wallet.openTransferToWalletDialog("wallet")
        }


    }

    private fun showPopMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.ivMore)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.aeps_menu, popupMenu.menu)
        enablePopupMenuIcons(popupMenu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.toWallet -> {
                    val progressBar = CustomProgressBar()
                    progressBar.showProgress(context)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        progressBar.hideProgress()
                    }
                    true
                }
                R.id.toBank -> {
                    // Handle action two click
                    true
                }
                R.id.viewAll->{

                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun toggleBalanceLayout() {
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

    override fun setData() {
        binding.currBal.text = userSession.getData(Constant.MAIN_WALET).toString()
        binding.mainWallet.text = userSession.getData(Constant.MAIN_WALET).toString()
        binding.aepsWallet.text = userSession.getData(Constant.AEPS_WALLET) ?: "0.00"
        binding.cashBackWallet.text = userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
        binding.tvAepsBalance.text=userSession.getData(Constant.AEPS_WALLET) ?: "0.00"
        binding.tvCart.text=userSession.getIntData(Constant.NOTIFICATION_COUNT).toString()
        MyGlide.with(
            requireContext(),
            Uri.parse(Constant.PIMAGE_URL + userSession.getData(Constant.PROFILE_PIC).toString()),
            binding.profileImage
        )
    }
    private fun enablePopupMenuIcons(popupMenu: PopupMenu) {
        try {
            val field = PopupMenu::class.java.getDeclaredField("mPopup")
            field.isAccessible = true
            val menuPopupHelper = field.get(popupMenu)
            menuPopupHelper.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menuPopupHelper, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}