package app.pay.pandapro.fragments.aepsFragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R

import app.pay.pandapro.activity.DashBoardActivity
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.commonclass.ServiceChecker
import app.pay.pandapro.databinding.FragmentAepsOnboardingBinding
import app.pay.pandapro.databinding.LytDmtOtoBinding
import app.pay.pandapro.helperclasses.ShowDialog
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.responsemodels.aepsbank4.Bank4OnboardingResponse
import app.pay.pandapro.responsemodels.dmtotp.DMTOtpResponse

import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson
import com.paysprint.onboardinglib.activities.HostActivity
import org.koin.android.scope.ScopeService

class FragmentAepsOnboarding :BaseFragment<FragmentAepsOnboardingBinding>(FragmentAepsOnboardingBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private var serviceChecker: ServiceChecker? = null
    private var selectedAepsType: String = ""
    var catId=""
    var title=""
    var bank="aeps2"
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //   openSelectBankDialog()
//        startActivity(Intent(myActivity, DashBoardActivity::class.java))
//        myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Handle the result from the intent
            data?.let {
                val status = it.getBooleanExtra("status", false)
                val response = it.getIntExtra("response", 0)
                val message = it.getStringExtra("message")


                val detailedResponse = "Status: $status\n\nResponse: $response\n\nMessage: $message"
                val messageView = TextView(requireContext()).apply {
                    text = message
                    setTextColor(Color.BLACK) // Set text color to black
                    setTypeface(null, Typeface.BOLD) // Set text to bold
                    setPadding(40, 20, 40, 20) // Optional: Adjust padding as needed
                    textSize = 16f // Optional: Set text size
                }
                // Display the detailed response using AlertDialog
                AlertDialog.Builder(requireContext())
                    .setMessage(message)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss() // This will close the dialog when "OK" is clicked
                    }
                    .show()
            }
        }
    }






    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        catId = arguments?.getString("catId").toString()
        title = arguments?.getString("title").toString()
        selectedAepsType = arguments?.getString("selectedAepsType").toString()
        when (selectedAepsType) {
            "Aeps 2" -> binding.radioGroupAepsType.check(R.id.aeps2)
            "Aeps 4" -> binding.radioGroupAepsType.check(R.id.aeps4)
        }
        serviceChecker = ServiceChecker(requireContext(), userSession, requireActivity())

        if(userSession.getBoolData(Constant.AEPS_ONBOARD)==true){
            binding.aeps2Onboad.setImageDrawable( ContextCompat.getDrawable(myActivity,R.drawable.iconcheck))
        }else {
            binding.aeps2Onboad.setImageDrawable( ContextCompat.getDrawable(myActivity,R.drawable.iconcross))
        }
        if(userSession.getBoolData(Constant.AEPS_ONBOARD_INSTENT)==true){
            binding.aeps4Onboad.setImageDrawable( ContextCompat.getDrawable(myActivity,R.drawable.iconcheck))
        }else{
            binding.aeps4Onboad.setImageDrawable( ContextCompat.getDrawable(myActivity,R.drawable.iconcross))
        }
        if(selectedAepsType== "Aeps 4"){
            bank="aeps4"
        }else {
            bank="aeps2"
        }
    }

    private fun nullActivityCheck() {
        if (activity!=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.btnProceed.setOnClickListener {
            if (selectedAepsType == "Aeps 2") {
                val mobile = userSession.getData(Constant.MOBILE).toString()
                val intent = Intent(myActivity, HostActivity::class.java)
                intent.putExtra("pId", "PS003227")
                intent.putExtra(
                    "pApiKey",
                    "UFMwMDMyMjc5MjdlNTE5MjRjMTFiOTI5NDkxOTQ2YjFkMDMwZTU0Mw=="
                )
                intent.putExtra("mCode", userSession.getData(Constant.MERCHANT_CODE).toString())
                intent.putExtra("mobile", mobile.substring(3))
                intent.putExtra("lat", userSession.getData(Constant.M_LAT).toString())
                intent.putExtra("pipe", "1")
                intent.putExtra("lng", userSession.getData(Constant.M_LONG).toString())
                intent.putExtra("firm", userSession.getData(Constant.BUSINESS_NAME).toString())
                intent.putExtra("email", userSession.getData(Constant.EMAIL).toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                val bundle: Bundle? = intent.extras
                if (bundle != null) {
                    for (key in bundle.keySet()) {
                        val value = bundle.get(key)
                        Log.e("IntentExtras", "Key: $key, Value: $value")
                    }
                } else {
                    Log.e("IntentExtras", "No extras found in the Intent")
                }

                startForResult.launch(intent)

            }else{
                bank4Onboarding()
            }
        }
        binding.radioGroupAepsType.setOnCheckedChangeListener { group, checkedId ->
            // Check which radio button was clicked
            when (checkedId) {
                R.id.aeps2 -> {
                    selectedAepsType = "Aeps 2"
                    // Aeps 2 is selected
                    serviceChecker?.checkService(title, catId,selectedAepsType)
                }

                R.id.aeps4 -> {
                    selectedAepsType = "Aeps 4"
                    // Aeps 4 is selected
                    serviceChecker?.checkService(title, catId,selectedAepsType)
                }
            }
        }
        binding.ivBack.setOnClickListener {
            val builder = AlertDialog.Builder(myActivity)
            builder.setMessage("Go to DashBoard ?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { _, _ ->
                    startActivity(Intent(myActivity, DashBoardActivity::class.java))
                    myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                .setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
    }

   /* private fun checkService( title: String,catId: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.isServiceAvailable(requireContext(), catId, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckServiceStatusResponse =
                    Gson().fromJson(message, CheckServiceStatusResponse::class.java)
                if (!response.error) {
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
                    } else if (response.statusCode.equals("007")) {
                        openTransactionFilterDialog()

                    } else {
                        if (binding.aeps2.isChecked) {
                            sendToAeps(response, catId, title)
                        } else {
                            sendToAepsinstentPay(response, catId, title)
                        }
                    }
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
    }*/
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

    private fun bank4Onboarding() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.bank4Onboarding(myActivity, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: Bank4OnboardingResponse =
                    Gson().fromJson(message, Bank4OnboardingResponse::class.java)
                if (!response.error) {
                    if(response.statusCode=="007"){
                        openTransactionFilterDialog()
                    }else{
                        Toast.makeText(myActivity, response.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }


                } else {
                    Toast.makeText(myActivity, response.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(myActivity, from.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
   /* private fun sendToAeps(
        response: CheckServiceStatusResponse,
        catId: String,
        serviceName: String
    ) {
        if (!response.data.Aeps2.is_onboarding) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
                    "status",
                    "1"
                )
            )
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.Aeps2.authRegistered) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
                    "status",
                    "2"
                )
            )
            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }else if (!response.data.Aeps2.bankActiveStatus.Bank2 && !response.data.Aeps2.bankActiveStatus.Bank3) {
            openSelectBankDialog()


        } else if (!response.data.Aeps2.dailyAuth) {
            startActivity(
                Intent(activity, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "1")
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
    }*/

   /* private fun openSelectBankDialog() {
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
    }*/
//    private fun submitSelectedBanks(selectedBanks: List<String>) {
//        if (selectedBanks.isEmpty()) {
//            Toast.makeText(requireContext(), "No banks selected", Toast.LENGTH_LONG).show()
//        } else {
//            merchantOnBoarding(selectedBanks.joinToString())
//
//            // Handle the selected banks (e.g., show a message or process the selection)
//            //  Toast.makeText(requireContext(), "Selected Banks: ${selectedBanks.joinToString()}", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun merchantOnBoarding(selsectbank:String) {
//        val token = userSession.getData(Constant.USER_TOKEN).toString()
//        val mobile=userSession.getData(Constant.MOBILE).toString()
//        val requestData = hashMapOf<String, Any?>()
//        requestData["merchantcode"] = userSession.getData(Constant.MERCHANT_CODE).toString()
//        requestData["mobile"]=mobile.substring(3)
//        requestData["pipe"]=selsectbank
//
//        UtilMethods.merchantOnBoarding(requireContext(),token ,requestData,object :
//            MCallBackResponse {
//            override fun success(from: String, message: String) {
//                val response: merchantOnBoardingResponse =
//                    Gson().fromJson(message, merchantOnBoardingResponse::class.java)
//                if (response.error == false) {
//                    Toast.makeText(
//                        requireContext(),
//                        response.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                } else {
//                    Toast.makeText(
//                        requireContext(),
//                        response.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//            }
//
//            override fun fail(from: String) {
//                showToast(requireContext(), from)
//            }
//        })
//    }
//        private fun sendToAepsinstentPay(
//        response: CheckServiceStatusResponse,
//        catId: String,
//        serviceName: String
//    ) {
//            if (!response.data.Aeps4.is_onboarding) {
//            startActivity(
//                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
//                    "status",
//                    "1"
//                )
//            )
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
//        }
//        else if (!response.data.Aeps4.authRegistered) {
//            startActivity(
//                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
//                    "status",
//                    "2"
//                )
//            )
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
//        } else if (!response.data.Aeps4.dailyAuth) {
//            startActivity(
//                Intent(activity, AepsOnBoardingActivity::class.java).putExtra(
//                    "status",
//                    "3"
//                )
//            )
//            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
//        } else {
//            if (response.data.Aeps2.bankActiveStatus.Bank2 || response.data.Aeps2.bankActiveStatus.Bank3) {
//                if (serviceName == "Aeps Cash Deposit") {
//                    startActivity(
//                        Intent(
//                            activity,
//                            CashDepositActivity::class.java
//                        ).putExtra("status", "4")
//                    )
//                    activity?.overridePendingTransition(
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
//                } else {
//                    startActivity(
//                        Intent(activity, AepsAllActions::class.java).putExtra(
//                            "status",
//                            "4"
//                        ).putExtra("serviceName", serviceName)
//                    )
//                    activity?.overridePendingTransition(
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
//                }
//
//            } else {
//                startActivity(
//                    Intent(
//                        activity,
//                        AepsOnBoardingActivity::class.java
//                    ).putExtra("status", "92")
//                )
//                activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
//            }
//
//        }
//    }
    override fun setData() {

        binding.tvName.text=userSession.getData(Constant.NAME).toString()
        binding.tvEmail.text=userSession.getData(Constant.EMAIL).toString()
        val mobile=userSession.getData(Constant.MOBILE).toString()
        binding.tvMobile.text=mobile.substring(3)
        binding.tvMerchaentCode.text=userSession.getData(Constant.MERCHANT_CODE)
        binding.tvLong.text=userSession.getData(Constant.M_LONG).toString()
        binding.tvLat.text=userSession.getData(Constant.M_LAT).toString()
        binding.tvBusinessName.text=userSession.getData(Constant.BUSINESS_NAME).toString()


    }
}