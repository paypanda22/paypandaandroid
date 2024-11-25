package app.pay.retailers.helperclasses

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import app.pay.retailers.R
import app.pay.retailers.databinding.LytCreateTransactionPinBinding
import app.pay.retailers.databinding.OtpDialogBinding
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.responsemodels.gerateTpinOtp.GenerateTPinOtpResponse
import app.pay.retailers.responsemodels.resendtpin.TransactionPinResponse
import app.pay.retailers.retrofit.ApiMethods
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson

class ResetTPIN(
    private val myActivity:Activity,
    private val userSession: UserSession) {
    val tPinOtpDialogBinding = OtpDialogBinding.inflate(myActivity.layoutInflater)
    private lateinit var countDownTimer: CountDownTimer
    var timeLeftInMillis: Long = 60000L
    var isTimerRunning: Boolean = false
    fun resetTPin() {
        val tPinStatus=userSession.getData(Constant.TPINSTATUS).toString()
        if (tPinStatus=="NP"){
            ShowDialog.bottomDialogSingleButton(myActivity,"Create Your Transaction Pin","This PIN will used to complete Transactions.\n We will send OTP to your Registered Mobile Number.","resetPin",object:
                MyClick {
                override fun onClick() {
                    generateOtpForTPin()
                }
            })
        }else if(tPinStatus=="OV"){
            openResetOtpDialog()
        }else if (tPinStatus == "CP"){

        }else{

        }
    }

    private fun generateOtpForTPin() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        ApiMethods.generateOtpForTPin(myActivity,token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:GenerateTPinOtpResponse=Gson().fromJson(message,GenerateTPinOtpResponse::class.java)
                if(response.error==false){
                    openTPinOtpDialog()
                    setTimer()
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
       //  tPinOtpDialogBinding = OtpDialogBinding.inflate(myActivity.layoutInflater)
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
         tPinOtpDialogBinding.headingText.text="Generate Transaction Pin"

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
            if (isTimerRunning) {
                showToast(myActivity,"You can Resend OTP After One Minute")
            } else {
                resendOtpTPin()
            }
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
                    userSession.setData(Constant.TPINSTATUS, "OV")
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
        UtilMethods.resendOtpForTPin(myActivity,token,object:MCallBackResponse{
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
                    userSession.setData(Constant.TPINSTATUS, "CP")
                }else{
                    showToast(myActivity,response.message)
                }
            }

            override fun fail(from: String) {
                showToast(myActivity,from)
            }
        })

    }


    fun setTimer() {
        tPinOtpDialogBinding.tvTimer.visibility = View.VISIBLE
        tPinOtpDialogBinding.tvResendOtp.setTextColor(
            ContextCompat.getColor(
                myActivity, R.color.bggrey_dark
            )
        )
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                tPinOtpDialogBinding.tvResendOtp.isEnabled = true
                tPinOtpDialogBinding.tvTimer.visibility = View.GONE
                tPinOtpDialogBinding.tvResendOtp.setTextColor(
                    ContextCompat.getColor(
                        myActivity, R.color.colorPrimaryDark
                    )
                )
                isTimerRunning = false
            }
        }
        countDownTimer.start()
        isTimerRunning = true
    }

    private fun cancelTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel()
        }

    }

    private fun updateTimer() {
        val minutes = timeLeftInMillis / 1000 / 60
        val seconds = timeLeftInMillis / 1000 % 60

        @SuppressLint("DefaultLocale") val timeLeftFormatted =
            String.format("%02d:%02d", minutes, seconds)
        tPinOtpDialogBinding.tvTimer.text = timeLeftFormatted
    }

}