package app.pay.pandapro.commonclass

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import app.pay.pandapro.R
import app.pay.pandapro.activity.AepsAllActions
import app.pay.pandapro.activity.AepsOnBoardingActivity
import app.pay.pandapro.activity.CashDepositActivity
import app.pay.pandapro.helperclasses.ShowDialog
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.responsemodels.aepsbank4.Bank4OnboardingResponse
import app.pay.pandapro.responsemodels.merchantOnBoarding.merchantOnBoardingResponse
import app.pay.pandapro.responsemodels.serviceStatus.CheckServiceStatusResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson

class ServiceChecker(
    private val context: Context,
    private val userSession: UserSession,
    private val myActivity: Activity
) {

    fun checkService(title: String, catId: String,selectedAepsType:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.isServiceAvailable(context, catId, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckServiceStatusResponse =
                    Gson().fromJson(message, CheckServiceStatusResponse::class.java)
                userSession.setBoolData(Constant.AEPS_ONBOARD_INSTENT,response.data.Aeps4.is_onboarding).toString()
                userSession.setBoolData(Constant.AEPS_ONBOARD,response.data.Aeps2.is_onboarding).toString()
                if (!response.error) {
                    val aeps2 = response.data.Aeps2

                    if (!response.data.is_buy) {
                        ShowDialog.bottomDialogSingleButton(myActivity, "Note",
                            "Please Purchase Package", "pending", object : MyClick {
                                override fun onClick() {}
                            })
                    } else if (!response.data.is_active) {
                        Toast.makeText(context, "This Service is Not Active For You", Toast.LENGTH_SHORT).show()
                    } else if (response.statusCode.equals("007")) {
                        openTransactionFilterDialog()
                    } else {
                       if(selectedAepsType=="Aeps 2"){
                            sendToAeps(response, catId, title,selectedAepsType)
                        } else {
                            sendToAepsInstentPay(response, catId, title,selectedAepsType)
                        }
                    }
                }
            }

            override fun fail(from: String) {
                Toast.makeText(context, "Unable To Fetch Services Status", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun
            sendToAeps(
        response: CheckServiceStatusResponse,
        catId: String,
        serviceName: String,
        selectedAepsType: String
    ) {

        if (!response.data.Aeps2.is_onboarding) {
            context.startActivity(
                Intent(context, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "1")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                    putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                }
            )
            userSession.setBoolData(Constant.AEPS_ONBOARD,response.data.Aeps2.is_onboarding).toString()
            (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.Aeps2.authRegistered) {
            context.startActivity(
                Intent(context, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "2")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                    putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                }
            )
            userSession.setBoolData(Constant.AEPS_ONBOARD,response.data.Aeps2.is_onboarding).toString()
            (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.Aeps2.bankActiveStatus.Bank2 && !response.data.Aeps2.bankActiveStatus.Bank3) {
            openSelectBankDialog()
        } else if (!response.data.Aeps2.dailyAuth) {
            context.startActivity(
                Intent(context, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "3")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                    putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                }
            )
            userSession.setBoolData(Constant.AEPS_ONBOARD,response.data.Aeps2.is_onboarding).toString()
            (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else {
            if (response.data.Aeps2.bankActiveStatus.Bank2 || response.data.Aeps2.bankActiveStatus.Bank3) {
                if (serviceName == "Aeps Cash Deposit") {
                    context.startActivity(
                        Intent(context, CashDepositActivity::class.java).apply {
                            putExtra("status", "4")
                            putExtra("title", serviceName)  // Sending catId along with the intent
                            putExtra("catId", catId)  // Sending catId along with the intent
                            putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                        }
                    )
                    (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                } else {
                    context.startActivity(
                        Intent(context, AepsAllActions::class.java)
                            .apply {
                                putExtra("status", "4")
                                putExtra("title", serviceName)  // Sending catId along with the intent
                                putExtra("catId", catId)  // Sending catId along with the intent
                                putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                            }
                    )
                    (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                }
            } else {
                context.startActivity(
                    Intent(context, AepsOnBoardingActivity::class.java).putExtra("status", "92")
                )
                (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
            }
        }
    }

    private fun sendToAepsInstentPay(
        response: CheckServiceStatusResponse,
        catId: String,
        serviceName: String,
        selectedAepsType: String
    ) {

        if (!response.data.Aeps4.is_onboarding) {


            context.startActivity(
                Intent(context, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "1")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)
                    putExtra("selectedAepsType", selectedAepsType) // Sending catId along with the intent
                }
            )

            (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else if (!response.data.Aeps4.authRegistered) {
            context.startActivity(
                Intent(context, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "2")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                    putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                }
            )

            (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

        } else if (!response.data.Aeps4.dailyAuth) {
            context.startActivity(
                Intent(context, AepsOnBoardingActivity::class.java).apply {
                    putExtra("status", "3")
                    putExtra("title", serviceName)  // Sending catId along with the intent
                    putExtra("catId", catId)  // Sending catId along with the intent
                    putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                }
            )

            (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        } else {

                if (serviceName == "Aeps Cash Deposit") {
                    context.startActivity(
                        Intent(context, CashDepositActivity::class.java).apply {
                            putExtra("status", "4")
                            putExtra("title", serviceName)  // Sending catId along with the intent
                            putExtra("catId", catId)  // Sending catId along with the intent
                            putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                        }
                    )

                    (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                } else {
                    context.startActivity(
                        Intent(context, AepsAllActions::class.java)
                            .apply {
                                putExtra("status", "4")
                                putExtra("title", serviceName)  // Sending catId along with the intent
                                putExtra("catId", catId)  // Sending catId along with the intent
                                putExtra("selectedAepsType", selectedAepsType)  // Sending catId along with the intent
                            }
                    )

                    (context as? Activity)?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                }

        }
    }

    private fun openSelectBankDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.lyt_aeps_chek_bank, null)

        // Create the dialog builder and set the custom view
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dialogView)
        }

        // Initialize views in the dialog
        val chkBank2: AppCompatCheckBox = dialogView.findViewById(R.id.chkBank2)
        val chkBank3: AppCompatCheckBox = dialogView.findViewById(R.id.chkBank3)
        val btnYes: AppCompatButton = dialogView.findViewById(R.id.btnYes)
        val btnNo: AppCompatButton = dialogView.findViewById(R.id.btnNo)

        // Handle checkbox selections
        chkBank2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                chkBank3.isChecked = false
            }
        }
        chkBank3.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
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

            // Process the selected banks
            submitSelectedBanks(selectedBanks)

            // Dismiss the dialog
            alertDialog.dismiss()
        }
    }

    private fun submitSelectedBanks(selectedBanks: List<String>) {
        if (selectedBanks.isEmpty()) {
            Toast.makeText(context, "No banks selected", Toast.LENGTH_LONG).show()
        } else {
            merchantOnBoarding(selectedBanks.joinToString())
        }
    }

    private fun merchantOnBoarding(selectedBank: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val mobile = userSession.getData(Constant.MOBILE).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["merchantcode"] = userSession.getData(Constant.MERCHANT_CODE).toString()
        requestData["mobile"] = mobile.substring(3)
        requestData["pipe"] = selectedBank

        UtilMethods.merchantOnBoarding(context, token, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: merchantOnBoardingResponse =
                    Gson().fromJson(message, merchantOnBoardingResponse::class.java)
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }

            override fun fail(from: String) {
                showToast(context, from)
            }
        })
    }

    private fun openTransactionFilterDialog() {
        // Your implementation for opening the transaction filter dialog
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
