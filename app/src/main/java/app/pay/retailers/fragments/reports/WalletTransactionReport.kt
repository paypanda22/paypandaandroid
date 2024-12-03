package app.pay.retailers.fragments.reports

import android.R
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.retailers.BaseFragment
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.WalletTxnAdapter
import app.pay.retailers.databinding.FragmentWalletTransactionReportBinding
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.walletTxn.Wallet
import app.pay.retailers.responsemodels.walletTxn.WalletTxnReportResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class WalletTransactionReport : BaseFragment<FragmentWalletTransactionReportBinding>(FragmentWalletTransactionReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var list=ArrayList<Wallet>()
    var transactionTypes = listOf<String>()
    private var  selectedItem:String = ""
    private var  selectedItemtype:String = ""
    var Types = listOf<String>()
    private var count= 25
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
       getTransactionList(start_date,end_date, count,"","","","")
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        binding.edtFromDate.setText(todayDate)
        binding.edtToDate.setText(todayDate)

        binding.edtFromDate.setOnClickListener { CommonClass.showDatePickerDialog(myActivity, binding.edtFromDate) }
        binding.edtToDate.setOnClickListener {
            if (binding.edtFromDate.text.toString().isEmpty()) {
                binding.edtFromDate.error = "Select From Date"
            } else {
                CommonClass.showDatePickerDialog(myActivity, binding.edtToDate)
            }
        }
    }

    private fun getTransactionList(startDate:String,endDate:String,count:Int,order_id:String,trans_type:String,txn_id:String,type:String) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["count"]=count
        requestData["page"]=0
        requestData["start_date"]=startDate
        requestData["end_date"]=endDate
        requestData["order_id"]=order_id
        requestData["trans_type"]=trans_type
        requestData["txn_id"]=txn_id
        requestData["type"]=type
        UtilMethods.walletTxnReport(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
               val response:WalletTxnReportResponse=Gson().fromJson(message,WalletTxnReportResponse::class.java)
                if (!response.error){
                    if (list.isNotEmpty()){
                        list.clear()
                    }
                    list.addAll(response.data.wallet)
                    val adapter=WalletTxnAdapter(myActivity,list)
                    binding.rvWalletTransactions.adapter=adapter
                    binding.rvWalletTransactions.layoutManager=LinearLayoutManager(myActivity)




                    binding.llNoData.visibility=View.GONE
                    binding.llFilter.visibility=View.GONE
                    binding.rvWalletTransactions.visibility=View.VISIBLE
                    binding.imageView.visibility=View.GONE
                }else{
                    binding.llNoData.visibility=View.VISIBLE
                    binding.llFilter.visibility=View.GONE
                    binding.rvWalletTransactions.visibility=View.GONE
                    binding.imageView.visibility=View.GONE
                }
            }

            override fun fail(from: String) {
                binding.llNoData.visibility=View.VISIBLE
                binding.llFilter.visibility=View.GONE
                binding.rvWalletTransactions.visibility=View.GONE
                binding.imageView.visibility=View.GONE
            }
        })

    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivMenu.setOnClickListener {

            toggleFilterLayout()
            transactionTypes = listOf("Select Transaction Type",
                "refund",
                "recharge",
                "transfer",
                "order",
                "bill_pay",
                "pan",
                "fastag",
                "credit_card",
                "lic_bill",
                "qr_code",
                "add-wallet",
                "onBoarding",
                "fromAEPS",
                "reversal",
                "ureversal",
                "ucommission",
                "validate_beneficiary",
                "dmt",
                "dmt_rev",
                "admintrf",
                "cms"
            )
            val adapter = ArrayAdapter(myActivity, R.layout.simple_spinner_item, transactionTypes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerServiceOperator.adapter = adapter


            Types= listOf("Select Type","All","credit","debit")
            val adapterType=ArrayAdapter(myActivity,R.layout.simple_spinner_item,Types)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.typeOperator.adapter=adapterType
        }




        binding.spinnerServiceOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    selectedItemtype = ""
                } else if (position < transactionTypes.size) {
                    selectedItemtype = transactionTypes[position]
                } else {
                    // Handle out-of-bounds case if necessary
                    Log.e("SpinnerError", "Invalid position: $position")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if necessary
            }
        }

        binding.typeOperator.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(position==0){
                    selectedItem=""
                }else {
                    selectedItem = Types[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if necessary
            }
        }

        binding.rgCount.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                app.pay.retailers.R.id.rb25 -> {
                    count = 25
                }

                app.pay.retailers.R.id.rb50 -> {
                    count =50
                }

                app.pay.retailers.R.id.rb100 -> {
                    count = 100
                }

                app.pay.retailers.R.id.rb150 -> {
                    count = 150
                }

                app.pay.retailers.R.id.rb200 -> {
                    count = 200
                }
                app.pay.retailers.R.id.more -> {
                    count=900
                }
            }
        }
        binding.tvFilterTransactions.setOnClickListener{

            getTransactionList(binding.edtFromDate.text.toString(),binding.edtToDate.text.toString(),count,
                binding.orderID.text.toString(),selectedItemtype,binding.txnId.text.toString(), selectedItem)
        }
    }

    private fun toggleFilterLayout() {
        if (binding.llFilter.visibility == View.VISIBLE) {
            // If the filter layout is currently visible
            binding.llFilter.visibility = View.GONE // Hide the filter layout
        } else {
            // If the filter layout is not visible
            binding.llFilter.visibility = View.VISIBLE // Show the filter layout
            binding.llNoData.visibility = View.GONE // Ensure no data layout is hidden
        }
    }


    override fun setData() {
        val liveDate=CommonClass.getLiveTime("yyyy-MM-dd")
        binding.edtFromDate.setText(liveDate)
        binding.edtToDate.setText(liveDate)

    }


}