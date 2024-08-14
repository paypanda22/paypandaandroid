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
import app.pay.panda.databinding.LytChangePinBinding
import app.pay.panda.databinding.LytCreateTransactionPinBinding
import app.pay.panda.databinding.OtpDialogBinding
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.gerateTpinOtp.GenerateTPinOtpResponse
import app.pay.panda.responsemodels.resendtpin.TransactionPinResponse
import app.pay.panda.retrofit.ApiMethods
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class ResetTPIN(
    private val myActivity:Activity,
    private val userSession: UserSession) {

    fun resetTPin() {
        val tPinStatus=userSession.getData(Constant.TPINSTATUS).toString()
        if (tPinStatus=="NP"){
            ShowDialog.bottomDialogSingleButton(myActivity,"Create Your Transaction Pin","This PIN will used to complete Transactions.\n We will send OTP to your Registered Mobile Number.","resetPin",object:
                MyClick {
                override fun onClick() {
                    generateOtpForTPin()
                }
            })
        }
    }

    private fun generateOtpForTPin() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        ApiMethods.generateOtpForTPin(myActivity,token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:GenerateTPinOtpResponse=Gson().fromJson(message,GenerateTPinOtpResponse::class.java)
                if(response.error==false){
                    openTPinOtpDialog()
                }else{
                    showToast(myActivity,response.message)
                }
            }

            override fun fail(from: String) {
                showToast(myActivity,from)
            }
        })

    }

    private fun openTPinOtpDialog() {
        val tPinOtpDialog: Dialog = Dialog(myActivity)
        val tPinOtpDialogBinding = OtpDialogBinding.inflate(myActivity.layoutInflater)
        tPinOtpDialogBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        tPinOtpDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        tPinOtpDialog.setContentView(tPinOtpDialogBinding.root)
        tPinOtpDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tPinOtpDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tPinOtpDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        tPinOtpDialog.window?.setGravity(Gravity.BOTTOM)
        tPinOtpDialogBinding.userMobile.text = userSession.getData(Constant.MOBILE).toString()


        tPinOtpDialog.setCancelable(false)
        tPinOtpDialog.show()


        tPinOtpDialogBinding.btnSubmit.setOnClickListener {
            if (tPinOtpDialogBinding.edtOtpLogin.text.toString().length < 6) {
                showToast(myActivity, "Enter Otp Received On Your Mobile")
            } else {
                verifyTPinOtp(tPinOtpDialogBinding,tPinOtpDialog)
            }
        }
        tPinOtpDialogBinding.tvResendOtp.setOnClickListener{
            resendOtpTPin()
        }    }

    private fun verifyTPinOtp(tPinOtpDialogBinding: OtpDialogBinding, tPinOtpDialog: Dialog) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["otp"]=tPinOtpDialogBinding.edtOtpLogin.text.toString()
        ApiMethods.verifyTPinOtp(myActivity,requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:GenerateTPinOtpResponse=Gson().fromJson(message,GenerateTPinOtpResponse::class.java)
                if(response.error==false){
                    tPinOtpDialog.dismiss()
                    openResetOtpDialog()
                }else{
                    showToast(myActivity,response.message)
                }
            }

            override fun fail(from: String) {
                showToast(myActivity,from)
            }
        })

    }

    private fun resendOtpTPin() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        ApiMethods.resendOtpTPin(myActivity,token,requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: TransactionPinResponse =Gson().fromJson(message,TransactionPinResponse::class.java)
                if(response.error==false){
                    showToast(myActivity,"Otp Sand Successfully ")
                }else{
                    showToast(myActivity,response.message)
                }
            }

            override fun fail(from: String) {
               // showToast(myActivity,from)
            }
        })

    }


    private fun openResetOtpDialog() {
        val createTPinDialog: Dialog = Dialog(myActivity)
        val dBinding = LytCreateTransactionPinBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        createTPinDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        createTPinDialog.setContentView(dBinding.root)
        createTPinDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        createTPinDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        createTPinDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        createTPinDialog.window?.setGravity(Gravity.BOTTOM)


        createTPinDialog.setCancelable(false)
        createTPinDialog.show()
        dBinding.btnChangePassword.setOnClickListener {
          if (dBinding.edtNewPin.text.toString().length < 4) {
                showToast(myActivity, "Enter a New PIN")
            } else if (dBinding.edtNewPin.text.toString() != dBinding.edtConfirmPin.text.toString()) {
                showToast(myActivity, "PIN Not Matched")
            } else {
                val token = userSession.getData(Constant.USER_TOKEN).toString()
                createTPin(createTPinDialog,dBinding)
            }
        }
    }

    private fun createTPin(createTPinDialog: Dialog, dBinding: LytCreateTransactionPinBinding) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["genTpin"]=dBinding.edtNewPin.text.toString()
        requestData["confirmTpin"]=dBinding.edtNewPin.text.toString()
        ApiMethods.createNewTPin(myActivity,requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:GenerateTPinOtpResponse=Gson().fromJson(message,GenerateTPinOtpResponse::class.java)
                if (response.error==false){
                    showToast(myActivity,response.message)
                    createTPinDialog.dismiss()
                }else{
                    showToast(myActivity,response.message)
                }
            }

            override fun fail(from: String) {
                showToast(myActivity,from)
            }
        })

    }
}