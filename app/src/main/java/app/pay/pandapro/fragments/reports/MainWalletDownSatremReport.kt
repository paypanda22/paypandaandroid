package app.pay.pandapro.fragments.reports

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.AadharPayAdapter
import app.pay.pandapro.adapters.WallletReportAdapter
import app.pay.pandapro.databinding.FragmentMainWalletDownSatremReportBinding
import app.pay.pandapro.databinding.FragmentWalletTransactionReportBinding
import app.pay.pandapro.databinding.LytDmtFilterBinding
import app.pay.pandapro.databinding.LytMainWalletFilterBinding
import app.pay.pandapro.helperclasses.ActivityExtensions
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.AdharPayReportClick
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.adharpayresponse.AadharPayResponse
import app.pay.pandapro.responsemodels.walletreport.Data
import app.pay.pandapro.responsemodels.walletreport.Wallet
import app.pay.pandapro.responsemodels.walletreport.WalletReportResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson


class MainWalletDownSatremReport : BaseFragment<FragmentMainWalletDownSatremReportBinding>(FragmentMainWalletDownSatremReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    private var txnID = ""

    private var id = ""
    private var  selectedItem:String = ""
    private var txnList = ArrayList<Wallet>()
    private lateinit var walletAdapter: WallletReportAdapter
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        id = arguments?.getString("value").toString()
        maiWallaet(start_date,end_date,count,txnID,selectedItem,id)
    }
    fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }
    override fun addListeners() {
binding.ivBack.setOnClickListener{
    findNavController().popBackStack()
}
        binding.ivMenu.setOnClickListener{
            openTransactionFilterDialog()
        }
    }

    private fun maiWallaet(startDate: String, endDate: String, count: Int,txnID:String,type:String,id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["count"] = count
        requestData["type"] = ""
        requestData["trans_type"] = type
        requestData["count"] = count
        requestData["page"] = 0
        requestData["id"] = id
        requestData["order_id"] = ""
        requestData["start_date"] = startDate
        requestData["end_date"] = endDate
        requestData["txn_id"] = txnID
        requestData["user_id"] = ""

        UtilMethods.mainwallet(requireContext(), token,requestData, object :
            MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: WalletReportResponse = Gson().fromJson(message, WalletReportResponse::class.java)
                if (!response.error) {
                    if (txnList.isNotEmpty()) {
                        txnList.clear()
                    }
                    txnList.addAll(response.data.wallet)

                    walletAdapter = WallletReportAdapter(myActivity, txnList)
                    binding.rvDmtTransactions.adapter = walletAdapter
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
    private fun openTransactionFilterDialog() {
        val filterDialog: Dialog = Dialog(myActivity)
       val dBinding = LytMainWalletFilterBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)

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
        val types = listOf("Select Type", "transfer", "reverse")
        val adapterType = ArrayAdapter(myActivity, android.R.layout.simple_spinner_item, types)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dBinding.typeOperator.adapter = adapterType
        dBinding.typeOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedItem = if (position == 0) {
                    ""
                } else {
                    types[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if necessary
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



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no item is selected if needed
            }
        }
        dBinding.btnSubmit.setOnClickListener {
            if (dBinding.edttxnID.text.toString()
                    .isNotEmpty() && !ActivityExtensions.isValidMobile(dBinding.edttxnID.text.toString())
            ) {
                dBinding.edttxnID.error = "Enter a Valid Txn Id"
            } else if (dBinding.edtFromDate.text.toString().isNotEmpty() && dBinding.edtToDate.text.toString().isEmpty()) {
                dBinding.edtToDate.error = "Select Date"
            } else {
                dBinding.apply {
                    if (edtFromDate.text.toString().isNotEmpty()) start_date = edtFromDate.text.toString()
                    if (edtToDate.text.toString().isNotEmpty()) end_date = edtToDate.text.toString()
                      }
                maiWallaet(start_date,end_date,count,dBinding.edttxnID.text.toString(),selectedItem,id)
            }
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }
    override fun setData() {

    }


}