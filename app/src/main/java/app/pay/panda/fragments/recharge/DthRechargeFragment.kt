package app.pay.panda.fragments.recharge

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.RechargeOperatorAdapter
import app.pay.panda.databinding.DialogDisputeMasterBinding
import app.pay.panda.databinding.FragmentDthRechargeBinding
import app.pay.panda.databinding.RechargeConfirmationDialogBinding
import app.pay.panda.databinding.RechargeSuccessDialogBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.RechargeOperatorClick
import app.pay.panda.responsemodels.dthInfo.DthInforResponse
import app.pay.panda.responsemodels.processRecharge.Data
import app.pay.panda.responsemodels.processRecharge.ProcessRechargeResponse
import app.pay.panda.responsemodels.rechargeOperator.Operator
import app.pay.panda.responsemodels.rechargeOperator.RechargeOperatorResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class DthRechargeFragment : BaseFragment<FragmentDthRechargeBinding>(FragmentDthRechargeBinding::inflate), RechargeOperatorClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var operatorID = "66680198061904fed64223b4"
    private lateinit var alertDialog: AlertDialog
    private lateinit var operatorList: MutableList<Operator>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
    }

    private fun getOperatorList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getRechargeOperators(requireContext(), "667e8a8aa0cc9372aaceb002", token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: RechargeOperatorResponse = Gson().fromJson(message, RechargeOperatorResponse::class.java)
                if (!response.error) {
                    operatorList = mutableListOf()
                    if (response.data.operators.isNotEmpty()) {
                        if (operatorList.isNotEmpty()) {
                            operatorList.clear()
                        }
                        operatorList.addAll(response.data.operators)
                        openOperatorListDialog()
                    } else {
                        Utils.showToast(requireContext(), "Unable to Fetch Operator List")
                    }
                } else {
                    Utils.showToast(requireContext(), "Unable to Fetch Operator List")
                }


            }

            override fun fail(from: String) {
                Utils.showToast(requireContext(), "Unable to Fetch Operator List")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun openOperatorListDialog() {
        val dBinding = DialogDisputeMasterBinding.inflate(myActivity.layoutInflater)
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dBinding.root)
        }
        dBinding.tvTitle.text = "Select Mobile Operator"

        val operatorAdapter = RechargeOperatorAdapter(myActivity, operatorList, this@DthRechargeFragment)

        dBinding.rvDisputeMaster.adapter = operatorAdapter
        dBinding.rvDisputeMaster.layoutManager = LinearLayoutManager(myActivity)


        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.edtMobile.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.tvAddress.text=""
                binding.tvMonthlyRecharge.text = ""
                binding.tvName.text =""
                binding.tvDueDate.text =""
                binding.llDthInfo.visibility= GONE
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.ivBack.setOnClickListener {
            startActivity(Intent(myActivity, DashBoardActivity::class.java))
            myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }

        binding.edtOperatorName.setOnClickListener { getOperatorList() }

       // binding.ivChangeOperator.setOnClickListener { getOperatorList() }

        binding.btnSubmit.setOnClickListener {
            if (binding.edtMobile.text.toString().isEmpty()) {
                Utils.showToast(activity, "Enter Mobile/Vc Number")
            } else if (operatorID.isEmpty()) {
                binding.edtOperatorName.error = "Select Operator Name"
                Utils.showToast(activity, "Select Operator Name")
            } else if (binding.edtAmount.text.toString().isEmpty()) {
                binding.edtAmount.error = "Enter Amount"
            } else {
                val amt = binding.edtAmount.text.toString().toInt()
                if (amt <= 10) {
                    binding.edtAmount.error = "Amount Should be greater than 10"
                } else {
                    processRecharge()
                }
            }
        }

        binding.ivInfo.setOnClickListener {
            if (binding.edtMobile.text.toString().isEmpty()) {
                binding.edtMobile.error = "Enter Mobile/VC Number"
            } else if (operatorID.isEmpty()) {
                binding.edtOperatorName.error = "Select Operator Name"
            } else {
                getDthInfo()
            }
        }


    }

    private fun getDthInfo() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getDthInfo(requireContext(), operatorID, binding.edtMobile.text.toString(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DthInforResponse = Gson().fromJson(message, DthInforResponse::class.java)
                if (response.error == false) {
                    binding.tvAddress.text = response.data?.Address ?: ""
                    binding.tvMonthlyRecharge.text = response.data?.monthly.toString()
                    binding.tvName.text = response.data?.name.toString()
                    binding.tvDueDate.text = response.data?.next_recharge_date.toString()
                    binding.llDthInfo.visibility = VISIBLE
                } else {
                    showToast(requireContext(), response.message)
                }
            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun processRecharge() {
        val bottomSheetDialog: Dialog = Dialog(myActivity)
        val dBinding = RechargeConfirmationDialogBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bottomSheetDialog.setContentView(dBinding.root)
        bottomSheetDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.tvMobileTitle.text="Mobile/VC Number :"
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
                doRecharge(dBinding.edtOtpLogin.text.toString())
                bottomSheetDialog.dismiss()
            }
        }
    }

    private fun doRecharge(tPin:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["billNumber"] = binding.edtMobile.text.toString()
        requestData["rechargeId"] = operatorID
        requestData["amount"] = binding.edtAmount.text.toString()
        requestData["tpin"]=tPin
        UtilMethods.processRecharge(myActivity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: ProcessRechargeResponse = Gson().fromJson(message, ProcessRechargeResponse::class.java)
                if (response.error == false) {
                    successDialog(response.data)
                } else {
                    ShowDialog.bottomDialogSingleButton(
                        myActivity,
                        "Transaction Failed",
                        response.message ?: "Try Again Later !",
                        "error",
                        object : MyClick {
                            override fun onClick() {

                            }
                        })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "Transaction Failed", from, "error", object : MyClick {
                    override fun onClick() {

                    }
                })
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun successDialog(data: Data?) {
        val bottomSheetDialog: Dialog = Dialog(myActivity)
        val dBinding = RechargeSuccessDialogBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
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

    override fun setData() {

    }

    override fun onSelectOperator(holder: RecyclerView.ViewHolder, model: List<Operator>, pos: Int) {
        val selectedOperator = model[pos]
        binding.edtOperatorName.setText(selectedOperator.name)
        operatorID = selectedOperator._id
        // Set the operator logo
        val operatorImage = getOperatorImage(operatorID)
        binding.ivOperatorImage.setImageDrawable(operatorImage)
        setOperatorLogo()
        // Dismiss the dialog
        alertDialog.dismiss()
    }

    private fun setOperatorLogo() {
        val operatorImage = when (operatorID) {
            "66680134061904fed64223ab" -> R.drawable.airtel_tv
            "666803ee061904fed64223b8" -> R.drawable.sun_direct
            "66680479061904fed64223bb" -> R.drawable.tata_sky
            "666804bf061904fed64223bf" -> R.drawable.v_d2h
            "66680198061904fed64223b4" -> R.drawable.dish_tv  // DishTV drawable
            else -> R.drawable.bsnl  // Default image
        }
        binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity, operatorImage))
    }

    private fun getOperatorImage(operatorID: String): Drawable? {
        return when(operatorID){
            "66617b5f3c35f52923782cc0" -> ContextCompat.getDrawable(myActivity, R.drawable.bsnl)
            "66617b5f3c35f52923782cc3" -> ContextCompat.getDrawable(myActivity, R.drawable.vi)
            "66617b5f3c35f52923782cbf" -> ContextCompat.getDrawable(myActivity, R.drawable.bsnl)
            "66617b5f3c35f52923782cbe" -> ContextCompat.getDrawable(myActivity, R.drawable.airtel)
            "66617b5f3c35f52923782cc2" -> ContextCompat.getDrawable(myActivity, R.drawable.jio)
            "66680134061904fed64223ab" -> ContextCompat.getDrawable(myActivity, R.drawable.airtel_tv)
            "66680198061904fed64223b4" -> ContextCompat.getDrawable(myActivity, R.drawable.dish_tv)
            "666803ee061904fed64223b8" -> ContextCompat.getDrawable(myActivity, R.drawable.sun_direct)
            "66680479061904fed64223bb" -> ContextCompat.getDrawable(myActivity, R.drawable.tata_sky)
            "666804bf061904fed64223bf" -> ContextCompat.getDrawable(myActivity, R.drawable.v_d2h)
            else -> ContextCompat.getDrawable(myActivity, R.drawable.bsnl)
        }
    }

}