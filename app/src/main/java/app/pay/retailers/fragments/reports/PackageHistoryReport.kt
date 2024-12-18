package app.pay.retailers.fragments.reports

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.PackageReportAdapter
import app.pay.retailers.databinding.FragmentPackageHistoryReportBinding
import app.pay.retailers.databinding.LytDmtFilterBinding
import app.pay.retailers.helperclasses.ActivityExtensions
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.packageListResponse.PackageListResponse
import app.pay.retailers.responsemodels.packagehistory.Data
import app.pay.retailers.responsemodels.packagehistory.PackageHistoryReportResponse

import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class PackageHistoryReport : BaseFragment<FragmentPackageHistoryReportBinding>(FragmentPackageHistoryReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    var selectedServiceId=""
    private lateinit var dBinding: LytDmtFilterBinding
    private lateinit var packageList: MutableList<app.pay.retailers.responsemodels.packageListResponse.Data>
    private var customerMobile = ""
    private var accountNumber = ""
    private var txnList = ArrayList<Data>()

    private lateinit var packageReportAdapter: PackageReportAdapter
    override fun init() {
        nullActivityCheck()
        dBinding = LytDmtFilterBinding.inflate(myActivity.layoutInflater)

        userSession = UserSession(requireContext())
        packagePaymenthistory(start_date, end_date, count,"")

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }
        binding.swipeRefreshLayout.setOnRefreshListener{
            packagePaymenthistory(start_date, end_date, count,"")
        }
    }

    override fun setData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun packagePaymenthistory(start_date: String, endDate: String, count: Int, txnID: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        binding.swipeRefreshLayout.isRefreshing = true
        UtilMethods.packagePaymenthistory(requireContext(), start_date, endDate, txnID, "0", count.toString(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PackageHistoryReportResponse = Gson().fromJson(message, PackageHistoryReportResponse::class.java)
                if (!response.error) {
                    txnList.clear()

                    if (response.data.isNotEmpty()) {
                        txnList.addAll(response.data)
                        binding.swipeRefreshLayout.isRefreshing = false
                        packageReportAdapter = PackageReportAdapter(myActivity, txnList)
                        binding.rvDmtTransactions.adapter = packageReportAdapter
                        binding.rvDmtTransactions.layoutManager = LinearLayoutManager(myActivity)

                        binding.rvDmtTransactions.visibility = VISIBLE
                        binding.llNoData.visibility = GONE
                        binding.imageView.visibility = GONE
                    } else {
                        binding.swipeRefreshLayout.isRefreshing = false
                        // If response data is empty, show "No Data Found" message
                        binding.rvDmtTransactions.visibility = GONE
                        binding.llNoData.visibility = VISIBLE
                        binding.imageView.visibility = GONE  // Assuming this is part of the "No Data Found" layout
                    }
                } else {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.rvDmtTransactions.visibility = GONE
                    binding.llNoData.visibility = VISIBLE
                    binding.imageView.visibility = GONE  // Assuming this is part of the "No Data Found" layout
                }
            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.rvDmtTransactions.visibility = GONE
                binding.llNoData.visibility = VISIBLE
                binding.imageView.visibility = GONE  // Assuming this is part of the "No Data Found" layout
            }
        })

}
    private fun openTransactionFilterDialog() {
        val filterDialog: Dialog = Dialog(myActivity)
         dBinding = LytDmtFilterBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)

        dBinding.edtCustomerNumber.visibility= GONE // Clear any existing hint
        dBinding.txnIdlyt.visibility= GONE // Clear any existing hint
        dBinding.referIdtv.visibility= VISIBLE
        dBinding.edtAccountNumber1.visibility= GONE
        getPackageList()
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)

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
        dBinding.spinnerServiceOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {


                selectedServiceId =packageList[position]._id.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no item is selected if needed
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
                packagePaymenthistory(start_date, end_date, count,selectedServiceId);
            }
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }
    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }
    private fun getPackageList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getPackageList(myActivity, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PackageListResponse = Gson().fromJson(message, PackageListResponse::class.java)
                if (response.error == false) {
                    if (response.data?.isNotEmpty() == true) {
                        packageList = mutableListOf()
                        packageList.addAll(response.data)
                        val bbpsServiceNames = response.data.map { it.package_name }
                        val adapter = ArrayAdapter(myActivity, android.R.layout.simple_spinner_item, bbpsServiceNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        dBinding.spinnerServiceOperator.adapter = adapter

                    } else {
                        // Handle empty data scenario
                    }
                } else {
                    // Handle error scenario
                }
            }

            override fun fail(from: String) {
                // Handle failure
            }
        })
    }
}