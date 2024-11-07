package app.pay.pandapro.fragments.reports

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.AadharPayAdapter
import app.pay.pandapro.databinding.FragmentAadharPayReportBinding
import app.pay.pandapro.databinding.LytDmtFilterBinding
import app.pay.pandapro.fragments.SingleAadhaarPayInvoice
import app.pay.pandapro.helperclasses.ActivityExtensions
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.AdharPayReportClick
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.adharpayresponse.AadharPayResponse
import app.pay.pandapro.responsemodels.adharpayresponse.Data
import app.pay.pandapro.responsemodels.aepsenquiry.AepsEnquiryResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson

class AadharPayReport : BaseFragment<FragmentAadharPayReportBinding>(FragmentAadharPayReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    private var customerMobile = ""
    private var accountNumber = ""
    private lateinit var aadharPayAdapter: AadharPayAdapter
    private var txnList = ArrayList<Data>()
    private var  selectdStatus:String = ""
    var Types = listOf<String>()
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        aadharPayReport(start_date, end_date, count,"","",selectdStatus)
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }
    }
    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }
    private fun aadharPayReport(startDate: String, endDate: String, count: Int,txnID:String,AadharNo:String,status:String) {

        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["adhaar_no"] = AadharNo
        requestData["customer_mobile"] = ""
        requestData["count"] = count
        requestData["page"] = 0
        requestData["status"] = status
        requestData["txn_id"] = txnID
        requestData["start_date"] = startDate
        requestData["end_date"] = endDate

        UtilMethods.aadharPayReport(requireContext(), token,requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AadharPayResponse = Gson().fromJson(message, AadharPayResponse::class.java)
                if (!response.error) {
                    if (txnList.isNotEmpty()) {
                        txnList.clear()
                    }
                    txnList.addAll(response.data)
                    val clickListner = object : AdharPayReportClick {
                        override fun onItemClicked(
                            holder: RecyclerView.ViewHolder,
                            model: List<Data>,
                            pos: Int,
                            stsus: String
                        ) {
                            when (stsus) {
                                "1" -> {
                                    adhaarPayEnquiry(model[pos]._id)
                                }

                                "2" -> {
                                    openShareReceipt(model[pos]._id)
                                }

                                else -> {
                                    adhaarPayEnquiry(model[pos]._id)
                                }
                            }
                        }
                    }
                    aadharPayAdapter = AadharPayAdapter(myActivity, txnList,clickListner)
                    binding.rvDmtTransactions.adapter = aadharPayAdapter
                    binding.rvDmtTransactions.layoutManager = LinearLayoutManager(myActivity)

                    binding.rvDmtTransactions.visibility = VISIBLE
                    binding.llNoData.visibility = GONE
                    binding.imageView.visibility = GONE


                } else {
                    binding.rvDmtTransactions.visibility = GONE
                    binding.llNoData.visibility = VISIBLE
                    binding.imageView.visibility = GONE
                }
            }

            override fun fail(from: String) {
                binding.rvDmtTransactions.visibility = GONE
                binding.llNoData.visibility = VISIBLE
                binding.imageView.visibility = GONE
            }
        })
    }
    private fun adhaarPayEnquiry(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.adhaarPayEnquiry(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: AepsEnquiryResponse = Gson().fromJson(message, AepsEnquiryResponse::class.java)
                if (!response.error) {
                    aadharPayAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openTransactionFilterDialog() {
        val filterDialog: Dialog = Dialog(myActivity)
        val dBinding = LytDmtFilterBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.edtCustomerNumber.hint = null // Clear any existing hint
        dBinding.edtCustomerNumber.hint = "Filter by Aadhaar no" // Set dynamic hint
        dBinding.edtAccountNumber1.visibility= GONE
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)
        dBinding.status1.visibility= VISIBLE
        Types = listOf("All", "Pending", "Success", "Failed", "Refunded")
        val TypeValues = listOf("", "1", "2", "3", "4") // Corresponding values for each type

        val adapterType = ArrayAdapter(myActivity, android.R.layout.simple_spinner_item, Types)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dBinding.status.adapter = adapterType

        dBinding.status.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected label and value based on position
                val selectedLabel = Types[position]
                val selectedValue = TypeValues[position]

                // Use selectedValue as needed, for example:
                selectdStatus = selectedValue
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if necessary
            }
        }
        dBinding.edtFromDate.setOnClickListener { CommonClass.showDatePickerDialog(myActivity, dBinding.edtFromDate) }
        dBinding.edtToDate.setOnClickListener {
            if (dBinding.edtFromDate.text.toString().isEmpty()) {
                dBinding.edtFromDate.error = "Select From Date"
            } else {
                CommonClass.showDatePickerDialog(myActivity, dBinding.edtToDate)
            }
        }

        dBinding.rgCount.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb25 -> {
                    count = 25
                }

                R.id.rb50 -> {
                    count =50
                }

                R.id.rb100 -> {
                    count = 100
                }

                R.id.rb150 -> {
                    count = 150
                }

                R.id.rb200 -> {
                    count = 200
                }
                R.id.more -> {
                    count=400
                }
            }
        }
        dBinding.btnSubmit.setOnClickListener {
            if (dBinding.edtCustomerNumber.text.toString()
                    .isNotEmpty() && !ActivityExtensions.isValidMobile(dBinding.edtCustomerNumber.text.toString())
            ) {
                dBinding.edtCustomerNumber.error = "Enter a Valid Mobile"
            } else if (dBinding.edtFromDate.text.toString().isNotEmpty() && dBinding.edtToDate.text.toString().isEmpty()) {
                dBinding.edtToDate.error = "Select To Date"
            } else {
                dBinding.apply {
                    if (edtFromDate.text.toString().isNotEmpty()) start_date = edtFromDate.text.toString()
                    if (edtToDate.text.toString().isNotEmpty()) end_date = edtToDate.text.toString()
                    if (edtCustomerNumber.text.toString().isNotEmpty()) customerMobile = edtCustomerNumber.text.toString()
                    if (edtAccountNumber.text.toString().isNotEmpty()) accountNumber = edtAccountNumber.text.toString()
                }
                aadharPayReport(start_date, end_date, count,  dBinding.txnId.text.toString(),dBinding.edtCustomerNumber.text.toString(),selectdStatus);
            }
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    override fun setData() {

    }
    private fun openShareReceipt(id: String) {
        val bottomSheet = SingleAadhaarPayInvoice()
        val bundle = Bundle()
        bundle.putString("id", id)
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
}