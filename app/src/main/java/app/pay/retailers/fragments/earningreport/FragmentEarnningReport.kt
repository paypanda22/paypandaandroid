package app.pay.retailers.fragments.earningreport

import android.content.Intent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.EarningReportAdapter


import app.pay.retailers.databinding.FragmentEarnningReportBinding
import app.pay.retailers.helperclasses.CustomProgressBar
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse

import app.pay.retailers.responsemodels.earningreport.EarningReportResponse
import app.pay.retailers.responsemodels.earningreport.Wallet

import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class FragmentEarnningReport : BaseFragment<FragmentEarnningReportBinding>(FragmentEarnningReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var earningreportlist: MutableList<Wallet>
    private lateinit var adapter: EarningReportAdapter
    private var page = "0"
    private var count = 50
    var transactionTypes = listOf<String>()
    private var  selectedItem:String = ""
    private var  selectedItemtype:String = ""
    var Types = listOf<String>()
    private var start_date = ""
    private var end_date = ""
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        earningReport(start_date,end_date,count,"","")
    }
    private fun earningReport(start_date:String,end_date:String,count:Int,txn_id:String,trans_type:String) {
        val progressBar = CustomProgressBar()
        progressBar.showProgress(requireContext())
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["count"] = count // Example integer value
        requestData["end_date"] = end_date
        requestData["max_amt"] = 0 // Example integer value
        requestData["min_amt"] = 0 // Example integer value
        requestData["order_id"] = txn_id
        requestData["page"] = 0 // Example integer value
        requestData["sortKey"] = ""
        requestData["sortType"] = ""
        requestData["start_date"] = start_date
        requestData["trans_type"] = trans_type
        requestData["txn_id"] = ""
        requestData["type"] = ""
            requestData["user_id"] = token
        UtilMethods.earningReport(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                progressBar.hideProgress()
                val response: EarningReportResponse =
                    Gson().fromJson(message, EarningReportResponse::class.java)
                if (!response.error){
                    progressBar.hideProgress()
                    earningreportlist= mutableListOf()
                    earningreportlist.addAll(response.data.wallet)
                     adapter= EarningReportAdapter(myActivity,earningreportlist)
                    binding.recydlerlist.adapter=adapter
                    binding.recydlerlist.layoutManager=LinearLayoutManager(myActivity)


                    binding.recydlerlist.visibility= VISIBLE
                    binding.llNoData.visibility= GONE
                    binding.imageView.visibility= GONE
                }else{
                    progressBar.hideProgress()
                    binding.recydlerlist.visibility= GONE
                    binding.llNoData.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }
            }

            override fun fail(from: String) {
                progressBar.hideProgress()
                binding.recydlerlist.visibility= GONE
                binding.llNoData.visibility= VISIBLE
                binding.imageView.visibility= GONE
            }
        })
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
      /*  binding.edtSearchearning.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)

                val itemCount = binding.recydlerlist.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    binding.llNoData.visibility = View.GONE
                    binding.recydlerlist.visibility = View.VISIBLE
                } else {
                    binding.llNoData.visibility = View.VISIBLE
                    binding.recydlerlist.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })*/
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
            val adapter = ArrayAdapter(myActivity, android.R.layout.simple_spinner_item, transactionTypes)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerServiceOperator.adapter = adapter


        }



        binding.tvFilterTransactions.setOnClickListener{
            earningReport(start_date,end_date,count,binding.txnId.text.toString(),selectedItemtype)
            toggleFilterLayout()
        }
        binding.spinnerServiceOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if(position==0){
                    selectedItemtype=""
                }else {
                    selectedItemtype = transactionTypes[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if necessary
            }
        }

        binding.rgCount.setOnCheckedChangeListener { group, checkedId ->
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
                    count=900
                }
            }
        }
    }

    private fun toggleFilterLayout() {
        if (binding.llFilter.visibility == View.VISIBLE) {
            binding.llFilter.visibility = View.GONE
        } else {
            binding.llFilter.visibility = View.VISIBLE

        }
    }


    override fun setData() {

    }
    fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

}