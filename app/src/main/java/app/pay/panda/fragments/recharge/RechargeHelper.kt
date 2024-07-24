package app.pay.panda.fragments.recharge

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import app.pay.panda.R
import app.pay.panda.databinding.FragmentMobileRechargeBinding
import app.pay.panda.databinding.FragmentROfferBinding
import app.pay.panda.databinding.RechargeConfirmationDialogBinding
import app.pay.panda.databinding.RechargeSuccessDialogBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.processRecharge.Data
import app.pay.panda.responsemodels.processRecharge.ProcessRechargeResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class RechargeHelper(
    private val activity: FragmentActivity,
    private val binding: FragmentMobileRechargeBinding,
    private val userSession: UserSession,
    private val navController: NavController
) {
    fun processRecharge(operatorID: String) {
        if (binding.edtMobile.text.toString().isEmpty()) {
            showToast(activity, "Enter Mobile Number")
        } else if (!ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())) {
            binding.edtMobile.error = "Enter a Valid Mobile Number"
            showToast(activity, "Enter a Valid Mobile Number")
        } else if (operatorID.isEmpty()) {
            binding.edtOperatorName.error = "Select Operator Name"
            showToast(activity, "Select Operator Name")
        } else if (binding.edtAmount.text.toString().isEmpty()) {
            binding.edtAmount.error = "Enter Amount"
        } else {
            val amt = binding.edtAmount.text.toString().toInt()
            if (amt <= 10) {
                binding.edtAmount.error = "Amount Should be greater than 10"
            } else {
                doRecharge(operatorID)
            }
        }
    }

    private fun doRecharge(operatorID: String) {
        val bottomSheetDialog: Dialog = Dialog(activity)
        val dBinding = RechargeConfirmationDialogBinding.inflate(activity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bottomSheetDialog.setContentView(dBinding.root)
        bottomSheetDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.tvMobileTitle.text="Mobile Number :"
        dBinding.tvConfirmMobile.text=binding.edtMobile.text.toString()
        dBinding.billerName.text=binding.edtOperatorName.text.toString()
        dBinding.tvAmount.text=binding.edtAmount.text.toString()
        dBinding.opLogo.setImageDrawable(getOperatorImage(operatorID))

        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.show()
        dBinding.btnPay.setOnClickListener {
            if (dBinding.edtOtpLogin.text.toString().length<4){
                showToast(activity,"Enter Your Transaction Pin")
            }else{
                finalRecharge(operatorID,dBinding.edtOtpLogin.text.toString())
                bottomSheetDialog.dismiss()
            }
        }


    }

    private fun getOperatorImage(operatorID: String): Drawable? {
        when(operatorID){
            "66617b5f3c35f52923782cc0"->{
                return ContextCompat.getDrawable(activity,R.drawable.bsnl)
            }
            "66617b5f3c35f52923782cc3"->{
                return ContextCompat.getDrawable(activity,R.drawable.vi)
            }
            "66617b5f3c35f52923782cbf"->{
                return  ContextCompat.getDrawable(activity,R.drawable.bsnl)
            }
            "66617b5f3c35f52923782cbe"->{
                return ContextCompat.getDrawable(activity,R.drawable.airtel)
            }
            "66617b5f3c35f52923782cc2"->{
                return ContextCompat.getDrawable(activity,R.drawable.jio)
            }
            "66680134061904fed64223ab"->{
                return ContextCompat.getDrawable(activity,R.drawable.airtel_tv)
            }
            "66680198061904fed64223b4"->{
                return ContextCompat.getDrawable(activity,R.drawable.dish_tv)
            }
            "666803ee061904fed64223b8"->{
                return ContextCompat.getDrawable(activity,R.drawable.sun_direct)
            }
            "66680479061904fed64223bb"->{
                return ContextCompat.getDrawable(activity,R.drawable.tata_sky)
            }
            "666804bf061904fed64223bf"->{
                return ContextCompat.getDrawable(activity,R.drawable.v_d2h)
            }
            else->{
                return ContextCompat.getDrawable(activity,R.drawable.bsnl)
            }
        }
    }

    private fun finalRecharge(operatorID:String,tPin:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["billNumber"]=binding.edtMobile.text.toString()
        requestData["rechargeId"]=operatorID
        requestData["amount"]=binding.edtAmount.text.toString()
        requestData["tpin"]=tPin
        UtilMethods.processRecharge(activity,requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:ProcessRechargeResponse=Gson().fromJson(message,ProcessRechargeResponse::class.java)
                if (response.error==false){
                    successDialog(response.data)
                }else{
                    ShowDialog.bottomDialogSingleButton(activity,"Transaction Failed",response.message?:"Try Again Later !","error",object:MyClick{
                        override fun onClick() {

                        }
                    })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(activity,"Transaction Failed",from,"error",object:MyClick{
                    override fun onClick() {

                    }
                })
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun successDialog(data: Data?) {
        val bottomSheetDialog: Dialog = Dialog(activity)
        val dBinding = RechargeSuccessDialogBinding.inflate(activity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bottomSheetDialog.setContentView(dBinding.root)
        bottomSheetDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.tvAmount.text=binding.edtAmount.text.toString()
        dBinding.tvClosing.text=data?.close_bal.toString()
        dBinding.tvOpName.text=data?.operator_name.toString()
        dBinding.tvOpId.text=data?.operatorid.toString()
        if (data?.status==2){
            dBinding.tvDesc.text="Transaction Successfully Completed"
        }else{
            dBinding.tvDesc.text="Transaction Successfully Accepted"
        }
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.show()
        dBinding.btnYes.setOnClickListener {
            binding.edtMobile.text?.clear()
            binding.edtAmount.text?.clear()
            bottomSheetDialog.dismiss()
        }

    }
}