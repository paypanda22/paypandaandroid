package app.pay.panda.helperclasses

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import app.pay.panda.R
import app.pay.panda.databinding.LytTransferFromAepsToMainWalletBinding
import app.pay.panda.databinding.OtpDialogBinding
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.general.GeneralResponse
import app.pay.panda.retrofit.ApiMethods
import app.pay.panda.retrofit.Constant
import com.google.gson.Gson

class AepsWalletActions(
    private val myActivity: Activity,
    private val userSession: UserSession
) {
    fun openTransferToWalletDialog() {
        val toWalletDialog: Dialog = Dialog(myActivity)
        val toWalletDialogBinding = LytTransferFromAepsToMainWalletBinding.inflate(myActivity.layoutInflater)
        toWalletDialogBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        toWalletDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        toWalletDialog.setContentView(toWalletDialogBinding.root)
        toWalletDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        toWalletDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        toWalletDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        toWalletDialog.window?.setGravity(Gravity.BOTTOM)
        toWalletDialogBinding.edtAmount.requestFocus()
        val balance = userSession.getData(Constant.AEPS_WALLET).toString().toDouble()
        toWalletDialogBinding.tvAepsBalance.text = balance.toString()
        toWalletDialog.setCancelable(true)
        toWalletDialog.show()


        toWalletDialogBinding.btnSubmit.setOnClickListener {
            if (toWalletDialogBinding.edtOtpMobile.text.toString().length < 4) {
                showToast(myActivity, "Enter Your Transaction PIN")
            } else if (toWalletDialogBinding.edtAmount.text.toString().isEmpty()) {
                toWalletDialogBinding.edtAmount.error = "Enter Amount to Transfer"
            } else {
                val amt = toWalletDialogBinding.edtAmount.text.toString().toInt()
                if (amt > balance) {
                    toWalletDialogBinding.edtAmount.error = "Insufficient Balance"
                } else {
                    processTransfer(toWalletDialogBinding, toWalletDialog)
                }

            }   
        }
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