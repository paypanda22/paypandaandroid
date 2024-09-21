package app.pay.panda.fragments.home

import DynamicServicesAdapter
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import app.pay.panda.activity.RechargeActivity
import app.pay.panda.databinding.FragmentHomeBinding
import app.pay.panda.databinding.LytDmtOtoBinding
import app.pay.panda.helperclasses.Category
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.ResetTPIN
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.DynamicServicesClickListener
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.allservices.Data
import app.pay.panda.responsemodels.distributerDashobord.DashboardResponse
import app.pay.panda.responsemodels.dmtotp.DMTOtpResponse
import app.pay.panda.responsemodels.serviceStatus.CheckServiceStatusResponse
import app.pay.panda.responsemodels.userid.UserIDResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    DynamicServicesClickListener {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private lateinit var resetTPIN: ResetTPIN
var name=""
    override fun init() {


        nullActivityCheck()

        userSession = UserSession(requireContext())
        resetTPIN=ResetTPIN(myActivity,userSession)
        resetTPIN.resetTPin()
        val userType = userSession.getData(Constant.USER_TYPE_NAME)

        if (userType.equals("Super Distributor") || userType.equals("Distributor")) {
      binding.llAepsWallet.visibility=View.GONE
      binding.retailerDashboard.visibility=View.GONE
      binding.distributerDashboard.visibility=View.VISIBLE

        }else{
            binding.retailerDashboard.visibility=View.VISIBLE
            binding.distributerDashboard.visibility=View.GONE
        }
        distributerDashboard("3-8-2024")
        val name = userSession.getData(Constant.NAME)
        binding.tvCongratulations.text = "Congratulations! Dear $name, now you have become our partner with PayPanda."
        binding.tvCongratulations.isSelected = true // Enable marquee
        getUserDetail()
        allServices()
    }

    private fun nullActivityCheck() {
        if (activity != null){
            myActivity =activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }
    override fun onResume() {
        super.onResume()
        binding.tvCongratulations.isSelected = true // Ensure marquee is active


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


        /*binding.llCashDeposit.setOnClickListener {

            startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status","1"))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
//            startActivity(Intent(activity, CashDepositActivity::class.java).putExtra("status", "4"))
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            checkService("Aeps Cash Deposit", "206")
        }
        binding.llDmt.setOnClickListener {
            startActivity(Intent(activity, DmtActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
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
        binding.llGooglePlay.setOnClickListener {
            operatorList("Metro Card Recharge", Category.getIdByCategoryName("Metro Recharge"))
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
            operatorList("Life Insurance Corporation", Category.getIdByCategoryName("LIC"))
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
*/


        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.action_global_requestWalletFragment) }
        binding.rlNotifications.setOnClickListener { findNavController().navigate(R.id.action_global_notificationsFragment) }


        binding.transactionStatus.setOnClickListener{
            findNavController().navigate(R.id.action_global_walletRequestListFragment2)

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
                        ShowDialog.bottomDialogSingleButton(myActivity, "Note",
                            "Please Purchase Package", "pending", object : MyClick {
                                override fun onClick() {
                                    findNavController().popBackStack()
                                }
                            })
                       // startActivity(Intent(activity, ActivationPackages::class.java))
                      //  activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    } else if (!response.data.is_active) {
                        Toast.makeText(requireContext(), "This Service is Not Active For You", Toast.LENGTH_SHORT).show()
                    } else {
                        if (catId == "206"|| catId == "207" || catId == "208") {
                            userSession.setData(Constant.MERCHANT_CODE, response.data.merchantCode)
                            sendToAeps(response, catId, title)
                        } else if(response.statusCode.equals("007")){
                            openTransactionFilterDialog()

                        }else{
                            sendToServiceScreen(response, catId)
                        }

                    }
                } else {
                //    Toast.makeText(requireContext(), "Unable To Fetch Services Status", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Unable To Fetch Services Status", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun openTransactionFilterDialog() {
        val filterDialog: Dialog = Dialog(myActivity)
        val dBinding = LytDmtOtoBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.btnVerify.setOnClickListener{
            if(dBinding.etOtp.text.toString().isNotEmpty()){
                Toast.makeText(requireContext(), "Please Enter Otp...", Toast.LENGTH_SHORT).show()
            }else{
                getOnboardingOtpValidate(dBinding.etOtp.text.toString())
            }

        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }
    private fun getOnboardingOtpValidate(otp:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getOnboardingOtpValidate(requireContext(),userSession.getData(Constant.MOBILE).toString(),token, token,otp ,object : MCallBackResponse {
            override fun success(from: String, message: String) {
              //  CommonClass.hideKeyBoard(myActivity, binding.edtCustomerNumber)
                val response: DMTOtpResponse =
                    Gson().fromJson(message, DMTOtpResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Congratulations!",
                        "Bank3 OnBoarding Successful",
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })

                }else{
                    showToast(requireContext(), "Something went wrong")
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
            }
        })
    }
    private fun allServices() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.allServices(myActivity, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: app.pay.panda.responsemodels.allservices.Category =
                    Gson().fromJson(message, app.pay.panda.responsemodels.allservices.Category::class.java)

                // First check for a valid response without an error
                if (!response.error) {
                    if (!response.data.isNullOrEmpty()) {
                        // Show recycler view if data exists
                        val adapter = DynamicServicesAdapter(myActivity, response.data, this@HomeFragment)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = GridLayoutManager(myActivity, 4)
                        binding.llNoData.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    } else {
                        // If data is empty, show no data view
                        binding.llNoData.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }
                } else {
                    // Handle the case where there's an error in the response
                    Toast.makeText(myActivity, response.error.toString(), Toast.LENGTH_SHORT).show()
                    binding.llNoData.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }

            override fun fail(from: String) {
                // Handle the failure scenario
                Toast.makeText(myActivity, from, Toast.LENGTH_SHORT).show()
                binding.llNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
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

    private fun distributerDashboard(date: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.distributerDashboard(requireContext(), token,date, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DashboardResponse =
                    Gson().fromJson(message, DashboardResponse::class.java)
                if (!response.error) {

       binding.totalDebit.text= buildString {
           append(response.data.wallet.debitTotal.toString())
           append(" ₹")

           binding.totalCredit.text= buildString {
               append(response.data.wallet.creditTotal.toString())
               append(" ₹")

           }
           binding.totaltxn.text= buildString {
               append(response.data.dmtTotal.toString())
               append(" ₹")

           }
           binding.disputeRequest.text= buildString {
               append(response.data.disputeRequest.toString())


           }
       }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable To Fetch Services Status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun fail(from: String) {
               // Toast.makeText(requireContext(), "Unable To Fetch Services Status", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getUserDetail() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getUserDetail(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: UserIDResponse =
                    Gson().fromJson(message, UserIDResponse::class.java)
                if (!response.error) {
                    userSession.setData(Constant.ID,response.data?._id.toString())


                } else {
                    Toast.makeText(requireContext(), "Unable to Load Bank List", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Unable to Load Bank List", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        val serviceId = model[pos]._id
        val serviceName = model[pos].service_name

        println("Clicked Service ID: $serviceId, Service Name: $serviceName")  // Log for debugging
        processServices( serviceName,serviceId)


    }
    fun processServices(service:String,id:String) {
            when (id.trim()) {
                "206"->{
                  /*  startActivity(Intent(myActivity, AepsOnBoardingActivity::class.java).putExtra("status","1"))
                    myActivity.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)*/
//            startActivity(Intent(activity, CashDepositActivity::class.java).putExtra("status", "4"))
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    checkService("Aeps Cash Deposit", "206")
                }
                "19" -> {
                    // Handle Credit Card
                    operatorList("Credit Card Billers", Category.getIdByCategoryName("Credit Card"))
                    println("Processing: ${service}")
                }
                "18" -> {
                    // Handle Electricity
                    operatorList("Electricity Billers", Category.getIdByCategoryName("Electricity"))
                    println("Processing: ${service}")
                }
                "188" -> {
                    // Handle DMT
                    startActivity(Intent(myActivity, DmtActivity::class.java))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    println("Processing: ${service}")
                }
                "9" -> {
                    // Handle Fastag
                    operatorList("FasTag Recharge Billers", Category.getIdByCategoryName("Fastag"))
                    println("Processing: ${service}")
                }
                "12" -> {
                    // Handle Landline Postpaid
                    operatorList("Landline Bill Payment", Category.getIdByCategoryName("Landline"))
                    println("Processing: ${service}")
                }
                "11" -> {
                    // Handle Landline Gas
                    operatorList("Pipped Gas Billers", Category.getIdByCategoryName("Gas"))
                    println("Processing: ${service}")
                }
                "13" -> {
                    // Handle Landline DTH
                    startActivity(Intent(myActivity, RechargeActivity::class.java).putExtra("dest", "dth"))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    println("Processing: ${service}")
                }
                "5" -> {
                    // Handle Landline Broadband Postpaid
                    operatorList("Broadband Billers", "166")
                    println("Processing: ${service}")
                }
                "33" -> {
                    // Handle Recharge
                    startActivity(Intent(myActivity, RechargeActivity::class.java).putExtra("dest", "mobile"))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    println("Processing: ${service}")
                }
                "28" -> {
                    // Handle Water
                    operatorList("Water Tax Payment", Category.getIdByCategoryName("Water"))
                    println("Processing: ${service}")
                }
                "10" -> {
                    // Handle Mobile Postpaid
                    operatorList("Mobile Postpaid Billers", Category.getIdByCategoryName("Mobile Postpaid"))
                   // println("Processing: ${service}")
                }
                "207" -> {
                    // Handle Aeps Bank Withdraw
 /* startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status","1"))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)*/
//            startActivity(Intent(activity, CashDepositActivity::class.java).putExtra("status", "4"))
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    checkService("Aeps Bank Withdraw", "207")
                    //println("Processing: ${service}")
                }
                "208" -> {
               /*     startActivity(Intent(activity, AepsOnBoardingActivity::class.java).putExtra("status","1"))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)*/
//            startActivity(Intent(activity, CashDepositActivity::class.java).putExtra("status", "4"))
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    checkService("Aeps Adhaar pay", "208")
                    // Handle Aeps Adhaar pay

                   // println("Processing: ${service}")
                }
                "35" -> {
                    // Handle CMS
                    startActivity(Intent(myActivity, AirtelCmsActivity::class.java))
                    activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    println("Processing: ${service}")
                }
                "3" -> {
                    // Handle Education Fees
                    operatorList("Education Fees Billers", Category.getIdByCategoryName("Education"))
                    println("Processing: ${service}")
                }
                "36" -> {
                    // Handle Quick Dhan
                    println("Processing: ${service}")
                }
                "15" -> {
                    // Handle Hospital and Pathology
                    println("Processing: ${service}")
                }
                "31" -> {
                    // Handle NCMC Recharge
                    println("Processing: ${service}")
                }
                "8" -> {
                    // Handle Insurance
                    operatorList("Insurance Billers", Category.getIdByCategoryName("Insurance"))
                    println("Processing: ${service}")
                }
                "16" -> {
                    // Handle LPG Gas
                    println("Processing: ${service}")
                }
                else -> {
                    showComingSoonPopup()
                    // Handle default case
                  /*  operatorList("LPG Booking Billers", Category.getIdByCategoryName("LPG Gas"))
                    println("Processing: ${service} ")*/
                }

            }

        // Optional: Handle icon if present

        }
    private fun showComingSoonPopup() {
        val dialog = Dialog(myActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_coming_soon)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    }

