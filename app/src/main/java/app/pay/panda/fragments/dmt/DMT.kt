package app.pay.panda.fragments.dmt

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.adapters.DmtBankAdapter
import app.pay.panda.databinding.FragmentDMTBinding
import app.pay.panda.databinding.LytDialogAddRecipientBinding
import app.pay.panda.databinding.LytFinalDmtBinding
import app.pay.panda.databinding.LytRvNameSearchItemListBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.BankListClickListner
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.addRecipient.AddRecipientResponse
import app.pay.panda.responsemodels.dmtBankList.DMTBankListResponse
import app.pay.panda.responsemodels.dmtBankList.Data
import app.pay.panda.responsemodels.dmtTransaction.DMTMakeTransactionResponse
import app.pay.panda.responsemodels.verifyBankName.VerifyBankResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import app.pay.panda.reusable.Utills.setOnTextChangedListener
import com.google.gson.Gson

class DMT(
    private val fragment: DMTFragment,
    private val activity: FragmentActivity,
    private val binding: FragmentDMTBinding,
    private val userSession: UserSession,
    private var apiID: String
) {
    fun updateApiID(newApiID: String) {
        this.apiID = newApiID
    }

    private var selectedChannel = 2
    private var bankID = ""
    private var bankList = ArrayList<Data>()
    private val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.fragment_navs) as NavHostFragment
    val navController = navHostFragment.navController


    @SuppressLint("SetTextI18n")
    fun openMakeTransactionDialog(
        recipient: app.pay.panda.responsemodels.dmtBeneficiaryList.Data,
        avlLimit: Int,
        customerName: String,
        customerMobile: String
    ) {
        val dmtDialog: Dialog = Dialog(activity)
        val dBinding = LytFinalDmtBinding.inflate(activity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
        dmtDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dmtDialog.setContentView(dBinding.root)
        dmtDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dmtDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dmtDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        dmtDialog.window?.setGravity(Gravity.BOTTOM)
        val verificationCharge = userSession.getIntData(Constant.VERIFICATION_CHARGE).toInt()
        dBinding.tvValidateText.text = "You will be charged $verificationCharge/- for beneficiary validation"

        dBinding.tvAcName.text = recipient.recipient_name
        dBinding.tvHolderMobile.text = recipient.recipient_mobile
        dBinding.tvAcNumber.text = recipient.account
        dBinding.tvBankName.text = recipient.bank
        dBinding.tvLimit.text = avlLimit.toString()
        dBinding.tvIfscCode.text = recipient.ifsc.toString()
        dBinding.edtAmount.setOnTextChangedListener { charSequence, i, i2, i3 ->
            if (charSequence.toString().length>1){
                val amount=charSequence.toString().toInt()
                if (amount<100 || amount>700){
                    dBinding.btnPay.isEnabled=false
                    dBinding.btnPay.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.bg_btn_grey))
                }else{
                    dBinding.btnPay.isEnabled=true
                    dBinding.btnPay.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.bg_btn_green))
                }
            }
        }

        if (recipient.isVerified) {
            dBinding.llVerify.visibility = GONE
            dBinding.tvStatus.text = "Verified"
            dBinding.ivStatus.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_check_green))
            dBinding.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.green_700))
        } else {
            dBinding.tvValidateBene.visibility = VISIBLE
            dBinding.tvValidateText.visibility = VISIBLE
            dBinding.tvStatus.text = "Not-Verified"
            dBinding.ivStatus.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_cancel_red))
            dBinding.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.red))
        }
        dBinding.rgMode.setOnCheckedChangeListener { group, checkedId ->
            selectedChannel = if (checkedId == R.id.imps) {
                2
            } else {
                1
            }
        }


        when (recipient.available_channel) {
            1 -> {
                dBinding.imps.isActivated = false
                dBinding.neft.isChecked = true
                selectedChannel = 2
            }

            2 -> {
                dBinding.neft.isActivated = false
                dBinding.imps.isChecked = true
                selectedChannel = 1
            }

            else -> {
                selectedChannel = 2
            }
        }
        dBinding.tvValidateBene.setOnClickListener {
            validateBeneficiaryAfter(dBinding, recipient)
        }

        dBinding.btnPay.setOnClickListener {
            if (dBinding.edtAmount.text.toString().isEmpty()) {
                showToast(activity,"Enter a Valid Amount")
            } else {
                val amt = dBinding.edtAmount.text?.toString()?.toInt() ?: 0
                if (amt > avlLimit) {
                    showToast(activity,"Amount should be less than available limit")
                } else {
                    if (dBinding.llMode.visibility == VISIBLE) {
                        dBinding.llTPin.visibility = VISIBLE
                        dBinding.llMode.visibility = GONE
                        dBinding.ivEdit.setOnClickListener {
                            if (dBinding.edtAmount.isEnabled) {
                                dBinding.edtAmount.isEnabled = false
                                dBinding.edtAmount.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.submitt_btn_dialog_light_grey))
                            } else {
                                dBinding.edtAmount.isEnabled = true
                                dBinding.edtAmount.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.submitt_btn_small_white))
                            }
                        }
                        dBinding.edtAmount.setTextColor(ContextCompat.getColor(activity, R.color.black))
                        dBinding.edtAmount.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.submitt_btn_dialog_light_grey))
                    } else {
                        if (dBinding.edtTPin.text.toString().length < 4) {
                            showToast(activity, "Enter Your Transaction Pin")
                        } else {
                            makeTransaction(recipient, dBinding, customerName, customerMobile, dmtDialog)
                        }

                    }

                }
            }


        }

        dmtDialog.setCancelable(true)
        dmtDialog.show()


    }


    private fun validateBeneficiaryAfter(dBinding: LytFinalDmtBinding, recipient: app.pay.panda.responsemodels.dmtBeneficiaryList.Data) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["name"] = recipient.recipient_name
        requestData["ifsc"] = recipient.ifsc
        requestData["phone"] = recipient.recipient_mobile
        requestData["bankAccount"] = recipient.account
        requestData["recipient_id"] = recipient.recipient_id
        requestData["user_id"] = token
        UtilMethods.verifyBankAccount(activity, requestData, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: VerifyBankResponse = Gson().fromJson(message, VerifyBankResponse::class.java)
                if (!response.error) {
                    Toast.makeText(activity, "Bank Verified Successfully", Toast.LENGTH_SHORT).show()
                    dBinding.tvAcName.text = response.data.data.bankName.toString()
                    dBinding.tvValidateBene.visibility = GONE
                    dBinding.tvValidateText.visibility = GONE
                    dBinding.tvStatus.text = "Verified"
                    dBinding.ivStatus.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_check_green))
                    dBinding.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.green_700))
                    dBinding.llVerify.visibility = GONE
                } else {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun makeTransaction(
        recipient: app.pay.panda.responsemodels.dmtBeneficiaryList.Data,
        dBinding: LytFinalDmtBinding,
        customerName: String,
        customerMobile: String,
        dmtDialog: Dialog
    ) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val latLong = userSession.getData(Constant.M_LAT).toString() + "," + userSession.getData(Constant.M_LONG).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["recipient_id"] = recipient.recipient_id.toString()
        requestData["amount"] = dBinding.edtAmount.text.toString().toInt()
        requestData["latlong"] = latLong
        requestData["channel"] = selectedChannel
        requestData["beneficiary_name"] = recipient.recipient_name
        requestData["bank_name"] = recipient.bank
        requestData["ifsc_code"] = recipient.ifsc
        requestData["account_number"] = recipient.account
        requestData["deviceID"] = CommonClass.getDeviceId(activity)
        requestData["customer_name"] = customerName
        requestData["customer_mobile"] = customerMobile
        requestData["bank_id"] = recipient.bankId
        requestData["api_id"] = apiID
        requestData["pincode"] = ""
        requestData["tpin"] = dBinding.edtTPin.text.toString()

        UtilMethods.dmtMakeTransaction(activity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DMTMakeTransactionResponse = Gson().fromJson(message, DMTMakeTransactionResponse::class.java)
                if (!response.error) {
                    if (response.data.isNotEmpty()) {
                        dmtTxnListDialog(response, dmtDialog)
                    } else {
                        ShowDialog.bottomDialogSingleButton(activity, "Transaction Initiated",
                            response.message, "pending", object : MyClick {
                                override fun onClick() {
                                    //dmtDialog.dismiss()
                                }
                            })
                    }
                } else {
                    ShowDialog.bottomDialogSingleButton(activity, "ERROR",
                        response.message, "error", object : MyClick {
                            override fun onClick() {
                              //  dmtDialog.dismiss()
                            }
                        })

                }

            }

            override fun fail(from: String) {
                ShowDialog.showDialog(activity, "ERROR",
                    "REQUEST FAILED", "error", object : MyClick {
                        override fun onClick() {
                           // dmtDialog.dismiss()
                        }
                    })
            }
        })

    }

    private fun dmtTxnListDialog(response: DMTMakeTransactionResponse, dmtDialog: Dialog) {
        dmtDialog.dismiss()
        val list = ArrayList<app.pay.panda.responsemodels.dmtTransaction.Data>()
        list.addAll(response.data)
        ShowDialog.bottomDialogDmtMakeTxn(
            activity, response.message, list, "success", object : MyClick {
                override fun onClick() {
                    binding.edtCustomerNumber.text?.clear()
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    fun openAddBeneficiaryAccount(customerMobile: String) {
        val dmtDialog: Dialog = Dialog(activity)
        val dBinding = LytDialogAddRecipientBinding.inflate(activity.layoutInflater)
//        dBinding.root.background =
//            ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
        dmtDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dmtDialog.setContentView(dBinding.root)
        dmtDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dmtDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dmtDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        dmtDialog.window?.setGravity(Gravity.BOTTOM)
        val verificationCharge = userSession.getIntData(Constant.VERIFICATION_CHARGE).toInt()
        dBinding.tvPromtCharges.text = "You will be charged $verificationCharge/- for beneficiary validation"

        dBinding.edtBankName.setOnClickListener {
            openBankListDialog(dBinding, dmtDialog)
        }
        dBinding.edtAccountNumber.setOnTextChangedListener { charSequence, i, i2, i3 -> dBinding.llVerify.visibility = VISIBLE }
        dBinding.edtName.setOnTextChangedListener { charSequence, i, i2, i3 -> dBinding.llVerify.visibility = VISIBLE }
        dBinding.edtIfsc.setOnTextChangedListener { charSequence, i, i2, i3 -> dBinding.llVerify.visibility = VISIBLE }
        dBinding.edtBankName.setOnTextChangedListener { charSequence, i, i2, i3 -> dBinding.llVerify.visibility = VISIBLE }

        dBinding.lytBankList.setEndIconOnClickListener { openBankListDialog(dBinding, dmtDialog) }


        dBinding.btnAddBeneficiary.setOnClickListener {
            if (validateData(dBinding)) {
                addNewRecipient(dBinding, customerMobile, dmtDialog)
            }
        }
        dBinding.ivCancel.setOnClickListener { dmtDialog.cancel() }
        dBinding.tvValidateBeneficiaryName.setOnClickListener {
            if (dBinding.edtName.text.toString().trim().isEmpty() || dBinding.edtName.text.toString().trim().isBlank()) {
                dBinding.edtName.error = "Enter Beneficiary Name"
            } else if (!ActivityExtensions.isValidIfsc(dBinding.edtIfsc.text.toString())) {
                dBinding.edtIfsc.error = "Enter a Valid IFSC Code"
            } else if (dBinding.edtAccountNumber.text.toString().isEmpty()) {
                dBinding.edtAccountNumber.error = "Enter Account Number"
            } else {
                validateBeneficiary(dBinding)
            }
        }


        dmtDialog.setCancelable(true)
        dmtDialog.show()

    }

    private fun validateBeneficiary(dBinding: LytDialogAddRecipientBinding) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val phone = userSession.getData(Constant.MOBILE).toString()
        val requestData = hashMapOf<String, Any>()
        requestData["name"] = dBinding.edtName.text.toString()
        requestData["ifsc"] = dBinding.edtIfsc.text.toString()
        requestData["phone"] = phone
        requestData["bankAccount"] = dBinding.edtAccountNumber.text.toString()
        requestData["recipient_id"] = ""
        requestData["user_id"] = token
        UtilMethods.verifyBankAccount(activity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: VerifyBankResponse = Gson().fromJson(message, VerifyBankResponse::class.java)
<<<<<<< Updated upstream
                dBinding.edtName.setText(response.data.bank_account_name)
                dBinding.llVerify.visibility = GONE
=======
                dBinding.edtName.setText(response.data.data.bankName)
>>>>>>> Stashed changes
                Toast.makeText(activity, "Bank Verified Successfully", Toast.LENGTH_SHORT).show()
            }

            override fun fail(from: String) {
                dBinding.edtName.error = ""
                Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addNewRecipient(dBinding: LytDialogAddRecipientBinding, customerMobile: String, dmtDialog: Dialog) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["customer_mobile"] = customerMobile
        requestData["recipient_id_type"] = "acc_ifsc"
        requestData["account_number"] = dBinding.edtAccountNumber.text.toString()
        requestData["ifsc"] = dBinding.edtIfsc.text.toString()
        requestData["bank_code"] = ""
        requestData["bank_id"] = bankID
        requestData["api_id"] = apiID
        requestData["recipient_name"] = dBinding.edtName.text.toString()
        requestData["recipient_mobile"] = userSession.getData(Constant.MOBILE).toString()
        requestData["user_id"] = token
        UtilMethods.addEkoRecipient(activity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AddRecipientResponse = Gson().fromJson(message, AddRecipientResponse::class.java)
                if (!response.error) {
                    ShowDialog.showDialog(activity,
                        "Congratulations!", response.message, "success", object : MyClick {
                            override fun onClick() {
                                fragment.getBeneficiaryList()
                                dmtDialog.dismiss()
                            }
                        })


                } else {
                    ShowDialog.showDialog(activity,
                        "ERROR !", response.message, "error", object : MyClick {
                            override fun onClick() {
                                dmtDialog.dismiss()
                            }
                        })
                }

            }

            override fun fail(from: String) {
                ShowDialog.showDialog(activity,
                    "ERROR !", "Request Failed ", "error", object : MyClick {
                        override fun onClick() {
                            dmtDialog.dismiss()
                        }
                    })
            }
        })
    }

    private fun validateData(dBinding: LytDialogAddRecipientBinding): Boolean {
        if (dBinding.edtName.text.toString().trim().isEmpty()) {
            dBinding.edtName.error = "Enter Name"
            return false
        } else if (dBinding.edtBankName.text.toString().isEmpty()) {
            Toast.makeText(activity, "Select Bank First", Toast.LENGTH_SHORT).show()
            return false
        } else if (!ActivityExtensions.isValidIfsc(dBinding.edtIfsc.text.toString())) {
            dBinding.edtIfsc.error = "Enter a Valid IFSC Code"
            return false
        } else if (dBinding.edtAccountNumber.text.toString().isEmpty()) {
            dBinding.edtAccountNumber.error = "Enter Bank Account Number"
            return false
        } else {
            return true
        }
    }

    private fun openBankListDialog(dBinding: LytDialogAddRecipientBinding, dmtDialog: Dialog) {
        UtilMethods.dmtBankList(activity, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DMTBankListResponse = Gson().fromJson(message, DMTBankListResponse::class.java)
                if (response.data.isEmpty()) {
                    ShowDialog.showDialog(activity, "Unable to Fetch bank List", from, "error", object : MyClick {
                        override fun onClick() {
                            dmtDialog.dismiss()
                            navController.popBackStack()
                        }
                    })
                } else {
                    if (bankList.isNotEmpty()) {
                        bankList.clear()
                    }
                    bankList.addAll(response.data)
                    openBankDialog(dBinding)
                }

            }

            override fun fail(from: String) {
                ShowDialog.showDialog(activity, "Unable to Fetch bank List", from, "error", object : MyClick {
                    override fun onClick() {
                        dmtDialog.dismiss()
                        navController.popBackStack()
                    }
                })
            }
        })
    }

    private fun openBankDialog(dBinding: LytDialogAddRecipientBinding) {
        val bankListDialog: Dialog = Dialog(activity)
        val bankListBinding = LytRvNameSearchItemListBinding.inflate(activity.layoutInflater)
        bankListBinding.root.background =
            ContextCompat.getDrawable(activity, R.drawable.curved_background_with_shadow)
        bankListDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bankListDialog.setContentView(bankListBinding.root)
        bankListDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bankListDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bankListDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        bankListDialog.window?.setGravity(Gravity.BOTTOM)
        bankListBinding.ivDismiss.setOnClickListener { bankListDialog.dismiss() }
        val clickListener = object : BankListClickListner {
            override fun onItemClicked(
                holder: RecyclerView.ViewHolder,
                model: List<Data>,
                pos: Int
            ) {
                dBinding.edtBankName.setText(model[pos].bank_name)
                bankID = model[pos]._id.toString()
                dBinding.edtIfsc.setText(model[pos].ifsc_code)
                bankListDialog.dismiss()
            }
        }
        val adapter = DmtBankAdapter(activity, bankList, clickListener)
        bankListBinding.rvList.adapter = adapter
        bankListBinding.rvList.layoutManager = LinearLayoutManager(activity)

        bankListBinding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(bankListBinding.edtName.text.toString())
                val itemCount = bankListBinding.rvList.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    bankListBinding.llNoData.visibility = GONE
                    bankListBinding.rvList.visibility = VISIBLE
                } else {
                    bankListBinding.llNoData.visibility = VISIBLE
                    bankListBinding.rvList.visibility = GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        bankListDialog.setCancelable(false)
        bankListDialog.show()

    }
}