package app.pay.paypanda.helperclasses

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import app.pay.paypanda.R
import app.pay.paypanda.activity.DashBoardActivity
import app.pay.paypanda.databinding.LytTransferFromAepsToMainWalletBinding
import app.pay.paypanda.helperclasses.Utils.Companion.showToast
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.MyClick
import app.pay.paypanda.responsemodels.general.GeneralResponse
import app.pay.paypanda.responsemodels.payoutTxn.PayoutTransactionResponse
import app.pay.paypanda.retrofit.ApiMethods
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson

class AepsWalletActions(
    private val myActivity: Activity,
    private val userSession: UserSession
) {
    private var transferMode = "PA"
    fun openTransferToWalletDialog(type: String, beneID: String? = null,bankifsc: String? = null,bankaccountname: String? = null,bankname: String? = null,bankaccountnumber: String? = null,mobilenumber: String? = null,) {
        val toWalletDialog: Dialog = Dialog(myActivity)
        val dBinding = LytTransferFromAepsToMainWalletBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        toWalletDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        toWalletDialog.setContentView(dBinding.root)
        toWalletDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        toWalletDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toWalletDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        toWalletDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.edtAmount.requestFocus()
        val balance = userSession.getData(Constant.AEPS_WALLET).toString().toDouble()
        dBinding.tvAepsBalance.text = balance.toString()
        var text = ""
        var text2 = ""
        if (type == "wallet") {
            text = getString(myActivity, R.string.aeps_wallet_to_main_wallet)
            text2 = getString(myActivity, R.string.transfer_aeps_wallet_amount_to_main_wallet)
            dBinding.llTransferMode.visibility = GONE
        } else {
            text = getString(myActivity, R.string.aeps_wallet_to_Bank)
            text2 = getString(myActivity, R.string.transfer_aeps_wallet_amount_to_bank)
            dBinding.llTransferMode.visibility = VISIBLE
        }
        dBinding.textView18.text = text
        dBinding.textView19.text = text2
        dBinding.rgMode.setOnCheckedChangeListener { group, checkedId ->
            transferMode = when (checkedId) {
                R.id.rbIMPS -> {
                    "PA"
                }

                R.id.rbNEFT -> {
                    "NE"
                }

                R.id.rbRTGS -> {
                    "RT"
                }

                else -> {
                    "PA"
                }
            }
        }

        toWalletDialog.setCancelable(true)
        toWalletDialog.show()


        dBinding.btnSubmit.setOnClickListener {
            val amt = dBinding.edtAmount.text.toString().toInt()
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            if (dBinding.edtOtpMobile.text.toString().length < 4) {
                showToast(myActivity, "Enter Your Transaction PIN")
            } else if (dBinding.edtAmount.text.toString().isEmpty()) {
                dBinding.edtAmount.error = "Enter Amount to Transfer"
            } else if (amt < 100 || amt > 100000) {
                dBinding.edtAmount.error = "Minimum Amount Should be hundred"
                }else {
                if (amt > balance) {
                    dBinding.edtAmount.error = "Insufficient Balance"
                } else {
                    if (type == "wallet") {
                        processTransfer(dBinding, toWalletDialog)
                    } else {
                        if (beneID != null) {
                            val requestData = hashMapOf<String, Any?>()
                            requestData["user_id"] =  token
                            requestData["amount"] = dBinding.edtAmount.text.toString()
                            requestData["transId"] = beneID
                            requestData["tpin"] = dBinding.edtOtpMobile.text.toString()
                            requestData["paymentMode"] = transferMode
                            requestData["account_holder_name"] = bankaccountname
                            requestData["account_number"] = bankaccountnumber
                            requestData["bank_name"] = bankname
                            requestData["ifsc_code"] = bankifsc
                            requestData["mobile_number"] = mobilenumber

                            val encodedData = AesEncryptOld.encodeObj(requestData)
                            processBankTransfer(encodedData, toWalletDialog)
                        } else {
                            showToast(myActivity, "No Beneficiary ID Provided")
                        }
                    }

                }

            }
        }
    }

    private fun processBankTransfer(obj: Any, toWalletDialog: Dialog) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.doPayoutTransaction(myActivity, token, obj, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PayoutTransactionResponse = Gson().fromJson(message, PayoutTransactionResponse::class.java)
                if (!response.error) {
                    toWalletDialog.dismiss()
                    ShowDialog.bottomDialogSingleButton(myActivity, "Transaction Successfully Done", response.message, "success", object : MyClick {
                        override fun onClick() {
                            myActivity.startActivity(Intent(myActivity, DashBoardActivity::class.java))
                            myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
                        }
                    })
                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity, "ERROR",
                        response.message, "error", object : MyClick {
                            override fun onClick() {
                                toWalletDialog.dismiss()
                            }
                        })
                    //showToast(myActivity, response.message)
                   // toWalletDialog.dismiss()
                }

            }

            override fun fail(from: String) {
                showToast(myActivity, from)
                toWalletDialog.dismiss()
            }
        })


    }

    private fun processTransfer(toWalletDialogBinding: LytTransferFromAepsToMainWalletBinding, toWalletDialog: Dialog) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["amount"] = toWalletDialogBinding.edtAmount.text.toString()
        ApiMethods.transferAepsToMain(myActivity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: GeneralResponse = Gson().fromJson(message, GeneralResponse::class.java)
                if (response.error == false) {
                    toWalletDialog.dismiss()
                    response.message?.let {
                        ShowDialog.bottomDialogSingleButton(myActivity, "Transaction Successfully Completed", it, "success", object : MyClick {
                            override fun onClick() {

                            }
                        })
                    }
                } else {
                    response.message?.let { showToast(myActivity, it) }
                }

            }

            override fun fail(from: String) {
                showToast(myActivity, from)
            }
        })
    }


}