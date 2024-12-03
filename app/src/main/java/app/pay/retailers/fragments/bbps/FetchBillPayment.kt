package app.pay.retailers.fragments.bbps

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.databinding.DialogFetchBillBinding
import app.pay.retailers.databinding.FragmentFetchBillPaymentBinding
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.interfaces.OnBackPressedListner
import app.pay.retailers.responsemodels.billpayresponse.BillPayResponse
import app.pay.retailers.responsemodels.fetchBill.FetchBillResponse
import app.pay.retailers.responsemodels.getOperators.CustomerParam
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class FetchBillPayment : BaseFragment<FragmentFetchBillPaymentBinding>(FragmentFetchBillPaymentBinding::inflate), OnBackPressedListner {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var params: ArrayList<CustomerParam>? = null
    private var name: String = ""
    private var operatorid = ""
    private var paymentAmountExactness = ""
    private var fetchRequirement = ""
    private var catName = ""
    private var catID = ""
    private lateinit var inputData: MutableList<app.pay.retailers.helperclasses.CustomerParam>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        params = arguments?.getParcelableArrayList("params")
        name = arguments?.getString("name").toString()
        operatorid = arguments?.getString("operatorid").toString()
        paymentAmountExactness = arguments?.getString("paymentAmountExactness").toString()
        fetchRequirement = arguments?.getString("fetchRequirement").toString()
        catName = arguments?.getString("catName").toString()
        catID = arguments?.getString("catID").toString()

    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.rlSubmit.setOnClickListener {
            if (validate()) {
                fetchBillAPI()
            }
        }

    }

    private fun fetchBillAPI() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        val inputDataArray = getInputParams()
        if (inputDataArray.isNotEmpty()) {
            val requestData = hashMapOf<String, Any?>()
            requestData["billerId"] = operatorid
            requestData["customParams"] = inputDataArray
            requestData["user_id"] = token

            UtilMethods.fetchBill(requireContext(), requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: FetchBillResponse = Gson().fromJson(message, FetchBillResponse::class.java)
                    if (!response.error) {
                        openBillFetchDialog(response)
                    } else {
                        showToast(requireContext(), response.message)
                    }

                }

                override fun fail(from: String) {
                    showToast(requireContext(), from)
                }
            })
        } else {
            showToast(requireContext(), "Please Input Required Fields")
        }


    }

    private fun getInputParams(): List<app.pay.retailers.helperclasses.CustomerParam> {
        inputData = mutableListOf()
        inputData.add(app.pay.retailers.helperclasses.CustomerParam(params?.get(0)?.paramName.toString(), binding.edtAd1.text.toString()))

        if (params?.size!! > 1 && params?.get(1)?.optional == false && binding.edtAd2.text.toString().isNotEmpty()) {
            inputData.add(app.pay.retailers.helperclasses.CustomerParam(params?.get(1)?.paramName.toString(), binding.edtAd2.text.toString()))
        }

        if (params?.size!! > 2 && params?.get(2)?.optional == false && binding.edtAd3.text.toString().isNotEmpty()) {
            inputData.add(app.pay.retailers.helperclasses.CustomerParam(params?.get(2)?.paramName.toString(), binding.edtAd3.text.toString()))
        }

        if (params?.size!! > 3 && params?.get(3)?.optional == false && binding.edtAd4.text.toString().isNotEmpty()) {
            inputData.add(app.pay.retailers.helperclasses.CustomerParam(params?.get(3)?.paramName.toString(), binding.edtAd4.text.toString()))
        }

        if (params?.size!! > 4 && params?.get(4)?.optional == false && binding.edtAd5.text.toString().isEmpty()) {
            inputData.add(app.pay.retailers.helperclasses.CustomerParam(params?.get(4)?.paramName.toString(), binding.edtAd5.text.toString()))
        }

        return inputData
    }

    private fun openBillFetchDialog(response: FetchBillResponse) {
        val bottomSheetDialog: Dialog = Dialog(myActivity)
        val dBinding = DialogFetchBillBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        bottomSheetDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bottomSheetDialog.setContentView(dBinding.root)
        bottomSheetDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        bottomSheetDialog.window?.setGravity(Gravity.BOTTOM)

        if (catID == "667e8a8aa0cc9372aaceb009") {
            dBinding.finalAmount.apply {
                isFocusable = true
                isFocusableInTouchMode = true
                isClickable = true
                isEnabled = true
                requestFocus()

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val text = s.toString().trim()
                        // Disable the button if the text is empty or "0"
                        dBinding.btnPay.isEnabled = text.isNotEmpty() && text != "0"
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }

        dBinding.opName.text = name
        dBinding.tvBillAmount.text = response.data.billerResponse.amount.toString()
        dBinding.tvCustomerName.text = response.data.billerResponse.customerName
        dBinding.tvDueDate.text = response.data.billerResponse.dueDate
        dBinding.refId.text = response.data.refId
        dBinding.finalAmount.setText(response.data.billerResponse.amount.toString())


        dBinding.btnPay.setOnClickListener {
            val transactionPin = dBinding.edtOtpLogin.text.toString()
            if (transactionPin.length < 4) {
                showToast(requireContext(), "Enter Your Transaction Pin")
            } else {
                payBill2(transactionPin, dBinding.finalAmount.text.toString(), bottomSheetDialog, response)
            }
        }
       // bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.show()
    }

    private fun payBill2(tPIN: String, amount: String, bottomSheetDialog: Dialog, response: FetchBillResponse) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val inputDataArray = getInputParams()

        val requestData = hashMapOf<String, Any?>()
        requestData["billerId"] = operatorid
        requestData["customParams"] = inputDataArray
        requestData["amount"] = amount
        requestData["refId"] = response.data.refId
        requestData["billerResponse"] = response.data.billerResponse
        requestData["user_id"] = token
        requestData["tpin"] = tPIN

        UtilMethods.payBill(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val billPayResponse: BillPayResponse = Gson().fromJson(message, BillPayResponse::class.java)
                if (!billPayResponse.error) {
                    if (billPayResponse.data.status == 2) {
                        ShowDialog.bbpsSuccess(myActivity, billPayResponse.data, response.message, object : MyClick {
                            override fun onClick() {
                                bottomSheetDialog.dismiss()
                                binding.edtAd1.text?.clear()
                                binding.edtAd2.text?.clear()
                                binding.edtAd3.text?.clear()
                                binding.edtAd4.text?.clear()
                                binding.edtAd5.text?.clear()
                            }
                        })
                    } else {
                        ShowDialog.bottomDialogSingleButton(myActivity, billPayResponse.message, billPayResponse.data.message, "success", object : MyClick {
                            override fun onClick() {

                            }
                        })
                    }
                } else {
                    Toast.makeText(myActivity, billPayResponse.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "FAILED", from, "error", object : MyClick {
                    override fun onClick() {
                        bottomSheetDialog.dismiss()
                    }
                })
            }
        })
    }

    private fun validate(): Boolean {
        if (binding.edtAd1.text.toString().isEmpty()) {
            binding.edtAd1.error = "Enter a Valid" + params?.get(0)?.paramName
            return false
        } else {
            return if (params?.size!! > 1 && params?.get(1)?.optional == false && binding.edtAd2.text.toString().isEmpty()) {
                binding.edtAd2.error = "Enter a Valid" + params?.get(1)?.paramName
                false
            } else if (params?.size!! > 2 && params?.get(2)?.optional == false && binding.edtAd3.text.toString().isEmpty()) {
                binding.edtAd3.error = "Enter a Valid" + params?.get(2)?.paramName
                false
            } else if (params?.size!! > 3 && params?.get(3)?.optional == false && binding.edtAd4.text.toString().isEmpty()) {
                binding.edtAd4.error = "Enter a Valid" + params?.get(3)?.paramName
                false
            } else if (params?.size!! > 4 && params?.get(4)?.optional == false && binding.edtAd5.text.toString().isEmpty()) {
                binding.edtAd4.error = "Enter a Valid" + params?.get(3)?.paramName
                false
            } else {
                true
            }
        }

    }

    override fun setData() {
        binding.tvBillerName.text = name

        binding.lytAd1.hint = params?.get(0)?.paramName.toString()
        binding.edtAd1.requestFocus()
        if (params?.get(0)?.dataType == "NUMERIC") {
            binding.edtAd1.inputType = InputType.TYPE_CLASS_NUMBER
        } else {
            binding.edtAd1.inputType = InputType.TYPE_CLASS_TEXT
        }

        if (params?.size!! > 1) {
            binding.lytAd2.visibility = VISIBLE
            if (params?.get(1)?.optional == true) {
                binding.lytAd2.hint = params?.get(1)?.paramName.toString() + " (Optional)"
            } else {
                binding.lytAd2.hint = params?.get(1)?.paramName.toString()
            }
            if (params?.get(1)?.dataType == "NUMERIC") {
                binding.edtAd2.inputType = InputType.TYPE_CLASS_NUMBER
            } else {
                binding.edtAd2.inputType = InputType.TYPE_CLASS_TEXT
            }
            if (params?.size!! > 2) {
                binding.lytAd3.visibility = VISIBLE
                if (params?.get(2)?.optional == true) {
                    binding.lytAd3.hint = params?.get(2)?.paramName.toString() + " (Optional)"
                } else {
                    binding.lytAd3.hint = params?.get(2)?.paramName.toString()
                }
                if (params?.get(2)?.dataType == "NUMERIC") {
                    binding.edtAd3.inputType = InputType.TYPE_CLASS_NUMBER
                } else {
                    binding.edtAd3.inputType = InputType.TYPE_CLASS_TEXT
                }
                if (params?.size!! > 3) {
                    binding.lytAd4.visibility = VISIBLE
                    if (params?.get(3)?.optional == true) {
                        binding.lytAd4.hint = params?.get(3)?.paramName.toString() + " (Optional)"
                    } else {
                        binding.lytAd4.hint = params?.get(3)?.paramName.toString()
                    }
                    if (params?.get(3)?.dataType == "NUMERIC") {
                        binding.edtAd4.inputType = InputType.TYPE_CLASS_NUMBER
                    } else {
                        binding.edtAd4.inputType = InputType.TYPE_CLASS_TEXT
                    }

                    if (params?.size!! > 4) {
                        binding.lytAd5.visibility = VISIBLE
                        if (params?.get(4)?.optional == true) {
                            binding.lytAd5.hint = params?.get(4)?.paramName.toString() + " (Optional)"
                        } else {
                            binding.lytAd5.hint = params?.get(4)?.paramName.toString()
                        }
                        if (params?.get(4)?.dataType == "NUMERIC") {
                            binding.edtAd5.inputType = InputType.TYPE_CLASS_NUMBER
                        } else {
                            binding.edtAd5.inputType = InputType.TYPE_CLASS_TEXT
                        }
                    }
                }
            }
        }

    }

    override fun backPressed() {
        showToast(requireContext(), "BackPressed")
    }

}