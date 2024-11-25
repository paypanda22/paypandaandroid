package app.pay.retailers.fragments.home

import DynamicServicesAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R

import app.pay.retailers.activity.AepsAllActions
import app.pay.retailers.activity.AepsOnBoardingActivity
import app.pay.retailers.activity.AirtelCmsActivity
import app.pay.retailers.activity.BillPayment
import app.pay.retailers.activity.CashDepositActivity
import app.pay.retailers.activity.DashBoardActivity
import app.pay.retailers.activity.DmtActivity
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.activity.RechargeActivity
import app.pay.retailers.databinding.FragmentHomeBinding
import app.pay.retailers.databinding.LytDmtOtoBinding

import app.pay.retailers.helperclasses.Category
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.MyGlide
import app.pay.retailers.helperclasses.ResetTPIN
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.DynamicServicesClickListener
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.mAtm.MyAtmActivity
import app.pay.retailers.responsemodels.allservices.Data
import app.pay.retailers.responsemodels.count.NotificationCountResponse
import app.pay.retailers.responsemodels.distributerDashobord.DashboardResponse
import app.pay.retailers.responsemodels.dmtotp.DMTOtpResponse
import app.pay.retailers.responsemodels.merchantOnBoarding.merchantOnBoardingResponse
import app.pay.retailers.responsemodels.serviceStatus.CheckServiceStatusResponse
import app.pay.retailers.responsemodels.userid.UserIDResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import app.pay.retailers.sqlitehelper.CategoryDatabaseHelper


import com.google.gson.Gson


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    DynamicServicesClickListener {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var resetTPIN: ResetTPIN
    var name = ""
    override fun init() {


        nullActivityCheck()

        userSession = UserSession(requireContext())
        resetTPIN = ResetTPIN(myActivity, userSession)
        resetTPIN.resetTPin()
        val userType = userSession.getData(Constant.USER_TYPE_NAME)

        if (userType.equals("Super Distributor") || userType.equals("Distributor") || userType == "zsm" || userType == "asm") {
            binding.llAepsWallet.visibility = View.GONE
            binding.retailerDashboard.visibility = View.GONE
            binding.distributerDashboard.visibility = View.VISIBLE

        } else {
            binding.retailerDashboard.visibility = View.VISIBLE
            binding.distributerDashboard.visibility = View.GONE
        }
        val todayDate = CommonClass.getLiveTime("dd-MM-YYYY")
        distributerDashboard(todayDate)
        val name = userSession.getData(Constant.NAME)
        binding.tvCongratulations.text =
            "Congratulations! Dear $name, now you have become our partner with PayPanda."
        binding.tvCongratulations.isSelected = true // Enable marquee
        getUserDetail()
        allServices()
        summarynotification()


    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvCongratulations.isSelected = true // Ensure marquee is active

    }


    override fun addListeners() {

      binding.matm.setOnClickListener{
          startActivity(Intent(myActivity, MyAtmActivity::class.java) )
      }

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


        binding.llAddMoney.setOnClickListener { findNavController().navigate(R.id.action_global_requestWalletFragment) }
        binding.rlNotifications.setOnClickListener { findNavController().navigate(R.id.action_global_notificationsFragment) }


        binding.transactionStatus.setOnClickListener {
            findNavController().navigate(R.id.action_global_walletRequestListFragment2)

        }
        binding.newtwork.setOnClickListener{
            val userType = userSession.getData(Constant.USER_TYPE_NAME)

            if (userType == "zsm" || userType == "asm") {

                findNavController().navigate(R.id.FragmentZSMNetwork)
            }else{
                findNavController().navigate(R.id.FragmentNetwork)
            }
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
                val response: CheckServiceStatusResponse =
                    Gson().fromJson(message, CheckServiceStatusResponse::class.java)
                if (!response.error) {
                    userSession.setBoolData(Constant.AEPS_ONBOARD_INSTENT,response.data.Aeps4.is_onboarding).toString()
                    userSession.setBoolData(Constant.AEPS_ONBOARD,response.data.Aeps2.is_onboarding).toString()
                    userSession.setData(Constant.MERCHANT_CODE, response.data.merchantCode)
                    //val isInstantpayOnBoarded = response.data.Aeps4
                    val aeps2 = response.data.Aeps2
                    val aeps4 = response.data.Aeps4
                    if (!response.data.is_buy) {
                        ShowDialog.bottomDialogSingleButton(myActivity, "Note",
                            "Please Purchase Package", "pending", object : MyClick {
                                override fun onClick() {

                                }
                            })
                        // startActivity(Intent(activity, ActivationPackages::class.java))
                        //  activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                    } else if (!response.data.is_active) {
                        Toast.makeText(
                            requireContext(),
                            "This Service is Not Active For You",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (catId == "206" || catId == "207" || catId == "208") {
                            userSession.setData(Constant.MERCHANT_CODE, response.data.merchantCode)
                            sendToAeps(response, catId, title)
                        } else if (response.statusCode.equals("007")) {
                            openTransactionFilterDialog()

                        }else
                         {
                            ShowDialog.bottomDialogSingleButton(myActivity, "Message",
                             "something went wrong", "", object : MyClick {
                                    override fun onClick() {

                                    }
                                })
                            //  sendToServiceScreen(response, catId)
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(
                    requireContext(),
                    "Unable To Fetch Services Status",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun openSelectBankDialog() {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.lyt_aeps_chek_bank, null)

        // Create the dialog builder and set the custom view
        val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
        }

        // Initialize views in the dialog
        val chkBank2: AppCompatCheckBox = dialogView.findViewById(R.id.chkBank2)
        val chkBank3: AppCompatCheckBox = dialogView.findViewById(R.id.chkBank3)
        val btnYes: AppCompatButton = dialogView.findViewById(R.id.btnYes)
        val btnNo: AppCompatButton = dialogView.findViewById(R.id.btnNo)
        chkBank2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Uncheck the other checkbox
                chkBank3.isChecked = false
            }
        }

        chkBank3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Uncheck the other checkbox
                chkBank2.isChecked = false
            }
        }
        // Create and configure the alert dialog
        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false) // Prevent dialog from closing on outside touch

        // Show the dialog before applying full-screen adjustments
        alertDialog.show()

        // Make the dialog full screen
        alertDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Optional: Set transparent background if needed
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // Handle the 'Submit' button click event
        btnYes.setOnClickListener {
            val selectedBanks = mutableListOf<String>()
            if (chkBank2.isChecked) selectedBanks.add("Bank2")
            if (chkBank3.isChecked) selectedBanks.add("Bank3")

            // Do something with the selected banks
            submitSelectedBanks(selectedBanks)

            // Dismiss the dialog
            alertDialog.dismiss()
        }



        btnNo.setOnClickListener {
            startActivity(Intent(myActivity, DashBoardActivity::class.java))
            myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        // Handle the back press event
        alertDialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // Navigate to the dashboard or previous fragment/activity

                alertDialog.dismiss() // Dismiss the dialog when back is pressed
                true
            } else {
                false
            }
        }
    }

    // Function to handle navigation to the dashboard
    private fun navigateToDashboard() {
        startActivity(Intent(myActivity, DashBoardActivity::class.java))
        myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }




    // Function to handle the selected banks on submission
    private fun submitSelectedBanks(selectedBanks: List<String>) {
        if (selectedBanks.isEmpty()) {
            Toast.makeText(requireContext(), "No banks selected", Toast.LENGTH_LONG).show()
        } else {
            merchantOnBoarding(selectedBanks.joinToString())

            // Handle the selected banks (e.g., show a message or process the selection)
            //  Toast.makeText(requireContext(), "Selected Banks: ${selectedBanks.joinToString()}", Toast.LENGTH_SHORT).show()
        }
    }
    private fun merchantOnBoarding(selsectbank:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val mobile=userSession.getData(Constant.MOBILE).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["merchantcode"] = userSession.getData(Constant.MERCHANT_CODE).toString()
        requestData["mobile"]=mobile.substring(3)
        requestData["pipe"]=selsectbank

        UtilMethods.merchantOnBoarding(requireContext(),token ,requestData,object :
            MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: merchantOnBoardingResponse =
                    Gson().fromJson(message, merchantOnBoardingResponse::class.java)
                if (response.error == false) {
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
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
        dBinding.btnVerify.setOnClickListener {
            if (dBinding.etOtp.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please Enter Otp...", Toast.LENGTH_SHORT).show()
            } else {
                getOnboardingOtpValidate(dBinding.etOtp.text.toString())
                filterDialog.dismiss()
            }

        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    private fun getOnboardingOtpValidate(otp:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val mobile = userSession.getData(Constant.MOBILE).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["mobile"]=mobile.substring(3)
        requestData["user_id"]=token
        requestData["otp"]=otp
        UtilMethods.getOnboardingOtpValidate(requireContext(), token,requestData ,object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DMTOtpResponse =
                    Gson().fromJson(message, DMTOtpResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Congratulations!",
                        response.message.toString(),
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })

                }else{
                    showToast(requireContext(), response.message.toString())
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
            }
        })
    }

    private fun allServices() {
        val dbHelper = CategoryDatabaseHelper(myActivity)
        val categoryData = dbHelper.getAllCategories() // Fetch data from SQLite

        if (categoryData.isNotEmpty()) {
            // If data exists in SQLite, load it into the RecyclerView
            loadDataInRecyclerView(categoryData)
        } else {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.allServices(myActivity, token, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: app.pay.retailers.responsemodels.allservices.Category =
                        Gson().fromJson(
                            message,
                            app.pay.retailers.responsemodels.allservices.Category::class.java
                        )

                    // First check for a valid response without an error
                    if (!response.error) {
                        if (!response.data.isNullOrEmpty()) {
                            dbHelper.insertCategory(response.data)

                            // Load data into RecyclerView
                            loadDataInRecyclerView(response.data)

                            // Show recycler view if data exists
                            val adapter =
                                DynamicServicesAdapter(myActivity, response.data, this@HomeFragment)
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
                        Toast.makeText(myActivity, response.error.toString(), Toast.LENGTH_SHORT)
                            .show()
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
    }

    private fun loadDataInRecyclerView(dataList: List<app.pay.retailers.responsemodels.allservices.Data>) {
        val adapter = DynamicServicesAdapter(myActivity, dataList, this@HomeFragment)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(myActivity, 4)
        binding.llNoData.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }
    private fun sendToServiceScreen(response: CheckServiceStatusResponse, catId: String) {

    }

    private fun sendToAeps(
        response: CheckServiceStatusResponse,
        catId: String,
        serviceName: String
    ) {

        if (!response.data.Aeps2.is_onboarding) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "1")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                }
            )

            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.Aeps2.authRegistered) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "2")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                }
            )

            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }else if (!response.data.Aeps2.bankActiveStatus.Bank2 && !response.data.Aeps2.bankActiveStatus.Bank3) {
                openSelectBankDialog()


            } else if (!response.data.Aeps2.dailyAuth) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "3")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                }
            )
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else {
            if (response.data.Aeps2.bankActiveStatus.Bank2 || response.data.Aeps2.bankActiveStatus.Bank3) {
                if (serviceName == "Aeps Cash Deposit") {
                    startActivity(
                        Intent(
                            activity,
                            CashDepositActivity::class.java
                        ).apply {
                            putExtra("status", "4")
                            putExtra("title", serviceName)  // Sending catId along with the intent
                            putExtra("catId", catId)  // Sending catId along with the intent
                        }

                    )
                    activity?.overridePendingTransition(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                } else {
                    startActivity(
                        Intent(activity, AepsAllActions::class.java).apply {
                            putExtra("status", "4")
                            putExtra("title", serviceName)  // Sending catId along with the intent
                            putExtra("catId", catId)  // Sending catId along with the intent
                        }
                    )
                    activity?.overridePendingTransition(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }

            } else {
                startActivity(
                    Intent(
                        activity,
                        AepsOnBoardingActivity::class.java
                    ).putExtra("status", "92")
                )
                activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }

        }
    }

    private fun sendToAepsinstentPay(
        response: CheckServiceStatusResponse,
        catId: String,
        serviceName: String
    ) {
        if (!response.data.Aeps4.authRegistered) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
                    "status",
                    "2"
                )
            )
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.Aeps4.dailyAuth) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
                    "status",
                    "3"
                )
            )
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else {
            if (response.data.Aeps2.bankActiveStatus.Bank2 || response.data.Aeps2.bankActiveStatus.Bank3) {
                if (serviceName == "Aeps Cash Deposit") {
                    startActivity(
                        Intent(
                            activity,
                            CashDepositActivity::class.java
                        ).putExtra("status", "4")
                    )
                    activity?.overridePendingTransition(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                } else {
                    startActivity(
                        Intent(activity, AepsAllActions::class.java).putExtra(
                            "status",
                            "4"
                        ).putExtra("serviceName", serviceName)
                    )
                    activity?.overridePendingTransition(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }

            } else {
                startActivity(
                    Intent(
                        activity,
                        AepsOnBoardingActivity::class.java
                    ).putExtra("status", "92")
                )
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
    private fun summarynotification() {

        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.summarynotification(requireContext(),token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: NotificationCountResponse = Gson().fromJson(message,
                    NotificationCountResponse::class.java)
                if (!response.error) {
                  binding.tvCart.text= response.data.count.toString()
                    userSession.setIntData(Constant.NOTIFICATION_COUNT,response.data.count).toString()
                    }else{

                }
                }


            override fun fail(from: String) {

            }
        })

    }
    private fun distributerDashboard(date: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.distributerDashboard(requireContext(), token, date, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DashboardResponse =
                    Gson().fromJson(message, DashboardResponse::class.java)
                if (!response.error) {

                    binding.totalDebit.text = buildString {
                        append(response.data.wallet.debitTotal.toString())
                        append(" ₹")

                        binding.totalCredit.text = buildString {
                            append(response.data.wallet.creditTotal.toString())
                            append(" ₹")

                        }
                        binding.totaltxn.text = buildString {
                            append(response.data.dmtTotal.toString())
                            append(" ₹")

                        }
                        binding.disputeRequest.text = buildString {
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
                    userSession.setData(Constant.ID, response.data?._id.toString())
                        userSession.setData(Constant.LOCK_AMT,response.data.locking_amt.toString())

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
        processServices(serviceName, serviceId)


    }

    fun processServices(service: String, id: String) {
        when (id.trim()) {
            "206" -> {

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
                operatorList("Landline Bill Payment", Category.getIdByCategoryName("Landline Postpaid"))
                println("Processing: ${service}")
            }

            "11" -> {
                // Handle Piped Gas
                operatorList("Pipped Gas Billers", Category.getIdByCategoryName("Piped Gas"))
                println("Processing: ${service}")
            }

            "13" -> {
                // Handle Landline DTH
                startActivity(
                    Intent(myActivity, RechargeActivity::class.java).putExtra(
                        "dest",
                        "DTH"
                    )
                )
                activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                println("Processing: ${service}")
            }

            "5" -> {
                // Handle Landline Broadband Postpaid
                operatorList("Broadband Billers", Category.getIdByCategoryName("Broadband"))
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

                checkService("Aeps Bank Withdraw", "207")
                //println("Processing: ${service}")
            }

            "208" -> {

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
                operatorList("Quick Dhan", Category.getIdByCategoryName("Quick Dhan"))

            }

            "15" -> {
                operatorList("Handle Hospital and Pathology",Category.getIdByCategoryName("Hospital and Pathology"))
                // Handle Hospital and Pathology
                println("Processing: ${service}")
            }

            "14" -> {
                // Hospital
                operatorList("Hospital",Category.getIdByCategoryName("Hospital"))
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
                operatorList("LPG Gas", Category.getIdByCategoryName("LPG Gas"))
            }
            "29"->{
                //Metro Recharge
                operatorList("Metro Recharge", Category.getIdByCategoryName("Metro Recharge"))
            }
            "27"->{
                //Donation
                operatorList("Donation", Category.getIdByCategoryName("Donation"))
            }
            "22"->{
                //Life Insurance
                operatorList("Life Insurance", Category.getIdByCategoryName("Life Insurance"))
            }
            "216"->{
                //Recurring Deposit
                operatorList("Recurring Deposit", Category.getIdByCategoryName("Recurring Deposit"))
            }
            "24"->{
               // B2B
                operatorList("B2B", Category.getIdByCategoryName("B2B"))
            }
            "25"->{
                operatorList("Rent Payments", Category.getIdByCategoryName("Rental"))
            }
            "17"->{
                operatorList("Municipal Services", Category.getIdByCategoryName("Municipal Services"))
            }
            "23"->{
                operatorList("Mobile Prepaid", Category.getIdByCategoryName("Mobile Prepaid"))
            }
            "21"->{
                //Cable TV
                operatorList("Cable TV", Category.getIdByCategoryName("Cable TV"))
            }
            "6"->{
                //Subscription
                operatorList("Monthly Subscriptions", Category.getIdByCategoryName("Subscription"))
            }
            "30"->{
                //Clubs and Associations
                operatorList("Clubs and Associations", Category.getIdByCategoryName("Clubs and Associations"))
            }
            "7"->{
                //Municipal Taxes
                operatorList("Municipal Tax Payment", Category.getIdByCategoryName("Municipal Taxes"))
            }
            "2"->{
                //Loan
                operatorList("Loan", Category.getIdByCategoryName("Loan"))
            }
            "4"->{
                //Loan
                operatorList("Housing Society", Category.getIdByCategoryName("Housing Society"))
            }
            "31"->{
                //Loan
                operatorList("NCMC Recharge", Category.getIdByCategoryName("NCMC Recharge"))
            }
            "26"->{
            //Loan
            operatorList("Health Insurance", Category.getIdByCategoryName("Health Insurance"))
        }

            else -> {
               // showComingSoonPopup()
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

