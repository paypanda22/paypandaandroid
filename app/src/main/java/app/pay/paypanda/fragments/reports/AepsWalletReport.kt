package app.pay.paypanda.fragments.reports

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
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.adapters.AepsWalletAdapter
import app.pay.paypanda.databinding.FragmentAepsWalletReportBinding
import app.pay.paypanda.databinding.LytDmtFilterBinding
import app.pay.paypanda.helperclasses.ActivityExtensions
import app.pay.paypanda.helperclasses.CommonClass
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.responsemodels.aepsWalletReport.AepsWalletResponse
import app.pay.paypanda.responsemodels.aepsWalletReport.Wallet

import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson


class AepsWalletReport : BaseFragment<FragmentAepsWalletReportBinding>(FragmentAepsWalletReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    private var customerMobile = ""
    private var accountNumber = ""
    private lateinit var aepsWalletAdapter: AepsWalletAdapter
    private lateinit var aepsList:MutableList<Wallet>
    private var  selectedItem:String = ""
    var Types = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        aepswalletReport(start_date,end_date,count,"","","")
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }
    }

    override fun setData() {

    }
    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }
    private fun aepswalletReport(startDate: String, endDate: String, count: Int, txnID: String, orderID: String, type: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["max_amt"] = ""
        requestData["min_amt"] = ""
        requestData["sortType"] = ""
        requestData["txn_id"] = txnID
        requestData["type"] = type
        requestData["count"] = count
        requestData["page"] = 0
        requestData["order_id"] = orderID
        requestData["start_date"] = startDate
        requestData["end_date"] = endDate

        UtilMethods.aepswalletReport(requireContext(), token, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AepsWalletResponse = Gson().fromJson(message, AepsWalletResponse::class.java)

                if (!response.error) {
                    // Clear the list and check if response data contains wallet entries
                    aepsList = mutableListOf()

                    // Check if wallet list is empty
                    if (response.data.wallet.isNotEmpty()) {
                        aepsList.addAll(response.data.wallet)

                        // Setup RecyclerView adapter
                        aepsWalletAdapter = AepsWalletAdapter(myActivity, aepsList)
                        binding.rvDmtTransactions.adapter = aepsWalletAdapter
                        binding.rvDmtTransactions.layoutManager = LinearLayoutManager(myActivity)

                        // Show the RecyclerView and hide no data views
                        binding.rvDmtTransactions.visibility = VISIBLE
                        binding.llNoData.visibility = GONE
                        binding.imageView.visibility = GONE
                    } else {
                        // If the wallet list is empty, hide the RecyclerView and show the "No Data" layout
                        binding.rvDmtTransactions.visibility = GONE
                        binding.llNoData.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    }
                } else {
                    // If there's an error in the response
                    binding.rvDmtTransactions.visibility = GONE
                    binding.llNoData.visibility = VISIBLE
                    binding.imageView.visibility = GONE
                }
            }

            override fun fail(from: String) {
                // Handle failure by showing no data layout
                binding.rvDmtTransactions.visibility = GONE
                binding.llNoData.visibility = VISIBLE
                binding.imageView.visibility = GONE
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
        dBinding.aadharTv.visibility= GONE
        dBinding.orderID.visibility= VISIBLE
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)
        dBinding.typeOperator1.visibility= VISIBLE
        Types= listOf("Select Type","All","credit","debit")
        val adapterType= ArrayAdapter(myActivity, android.R.layout.simple_spinner_item,Types)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dBinding.typeOperator.adapter=adapterType
        dBinding.typeOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                if (position == 0) {
                    selectedItem=""
                }else{
                    selectedItem = Types[position]
                }
                // Toast.makeText(myActivity, "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
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
                aepswalletReport(start_date, end_date, count,  dBinding.txnId.text.toString(),dBinding.orderIdtv.text.toString(),selectedItem);
            }
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    }

