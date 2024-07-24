package app.pay.panda.fragments.home

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.ActivationPackages
import app.pay.panda.activity.AepsAllActions
import app.pay.panda.activity.AepsOnBoardingActivity
import app.pay.panda.activity.AirtelCmsActivity
import app.pay.panda.activity.BillPayment
import app.pay.panda.activity.CashDepositActivity
import app.pay.panda.activity.DmtActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.activity.QrCollectionActivity
import app.pay.panda.activity.RechargeActivity
import app.pay.panda.databinding.FragmentHomeBinding
import app.pay.panda.helperclasses.Category
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.ResetTPIN
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.mAtm.MyAtmActivity
import app.pay.panda.responsemodels.serviceStatus.CheckServiceStatusResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private lateinit var resetTPIN: ResetTPIN
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        resetTPIN=ResetTPIN(myActivity,userSession)
        resetTPIN.resetTPin()
    }

    private fun nullActivityCheck() {
        if (activity != null){
            myActivity =activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }



    override fun addListeners() {
        binding.llAllBalance.setOnClickListener {
            CommonClass.getDashBoardData(requireActivity(), userSession)
            binding.currBal.text = userSession.getData(Constant.MAIN_WALET).toString()
            binding.mainWallet.text = userSession.getData(Constant.MAIN_WALET).toString()
            binding.aepsWallet.text = userSession.getData(Constant.AEPS_WALLET) ?: "0.00"
            binding.cashBackWallet.text = userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
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
        binding.llCashDeposit.setOnClickListener {

            startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status","1"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
//            startActivity(Intent(activity, CashDepositActivity::class.java).putExtra("status", "4"))
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            //checkService("Aeps Cash Deposit", "206")
        }
        binding.llDmt.setOnClickListener {
            startActivity(Intent(activity, DmtActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        binding.llBroadband.setOnClickListener {
            operatorList("Broadband Billers", "166")
        }

        binding.llElectricity.setOnClickListener {
            operatorList("Electricity Billers", Category.getIdByCategoryName("Electricity"))
        }
        binding.llLandline.setOnClickListener {
            operatorList("Landline Bill Payment", Category.getIdByCategoryName("Landline"))
        }
        binding.llPipedGas.setOnClickListener {
            operatorList("Pipped Gas Billers", Category.getIdByCategoryName("Gas"))
        }
        binding.llEmi.setOnClickListener {
            operatorList("EMI Payment Billers", Category.getIdByCategoryName("Loan"))
        }
        binding.llInsurance.setOnClickListener {
            operatorList("Insurance Billers", Category.getIdByCategoryName("Insurance"))
        }
        binding.llWaterTax.setOnClickListener {
            operatorList("Water Tax Payment", Category.getIdByCategoryName("Water"))
        }
        binding.llAepsLayout.setOnClickListener {
            checkService("AEPS", "206")
        }
        binding.llBroadband.setOnClickListener {
            operatorList("Broadband Bill Payment", Category.getIdByCategoryName("Broadband"))
        }
        binding.llEducation.setOnClickListener {
            operatorList("Education Fees Billers", Category.getIdByCategoryName("Education"))
        }
        binding.llPrepaid.setOnClickListener {
            startActivity(Intent(activity, RechargeActivity::class.java).putExtra("dest", "mobile"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        binding.llDthRecharge.setOnClickListener {
            startActivity(Intent(activity, RechargeActivity::class.java).putExtra("dest", "dth"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        binding.llFastag.setOnClickListener {
            operatorList("FasTag Recharge Billers", Category.getIdByCategoryName("Fastag"))
        }
        binding.llLpgBooking.setOnClickListener {
            operatorList("LPG Booking Billers", Category.getIdByCategoryName("LPG Gas"))
        }
        binding.llRentPayment.setOnClickListener {
            operatorList("Rent Payments", Category.getIdByCategoryName("Rental"))
        }

        binding.llCreditCardBill.setOnClickListener {
            operatorList("Credit Card Billers", Category.getIdByCategoryName("Credit Card"))
        }
        binding.llPostPaid.setOnClickListener {
            operatorList("Mobile Postpaid Billers", Category.getIdByCategoryName("Mobile Postpaid"))
        }
        binding.llMunicipalTax.setOnClickListener {
            operatorList("Municipal Tax Payment", Category.getIdByCategoryName("Municipal Taxes"))
        }
        binding.llRd.setOnClickListener {
            operatorList("Recurring Deposit Payment", Category.getIdByCategoryName("Recurring Deposit"))
        }
        binding.llMunicinalServices.setOnClickListener {
            operatorList("Municipal Services", Category.getIdByCategoryName("Municipal Services"))
        }
        binding.llHealthInsurance.setOnClickListener {
            operatorList("Health Insurance Billers", Category.getIdByCategoryName("Health Insurance"))
        }

        binding.llLic.setOnClickListener {
            //operatorList("Life Insurance Corporation", Category.getIdByCategoryName("LIC"))
        }
        binding.llDonation.setOnClickListener {
            operatorList("Donation Billers", Category.getIdByCategoryName("Donation"))
        }
        binding.llSubscription.setOnClickListener {
            operatorList("Monthly Subscriptions", Category.getIdByCategoryName("Subscription"))
        }
        binding.llClubs.setOnClickListener {
            operatorList("Clubs and Associations", Category.getIdByCategoryName("Clubs and Associations"))
        }
        binding.llGooglePlay.setOnClickListener {
            operatorList("Metro Card Recharge", Category.getIdByCategoryName("Metro Recharge"))
        }


        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.action_global_requestWalletFragment) }
        binding.rlNotifications.setOnClickListener { findNavController().navigate(R.id.action_global_notificationsFragment) }
        binding.llCms.setOnClickListener {
            startActivity(Intent(activity, AirtelCmsActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
        binding.lytQrCollection.setOnClickListener {
            startActivity(Intent(activity, QrCollectionActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        binding.microAtmLyt.setOnClickListener {
            startActivity(Intent(activity, MyAtmActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

    }


    private fun operatorList(serviceTitle: String, catID: String) {
        startActivity(
            Intent(activity, BillPayment::class.java)
                .putExtra("service", serviceTitle)
                .putExtra("catID", catID)
                .putExtra("status", "1")
        )
        activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

    }

    private fun checkService(title: String, catId: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.isServiceAvailable(requireContext(), catId, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckServiceStatusResponse = Gson().fromJson(message, CheckServiceStatusResponse::class.java)
                if (!response.error) {
                    if (!response.data.is_buy) {
                        startActivity(Intent(activity, ActivationPackages::class.java))
                        activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    } else if (!response.data.is_active) {
                        Toast.makeText(requireContext(), "This Service is Not Active For You", Toast.LENGTH_SHORT).show()
                    } else {
                        if (catId == "206") {
                            userSession.setData(Constant.MERCHANT_CODE, response.data.merchantCode)
                            sendToAeps(response, catId, title)
                        } else {
                            sendToServiceScreen(response, catId)
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "Unable To Fetch Services Status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Unable To Fetch Services Status", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendToServiceScreen(response: CheckServiceStatusResponse, catId: String) {

    }

    private fun sendToAeps(response: CheckServiceStatusResponse, catId: String, serviceName: String) {
        if (!response.data.isOnBoarded) {
            startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status", "1"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.authRegistered) {
            startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status", "2"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.dailyAuth) {
            startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status", "3"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else {
            if (response.data.bank2 || response.data.bank3) {
                if (serviceName == "Aeps Cash Deposit") {
                    startActivity(Intent(activity, CashDepositActivity::class.java).putExtra("status", "4"))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                } else {
                    startActivity(Intent(activity, AepsAllActions::class.java).putExtra("status", "4"))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                }

            } else {
                startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status", "92"))
                activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }

        }
    }

    override fun setData() {
        binding.currBal.text = userSession.getData(Constant.MAIN_WALET).toString()
        binding.mainWallet.text = userSession.getData(Constant.MAIN_WALET).toString()
        binding.aepsWallet.text = userSession.getData(Constant.AEPS_WALLET) ?: "0.00"
        binding.cashBackWallet.text = userSession.getData(Constant.CASHBACK_WALLET) ?: "0.00"
        binding.tvCart.text = userSession.getIntData(Constant.NOTIFICATION_COUNT).toString()
        MyGlide.with(
            requireContext(),
            Uri.parse(Constant.PIMAGE_URL + userSession.getData(Constant.PROFILE_PIC).toString()),
            binding.profileImage
        )

    }

}