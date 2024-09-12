package app.pay.panda.mAtm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.databinding.ActivityMatmBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.mAtmInit.MAtmTxnInitResponse
import app.pay.panda.retrofit.ApiMethods
import app.pay.panda.retrofit.Constant
import com.google.gson.Gson
import com.sdk.matmsdk.ui.MatmActivity
import com.sdk.matmsdk.utils.MATMOnFinishListener
import com.sdk.matmsdk.utils.MatmSdkConstants
import org.json.JSONObject


class MyAtmActivity : BaseActivity<ActivityMatmBinding>(), MATMOnFinishListener {
    private lateinit var userSession: UserSession
    private var deviceName = "morefun"
    private var txnType = 1

    override fun getViewBinding(): ActivityMatmBinding = ActivityMatmBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@MyAtmActivity)
        MatmSdkConstants.MATMOnFinishListener = this

    }


    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            handleBackPressCustom()
        }
        binding.rgSelectDevice.setOnCheckedChangeListener { group, checkedId ->
            deviceName = when (checkedId) {
                R.id.rbPax -> "pax"
                R.id.rbMoreFun -> "morefun"
                R.id.rbWiseasy -> "Wiseasy"
                R.id.rbA910 -> "A910"
                else -> "morefun"
            }
        }

        binding.rgSelectTxnType.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbCw) {
                binding.edtAmount.setBackgroundColor(ContextCompat.getColor(this@MyAtmActivity, R.color.white))
                binding.edtAmount.background = ContextCompat.getDrawable(this@MyAtmActivity, R.drawable.edit_text_round_bg_outline_padding)
                binding.edtAmount.isEnabled = true
                txnType = 1
            } else {
                binding.edtAmount.setBackgroundColor(ContextCompat.getColor(this@MyAtmActivity, R.color.bggrey))
                binding.edtAmount.background = ContextCompat.getDrawable(this@MyAtmActivity, R.drawable.edit_text_round_bg_outline_padding)
                binding.edtAmount.isEnabled = false
                txnType = 0
            }
        }

        binding.btnSubmit.setOnClickListener {
            if (validate()) {
                val initiateTxnModel = InitiateTxnModel(
                    "paypandaapi",
                    "android",
                    "PayPanda",
                    "31ce608004db0fec63e7f6232930bbee53b979b452e6dda5898e405df9d6ed4d018c119164bb2a3f903b725ed9950e61",
                    "67d624dd646168c5c1c87256210591b09c5b90d3b38b74cb5f33f50290fc1310372d9b2b142285e4f17188e22b48d9d1d335848907839d3ea989c143ffd53779",
                    deviceName,
                    deviceName,
                    userSession.getData(Constant.M_LAT).toString(),
                    "paypandaapi",
                    userSession.getData(Constant.M_LONG).toString(),
                    "",
                    "",
                    userSession.getData(Constant.BUSINESS_NAME).toString(),
                    binding.chkSkipReceipt.isChecked,
                    "#8bc43f",
                    getTxnAmount(),
                    txnType.toString(),
                    binding.edtMobile.text.toString(),
                    userSession.getData(Constant.NAME).toString()
                )
                mAtmInitiate(initiateTxnModel)

            }


        }

    }

    private fun getTxnAmount(): String {
        return if (txnType == 0) {
            "0"
        } else {
            binding.edtAmount.text.toString()
        }
    }

    private fun mAtmInitiate(dataModel: InitiateTxnModel) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        ApiMethods.mAtmInitiate(this@MyAtmActivity, token, dataModel, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: MAtmTxnInitResponse = Gson().fromJson(message, MAtmTxnInitResponse::class.java)
                if (!response.error) {
                    val themeColor = ThemeColor(buttonBgColor = "#8bc43f", buttonTextColor = "#FFFFFF")
                    val requestModel = DataModel(
                        transactionAmount = (binding.edtAmount.text ?: 0).toString().toInt(),
                        transactionType = txnType.toString(),
                        shop_name = userSession.getData(Constant.BUSINESS_NAME).toString(),
                        brand_name = "PayPanda",
                        applicationType = "android",
                        paramB = "",
                        paramC = "",
                        device_type = deviceName,
                        device_name = deviceName,
                        apiUserName = "paypandaapi",
                        user_mobile_number = binding.edtMobile.text.toString(),
                        userName = "SIDDHARTHBEHERA",
                        clientRefID = response.data.clientRef_id.toString(),
                        clientID = "31ce608004db0fec63e7f6232930bbee53b979b452e6dda5898e405df9d6ed4d018c119164bb2a3f903b725ed9950e61",
                        clientSecret = "67d624dd646168c5c1c87256210591b09c5b90d3b38b74cb5f33f50290fc1310372d9b2b142285e4f17188e22b48d9d1d335848907839d3ea989c143ffd53779",
                        loginID = "paypandaapi",
                        skipReceipt = binding.chkSkipReceipt.isChecked,
                        themeColor = themeColor
                    )
                    val request = Gson().toJson(requestModel)
                    Log.e("DATA", "success: $request" )
                    val intent = Intent(this@MyAtmActivity, MatmActivity::class.java)
                    intent.putExtra("data", request)
                    startActivity(intent)
                } else {
                    showToast(this@MyAtmActivity, "Unable to Start Transaction")
                }

            }

            override fun fail(from: String) {
                showToast(this@MyAtmActivity, from)
            }
        })

    }

    private fun validate(): Boolean {
        val mobileText = binding.edtMobile.text.toString()
        if (!ActivityExtensions.isValidMobile(mobileText)) {
            binding.edtMobile.error = "Enter a Valid Mobile"
            return false
        }

        if (txnType == 1) {
            val amountText = binding.edtAmount.text.toString()
            if (amountText.isEmpty()) {
                binding.edtAmount.error = "Enter valid Amount"
                return false
            }

            val amt = amountText.toInt()
            if (amt < 100 || amt > 10000) {
                binding.edtAmount.error = "Enter valid Amount"
                showToast(this@MyAtmActivity, "Amount Should be between 100 and 10000")
                return false
            }
        }

        return true
    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        startActivity(Intent(this@MyAtmActivity, DashBoardActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        return true
    }

    override fun onClick(v: View?) {

    }

    override fun onMatmSDKFinish(statusTxt: String?, clientRefID: String?, remainingAmount: String?, statusDesc: String?, jsonObject: JSONObject?) {
        showToast(this@MyAtmActivity, statusTxt)
//        showToast(this@MyAtmActivity, statusTxt + clientRefID + statusDesc + jsonObject)
//        Log.e( "transactionData", jsonObject.toString() )
        when (statusTxt) {
            "success" -> {
                ShowDialog.bottomDialogSingleButton(this@MyAtmActivity, "Transaction Successful", "Remaining Amount is " + (jsonObject?.get("REMAINING_AMOUNT")?.toString() ?: "0.0"), "success", object : MyClick {
                    override fun onClick() {
                        handleBackPressCustom()
                    }
                })
            }

            else -> {
                ShowDialog.bottomDialogSingleButton(this@MyAtmActivity, "Transaction Failed", "Try After Some Time", "error", object : MyClick {
                    override fun onClick() {
                        handleBackPressCustom()
                    }
                })
            }


        }

    }
}