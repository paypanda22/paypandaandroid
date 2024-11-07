package app.pay.pandapro.helperclasses

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.pandapro.R
import app.pay.pandapro.adapters.AepsMsAdapter
import app.pay.pandapro.adapters.DmtMakeTxnAdapter
import app.pay.pandapro.databinding.DialogAepsBeBinding
import app.pay.pandapro.databinding.DialogAepsCwBinding
import app.pay.pandapro.databinding.DialogAepsMsBinding
import app.pay.pandapro.databinding.DialogFingerResultBinding
import app.pay.pandapro.databinding.LytBottomDialogSingleButtonBinding
import app.pay.pandapro.databinding.LytBottomDialogTwoButtonBinding
import app.pay.pandapro.databinding.LytChangePasswordBinding
import app.pay.pandapro.databinding.LytChangePinBinding
import app.pay.pandapro.databinding.LytDialogBbpsSuccessBinding
import app.pay.pandapro.databinding.LytDialogDmtTxnListBinding
import app.pay.pandapro.databinding.OtpDialogBinding
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.interfaces.MyClick2
import app.pay.pandapro.interfaces.MyClickWithString
import app.pay.pandapro.responsemodels.dmtTransaction.Data
import app.pay.pandapro.responsemodels.forgetPin.ForgetPinResponse
import app.pay.pandapro.responsemodels.miniStatement.Ministatement
import app.pay.pandapro.responsemodels.pinChange.PasswordChangeResponse
import app.pay.pandapro.responsemodels.resendOtpForTpin.resendOtpForTpinResponse
import app.pay.pandapro.responsemodels.verifyOtp.VerifyOtpResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson
import java.util.Locale

class ShowDialog(private val context: Context) {
    private lateinit var tvResendOtp: TextView
    private lateinit var tvTimer: TextView
    var timeLeftInMillis: Long = 60000L
    var isTimerRunning: Boolean = false
    private lateinit var countDownTimer: CountDownTimer

    companion object {
        @SuppressLint("InflateParams")
        fun showDialog(
            context: Activity,
            title: String,
            text: String,
            type: String,
            click: MyClick
        ) {
            val dialogView = context.layoutInflater.inflate(R.layout.single_button_dialog, null)
            val dialogBuilder = AlertDialog.Builder(context).apply {
                setView(dialogView)

            }

            val txtTitle: TextView = dialogView.findViewById(R.id.textView)
            val txtDescription: TextView = dialogView.findViewById(R.id.textView2)
            val imageView: ImageView = dialogView.findViewById(R.id.imageView)
            val btnPositive: TextView = dialogView.findViewById(R.id.btn_okay)

            txtTitle.text = title
            txtDescription.text = text
            val imageResource = when (type.toLowerCase()) {
                "success" -> R.drawable.accepted
                "pending" -> R.drawable.pending_req
                else -> R.drawable.error
            }
            imageView.setImageResource(imageResource)

            val alertDialog = dialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            alertDialog.show()

            btnPositive.setOnClickListener {
                click.onClick()
                alertDialog.dismiss()
            }
        }

        fun aadhaarConsent(context: Activity) {
            val dialogView = context.layoutInflater.inflate(R.layout.lyt_dialog_aadhaar_consent, null)
            val dialogBuilder = AlertDialog.Builder(context).apply {
                setView(dialogView)

            }
            val btnOkey: TextView = dialogView.findViewById(R.id.btnOkey)
            val alertDialog = dialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            alertDialog.show()

            btnOkey.setOnClickListener {
                alertDialog.dismiss()
            }
        }


        @SuppressLint("InflateParams")
        fun confirmDialog(
            context: Activity,
            title: String,
            text: String,
            type: String,
            click: MyClick
        ) {
            val dialogView = context.layoutInflater.inflate(R.layout.custom_dialog_twobtn, null)
            val dialogBuilder = AlertDialog.Builder(context).apply {
                setView(dialogView)

            }

            val txtTitle: TextView = dialogView.findViewById(R.id.textView)
            val txtDescription: TextView = dialogView.findViewById(R.id.textView2)
            val imageView: ImageView = dialogView.findViewById(R.id.imageView)
            val btnPositive: Button = dialogView.findViewById(R.id.btn_okay)
            val btnNegative: Button = dialogView.findViewById(R.id.btn_no)

            txtTitle.text = title
            txtDescription.text = text
            val imageResource = when (type.lowercase(Locale.ROOT)) {
                "success" -> R.drawable.accepted
                "pending" -> R.drawable.pending_req
                else -> R.drawable.error
            }
            imageView.setImageResource(imageResource)

            val alertDialog = dialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(false)
            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            alertDialog.show()
            btnPositive.setOnClickListener {
                click.onClick()
                alertDialog.dismiss()
            }
            btnNegative.setOnClickListener { alertDialog.dismiss() }
        }

        fun bottomDialogTwoButton(activity: Activity, title: String, desc: String, type: String, click: MyClick, click2: MyClick2) {
            var bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = LytBottomDialogTwoButtonBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)

            val imageResource = when (type.toLowerCase()) {
                "success" -> R.drawable.accepted
                "pending" -> R.drawable.pending_req
                else -> R.drawable.error
            }
            dBinding.tvDesc.text = desc
            dBinding.tvTitle.text = title
            dBinding.imageView.setImageResource(imageResource)

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener {
                click2.onCancel()
                bottomSheetDialog.dismiss()
            }
            dBinding.btnYes.setOnClickListener {
                click.onClick()

                bottomSheetDialog.dismiss()
            }


        }

//        @SuppressLint("SetTextI18n")
//        fun bottomDialogOtp(
//            userSession: UserSession,
//            activity: Activity,
//            loginType: String,
//            entity: String,
//            token: String,
//            click: MyClickWithString
//        ) {
//            var bottomSheetDialog: Dialog = Dialog(activity)
//            val dBinding = OtpDialogBinding.inflate(activity.layoutInflater)
////            dBinding.root.background =
////                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
//            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//            bottomSheetDialog.setContentView(dBinding.root)
//            bottomSheetDialog.window
//                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
//            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
//
//            if (loginType == "phone") {
//                dBinding.tvTitleText.text = getString(activity, R.string.enter_otp_received_on_your_mobile)
//                dBinding.userMobile.text = getString(activity, R.string._91) + entity
//            } else {
//                dBinding.tvTitleText.text = getString(activity, R.string.enter_otp_received_on_your_email)
//                dBinding.userMobile.text = entity
//            }
//            bottomSheetDialog.setCancelable(false)
//            bottomSheetDialog.show()
//
//
//            dBinding.btnSubmit.setOnClickListener {
//                if (dBinding.edtOtpLogin.text.toString().length < 6) {
//                    Toast.makeText(activity, "Enter a Valid Otp", Toast.LENGTH_SHORT).show()
//                } else {
//                    if (loginType == "phone") {
//                        validateMobileOtp(bottomSheetDialog, activity, userSession, token, dBinding.edtOtpLogin.text.toString(), click)
//                    } else {
//                        validateEmailOtp(bottomSheetDialog, activity, userSession, token, dBinding.edtOtpLogin.text.toString(), click)
//                    }
//
//                }
//            }
//            dBinding.ivCancel.setOnClickListener { bottomSheetDialog.dismiss() }
//
//
//        }

        private fun validateEmailOtp(
            bottomSheetDialog: Dialog,
            activity: Activity,
            userSession: UserSession,
            token: String,
            otp: String,
            click: MyClickWithString
        ) {
            val requestData = hashMapOf<String, Any?>()
            requestData["user_id"] = token
            requestData["deviceId"] = CommonClass.getDeviceId(activity)
            requestData["otp"] = otp

            UtilMethods.verifyOtpEmail(activity, requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: VerifyOtpResponse = Gson().fromJson(message, VerifyOtpResponse::class.java)
                    if (response.data.user.isNotEmpty()) {
                        click.onClick(response.data.user)
                        bottomSheetDialog.dismiss()
                    } else {
                        Toast.makeText(activity, "Unable to verify OTP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun fail(from: String) {
                    Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
                }
            })

        }

      /*  private fun validateMobileOtp(
            dialog: Dialog,
            activity: Activity,
            userSession: UserSession,
            token: String,
            otp: String,
            click: MyClickWithString
        ) {
            val requestData = hashMapOf<String, Any?>()
            requestData["user_id"] = token
            requestData["deviceId"] = CommonClass.getDeviceId(activity)
            requestData["otp"] = otp
            UtilMethods.verifyOtp(activity, requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: VerifyOtpResponse = Gson().fromJson(message, VerifyOtpResponse::class.java)
                    if (response.data.user.isNotEmpty()) {
                        click.onClick(response.data.user)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(activity, "Unable to verify OTP", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun fail(from: String) {
                    Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
                }
            })

        }*/


        fun bottomDialogSingleButton(activity: Activity, title: String, desc: String, type: String, click: MyClick) {
            var bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = LytBottomDialogSingleButtonBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)

            val imageResource = when (type.lowercase()) {
                "success" -> R.drawable.accepted
                "pending" -> R.drawable.pending_req
                "finger" -> R.drawable.finger_error
                "resetpin"->{
                    dBinding.btnYes.background=ContextCompat.getDrawable(activity,R.drawable.submitt_btn_water_blue)
                    R.drawable.pin_color
                }
                else -> R.drawable.error
            }
            dBinding.tvDesc.text = desc
            dBinding.tvTitle.text = title
            dBinding.imageView.setImageResource(imageResource)

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener { bottomSheetDialog.dismiss() }
            dBinding.btnYes.setOnClickListener {
                click.onClick()

                bottomSheetDialog.dismiss()
            }


        }

        @SuppressLint("SetTextI18n")
        fun aepsBalanceEnq(activity: Activity, model: app.pay.pandapro.responsemodels.aepsBe.Data, desc: String, isCashDeposit:Boolean, click: MyClick, click2: MyClick2) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = DialogAepsBeBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
            dBinding.tvAccountBalance.text = model.balanceamount
            dBinding.tvBankName.text = model.bank_name
            dBinding.tvBankRrn.text = model.bank_rrn
            dBinding.tvAadhar.text = "xxxx-xxxx-" + model.lastAadhar
            dBinding.tvDesc.text = desc
            if (isCashDeposit) {
                dBinding.btnYes.visibility=GONE
            }else{
                dBinding.btnYes.visibility=View.VISIBLE
            }

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener {
                click2.onCancel()
                bottomSheetDialog.dismiss()
            }
            dBinding.btnYes.setOnClickListener {
                click.onClick()
                bottomSheetDialog.dismiss()
            }


        }

        fun aepsMiniStmt(activity: Activity, model: app.pay.pandapro.responsemodels.miniStatement.Data, desc: String, click: MyClick) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = DialogAepsMsBinding.inflate(activity.layoutInflater)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            dBinding.tvAccountBalance.text = model.balanceamount.toString()
            dBinding.tvBankName.text = model.bank_name
            dBinding.tvBankRrn.text = model.bank_rrn
            dBinding.tvDesc.text = desc
            val list: MutableList<Ministatement> = mutableListOf()
            list.addAll(model.ministatement)

            val txnAdapter = AepsMsAdapter(activity, list)
            dBinding.rvMiniStmt.adapter = txnAdapter
            dBinding.rvMiniStmt.layoutManager = LinearLayoutManager(activity)
            dBinding.tvClose.setOnClickListener { bottomSheetDialog.dismiss() }
            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener { bottomSheetDialog.dismiss() }
            dBinding.btnYes.setOnClickListener {
                click.onClick()
                bottomSheetDialog.dismiss()
            }


        }

        @SuppressLint("SetTextI18n")
        fun aepsCashWithdrawal(activity: Activity, message: String, model: app.pay.pandapro.responsemodels.aepsCw.Data, click: MyClick) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = DialogAepsCwBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
            dBinding.tvAccountBalance.text = model.balanceamount
            dBinding.tvBankName.text = model.bank_name
            dBinding.tvBankRrn.text = model.bank_rrn
            dBinding.tvDesc.text = message
            dBinding.tvAmount.text = model.amount
            dBinding.tvCustomerName.text = model.name
            dBinding.tvAdhaarNumber.text = "xxxx-xxxx-${model.last_aadhar}"

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            dBinding.btnYes.setOnClickListener {
                click.onClick()
                bottomSheetDialog.dismiss()
            }


        }

        fun bottomDialogDmtMakeTxn(activity: Activity, title: String, list: List<Data>, type: String, click: MyClick) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = LytDialogDmtTxnListBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)

            val imageResource = when (type.lowercase()) {
                "success" -> R.drawable.accepted
                "pending" -> R.drawable.pending_req
                else -> R.drawable.error
            }
            val totalAmount = list.sumOf { it.amount ?: 0 }
            dBinding.tvTotalAmount.text = totalAmount.toString()
            dBinding.tvAccountNumber.text = list[0].account_number.toString()
            dBinding.tvBatchId.text = list[0].batchId
            dBinding.tvTitle.text = title
            dBinding.imageView.setImageResource(imageResource)

            val adapter = DmtMakeTxnAdapter(activity, list)
            dBinding.rvTxnList.adapter = adapter
            dBinding.rvTxnList.layoutManager = LinearLayoutManager(activity)

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener { bottomSheetDialog.dismiss() }
            dBinding.btnYes.setOnClickListener {
                click.onClick()
                bottomSheetDialog.dismiss()
            }


        }

        @SuppressLint("SetTextI18n")
        fun scanResult(activity: Activity, score: Int, click: MyClick) {
            var bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = DialogFingerResultBinding.inflate(activity.layoutInflater)
//            dBinding.root.background =
//                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)

            dBinding.tvScore.text = "$score%"
            if (score <= 30) {
                dBinding.tvScore.setTextColor(ContextCompat.getColor(activity, R.color.red))
                dBinding.btnNo.visibility = View.VISIBLE
            } else {
                dBinding.tvScore.setTextColor(ContextCompat.getColor(activity, R.color.water_blue))
                dBinding.btnNo.visibility = View.GONE
            }
            dBinding.ivCancel.setOnClickListener { bottomSheetDialog.dismiss() }

            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener { bottomSheetDialog.dismiss() }
            dBinding.btnYes.setOnClickListener {
                click.onClick()

                bottomSheetDialog.dismiss()
            }


        }

        fun changePassword(activity: FragmentActivity, userSession: UserSession) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = LytChangePasswordBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)


            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.show()
            dBinding.btnChangePassword.setOnClickListener {
                if (dBinding.edtOldPassword.text.toString().isEmpty()) {
                    showToast(activity, "Enter a Old Password")
                } else if (dBinding.edtNewPassword.text.toString().isEmpty()) {
                    showToast(activity, "Enter a New Password")
                } else if (!ActivityExtensions.isValidPassword(dBinding.edtNewPassword.text.toString())){
                    showToast(activity, "Password Not Matching the Password Rules")
                } else if (dBinding.edtNewPassword.text.toString() != dBinding.edtConfirmPassword.text.toString()) {
                    showToast(activity, "Password Not Matched")
                } else {
                    val token = userSession.getData(Constant.USER_TOKEN).toString()
                    changePasswordApi(
                        activity,
                        token,
                        dBinding.edtOldPassword.text.toString(),
                        dBinding.edtNewPassword.text.toString(),
                        bottomSheetDialog
                    )
                }
            }

        }

        private fun changePasswordApi(activity: Activity, token: String, oldPassword: String, newPassword: String, bottomSheetDialog: Dialog) {
            val requestData = hashMapOf<String, Any?>()
            requestData["user_id"] = token
            requestData["oldPassword"] = oldPassword
            requestData["newPassword"] = newPassword
            requestData["confirmPassword"] = newPassword
            UtilMethods.changePassword(activity, requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: PasswordChangeResponse = Gson().fromJson(message, PasswordChangeResponse::class.java)
                    if (!response.error) {
                        bottomSheetDialog.dismiss()
                        bottomDialogSingleButton(activity, "SUCCESS", "Password Changed Successfully", "success", object : MyClick {
                            override fun onClick() {

                            }
                        })
                    } else {
                        showToast(activity, response.message)
                    }

                }

                override fun fail(from: String) {
                    showToast(activity, from)
                }
            })

        }

        fun changePin(activity: FragmentActivity, userSession: UserSession) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = LytChangePinBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)


            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.show()
            dBinding.btnChangePassword.setOnClickListener {
                if (dBinding.edtOldPin.text.toString().length < 4) {
                    showToast(activity, "Enter a Valid Old Pin")
                } else if (dBinding.edtNewPin.text.toString().length < 4) {
                    showToast(activity, "Enter a New PIN")
                } else if (dBinding.edtNewPin.text.toString() != dBinding.edtConfirmPin.text.toString()) {
                    showToast(activity, "PIN Not Matched")
                } else {
                    val token = userSession.getData(Constant.USER_TOKEN).toString()
                    changePinApi(activity, token, dBinding.edtOldPin.text.toString(), dBinding.edtNewPin.text.toString(), bottomSheetDialog)
                }
            }
        }

        private fun changePinApi(activity: FragmentActivity, token: String, oldPin: String, newPin: String, bottomSheetDialog: Dialog) {
            val requestData = hashMapOf<String, Any?>()
            requestData["user_id"] = token
            requestData["oldPin"] = oldPin
            requestData["newPin"] = newPin
            requestData["confirmPin"] = newPin
            UtilMethods.changePin(activity, requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: PasswordChangeResponse = Gson().fromJson(message, PasswordChangeResponse::class.java)
                    if (!response.error) {
                        bottomSheetDialog.dismiss()
                        bottomDialogSingleButton(activity, "SUCCESS", "Transaction Pin Changed Successfully", "success", object : MyClick {
                            override fun onClick() {

                            }
                        })
                    } else {
                        showToast(activity, response.message)
                    }
                }

                override fun fail(from: String) {
                    showToast(activity, from)
                }
            })

        }

        fun forgetPin(activity: FragmentActivity, userSession: UserSession) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = OtpDialogBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
            dBinding.userMobile.text = userSession.getData(Constant.MOBILE).toString()

            // Timer-related variables
            var timeLeftInMillis: Long = 60000L
            var isTimerRunning: Boolean = false
            lateinit var countDownTimer: CountDownTimer

            // Initialize your views for timer and resend OTP
            val tvTimer = dBinding.tvTimer
            val tvResendOtp = dBinding.tvResendOtp

            // Update the timer text method (MM:SS)
            @SuppressLint("DefaultLocale")
            fun updateTimer() {
                val minutes = timeLeftInMillis / 1000 / 60
                val seconds = timeLeftInMillis / 1000 % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
                tvTimer.text = timeLeftFormatted
            }

            // Start timer function
            fun setTimer() {
                tvTimer.visibility = View.VISIBLE
                tvResendOtp.setTextColor(ContextCompat.getColor(activity, R.color.bggrey_dark))

                countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeLeftInMillis = millisUntilFinished
                        updateTimer()  // Call updateTimer to update the UI with formatted time
                    }

                    override fun onFinish() {
                        tvResendOtp.isEnabled = true
                        tvTimer.visibility = View.GONE
                        tvResendOtp.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
                        isTimerRunning = false
                    }
                }
                countDownTimer.start()
                isTimerRunning = true
            }

            // Cancel timer function
            fun cancelTimer() {
                if (isTimerRunning) {
                    countDownTimer.cancel()
                }
            }

            // Start the dialog
            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.show()

            // Start the timer initially
            setTimer()
            bottomSheetDialog.setCancelable(true)
            bottomSheetDialog.show()


            dBinding.btnSubmit.setOnClickListener {
                if (dBinding.edtOtpLogin.text.toString().length < 6) {
                    showToast(activity, "Enter Otp Received On Your Mobile")
                } else {
                    forgetTPin(activity, dBinding.edtOtpLogin.text.toString(), userSession, bottomSheetDialog)
                }
            }
            dBinding.tvResendOtp.setOnClickListener{

                    if (isTimerRunning) {
                        showToast(activity,"You can Resend OTP After One Minute")
                    } else {
                        timeLeftInMillis = 60000L  // Reset to 1 minute
                        setTimer() // Restart the timer
                        tvResendOtp.isEnabled = false // Disable the resend button until timer finishes
                        val token = userSession.getData(Constant.USER_TOKEN).toString()
                        UtilMethods.resendOtpForTPin(activity, token, object : MCallBackResponse {
                            override fun success(from: String, message: String) {
                                val response: resendOtpForTpinResponse =
                                    Gson().fromJson(message, resendOtpForTpinResponse::class.java)

                                if (!response.error!!) {
                                    Toast.makeText(activity, response.message.toString(), Toast.LENGTH_SHORT)
                                        .show()

                                } else {
                                    Toast.makeText(activity, "Unable to Load Bank List", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun fail(from: String) {
                                Toast.makeText(activity, "Unable to Load Bank List", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })

                    }

            }

        }

        private fun forgetTPin(activity: Activity, otp: String, userSession: UserSession, bottomSheetDialog: Dialog) {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            val requestData = hashMapOf<String, Any?>()
            requestData["user_id"] = token
            requestData["otp"] = otp
            UtilMethods.forgetTransactionPin(activity, requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: ForgetPinResponse = Gson().fromJson(message, ForgetPinResponse::class.java)
                    if (!response.error) {
                        bottomSheetDialog.dismiss()
                        bottomDialogSingleButton(activity, "SUCCESS", response.message, "success", object : MyClick {
                            override fun onClick() {

                            }
                        })
                    } else {
                        showToast(activity, response.message)
                    }
                }

                override fun fail(from: String) {
                    showToast(activity, from)
                }
            })
        }

        @SuppressLint("SetTextI18n")
        fun bbpsSuccess(activity: Activity, model: app.pay.pandapro.responsemodels.billpayresponse.Data, desc: String, click: MyClick) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = LytDialogBbpsSuccessBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
            dBinding.tvCaNumber.text = model.ca_number
            dBinding.tvOpID.text = model.operator_id
            dBinding.tvTxnId.text = model.txn_id
            dBinding.tvOpName.text =model.operator_name
            dBinding.tvDesc.text = desc

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnYes.setOnClickListener {
                click.onClick()
                bottomSheetDialog.dismiss()
            }


        }

        @SuppressLint("SetTextI18n")
        fun cashDepositSuccess(activity: Activity, message:String, model:app.pay.pandapro.responsemodels.cashdeposit.Data, click:MyClick) {
            val bottomSheetDialog: Dialog = Dialog(activity)
            val dBinding = DialogAepsCwBinding.inflate(activity.layoutInflater)
            dBinding.root.background =
                ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
            bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            bottomSheetDialog.setContentView(dBinding.root)
            bottomSheetDialog.window
                ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
            bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
            dBinding.rlCustomerName.visibility=View.GONE
            dBinding.tvAccountBalance.text = model.bal_amount.toString()
            dBinding.tvBankName.text = model.bank_name
            dBinding.tvBankRrn.text = model.bank_rrn
            dBinding.tvDesc.text = message
            dBinding.tvAmount.text = model.amount.toString()
            dBinding.tvAdhaarNumber.text = "xxxx-xxxx-${model.last_adhaar}"

            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.show()

            dBinding.btnNo.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            dBinding.btnYes.setOnClickListener {
                click.onClick()
                bottomSheetDialog.dismiss()
            }

        }


    }
}