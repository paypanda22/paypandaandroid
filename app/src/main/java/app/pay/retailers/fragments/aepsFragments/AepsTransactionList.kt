package app.pay.retailers.fragments.aepsFragments

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
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.AepsTxnAdapter
import app.pay.retailers.databinding.DialogAepsInvoiceBinding
import app.pay.retailers.databinding.FragmentAepsTransactionListBinding
import app.pay.retailers.databinding.LytAepsFilterBinding
import app.pay.retailers.helperclasses.ActivityExtensions
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.AepsTxnClick
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.aepsTxnList.AepsTransactionsResponse
import app.pay.retailers.responsemodels.aepsTxnList.Data
import app.pay.retailers.responsemodels.aepsenquiry.AepsEnquiryResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class AepsTransactionList : BaseFragment<FragmentAepsTransactionListBinding>(FragmentAepsTransactionListBinding::inflate),AepsTxnClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var txnList:MutableList<Data>
private lateinit var  txnAdapter:AepsTxnAdapter
    private var txnCount=25
    private var  selectdStatus:String = ""
     var Types = listOf<String>()
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getAepsTransactions("","","","","",txnCount,selectdStatus)
    }

    private fun getAepsTransactions(from:String,to:String,mobile:String,aadhaarNo:String,txnId:String,count:Int,status:String) {
        binding.llMainContent.visibility= GONE
        binding.rlNoData.visibility= GONE
        binding.imageView.visibility= VISIBLE
        binding.swipeRefreshLayout.isRefreshing = true
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["start_date"]=from
        requestData["end_date"]=to
        requestData["page"]=0
        requestData["status"]=status
        requestData["count"]=count
        requestData["customer_mobile"]=mobile
        requestData["adhaar_no"]=aadhaarNo
        requestData["txn_id"]=txnId
        UtilMethods.aepsTransactionList(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: AepsTransactionsResponse =Gson().fromJson(message,AepsTransactionsResponse::class.java)
                if (!response.error){
                    txnList= mutableListOf()
                    txnList.addAll(response.data)

                     txnAdapter=AepsTxnAdapter(myActivity,txnList,this@AepsTransactionList)
                    binding.rvTxnList.adapter=txnAdapter
                    binding.rvTxnList.layoutManager=LinearLayoutManager(myActivity)
                    binding.swipeRefreshLayout.isRefreshing = false

                    binding.llMainContent.visibility= VISIBLE
                    binding.rlNoData.visibility= GONE
                    binding.imageView.visibility= GONE
                }else{
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.llMainContent.visibility=GONE
                    binding.rlNoData.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }

            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.llMainContent.visibility=GONE
                binding.rlNoData.visibility= VISIBLE
                binding.imageView.visibility= GONE
            }
        })
    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivFilter.setOnClickListener {
           openFilterDialog()
        }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.swipeRefreshLayout.setOnRefreshListener{
            getAepsTransactions("","","","","",txnCount,selectdStatus)
        }
    }

    private fun openFilterDialog() {
        val filterDialog: Dialog = Dialog(myActivity)
        val dBinding = LytAepsFilterBinding.inflate(myActivity.layoutInflater)
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
        dBinding.edtFromDate.setOnClickListener { CommonClass.showDatePickerDialog(myActivity,dBinding.edtFromDate) }
        dBinding.edtToDate.setOnClickListener {
            if (dBinding.edtFromDate.text.toString().isEmpty()){
                dBinding.edtFromDate.error="Select From Date"
            }else{
                CommonClass.showDatePickerDialog(myActivity,dBinding.edtToDate) }
            }


        dBinding.rgCount.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb25->{
                    txnCount=25
                }
                R.id.rb50->{
                    txnCount=50
                }
                R.id.rb100->{
                    txnCount=100
                }
                R.id.rb150->{
                    txnCount=150
                }
                R.id.rb200->{
                    txnCount=200
                }
            }
        }

        dBinding.btnSubmit.setOnClickListener {
            val from= dBinding.edtFromDate.text.toString().ifEmpty { "" }
            val to= dBinding.edtToDate.text.toString().ifEmpty { "" }
            val mobile= dBinding.edtMobile.text.toString().ifEmpty { "" }
            val aadhaar= dBinding.edtAadhaar.text.toString().ifEmpty { "" }
            if (mobile.isNotEmpty() && !ActivityExtensions.isValidMobile(mobile)){
                dBinding.edtMobile.error="Enter a Valid Mobile"
            }else if (aadhaar.isNotEmpty() && !ActivityExtensions.isValidAadhaar(aadhaar)){
                dBinding.edtAadhaar.error="Enter a Valid aadhaar"
            }else{
                getAepsTransactions(from,to,mobile,aadhaar,dBinding.edtTxnID.text.toString(),txnCount,selectdStatus)
                filterDialog.dismiss()
            }
        }


        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    override fun setData() {

    }

    override fun onItemClick(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        val receipt=SingleAepsTransaction()
        val bundle=Bundle()
        bundle.apply {
            putString("id",model[pos]._id.toString())


        }
        receipt.arguments=bundle
        receipt.show(parentFragmentManager, receipt.tag)
    }

    override fun onItemClickInvoice(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
       openViewDetailDialog(model[pos])

    }

    override fun onItemClickEnquery(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int,type:String) {
        when (type) {
            "CW" -> {
                cashwidrawl(model[pos]._id)
            }

            "MS" -> {

            }

            "BE" -> {

            }

            "AP" -> {
               // adhaarPayEnquiry(model[pos]._id)
            }

            "CD" -> {
                cashDipositEnquiry(model[pos]._id)
            }

            else -> {

            }


        }
    }
    private fun cashDipositEnquiry(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.cashDipositEnquiry(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: AepsEnquiryResponse = Gson().fromJson(message, AepsEnquiryResponse::class.java)
                if (!response.error) {
                    txnAdapter.notifyDataSetChanged()
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
    private fun cashwidrawl(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.cashwidrawl(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: AepsEnquiryResponse = Gson().fromJson(message, AepsEnquiryResponse::class.java)
                if (!response.error) {
                    txnAdapter.notifyDataSetChanged()
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




    private fun openViewDetailDialog(model: Data){
        val openDialog: Dialog=Dialog(myActivity)
        val binding= DialogAepsInvoiceBinding.inflate(myActivity.layoutInflater)
        binding.root.background= ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        openDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        openDialog.setContentView(binding.root)
        openDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        openDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        openDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        openDialog.window?.setGravity(Gravity.BOTTOM)
        binding.OpeningBalance.text=model.opening_balance.toString()
        binding.ClosingBalance.text=model.closing_balance.toString()
        binding.txnId.text=model.txn_id.toString()
        binding.BeneficiaryName.text=model.user_name.toString()
       // binding.AccountNo.text=model..toString()
        binding.TransactionDate.text=model.updatedAt.toString()
        binding.BankName.text=model.bank_name.toString()
        //binding.IFSCCode.text=model..toString()
        binding.RemitterMobileNo.text=model.customer_mobile.toString()
        binding.Amount.text=model.amount.toString()
       // binding.Charges.text=model.charge.toString()
        binding.Commissions.text=model.commission.toString()
        binding.TDS.text=model.user_tds.toString()


        binding.btnProceed.setOnClickListener{
            openDialog.dismiss()
        }
        openDialog.setCancelable(true)
        openDialog.show()

    }
}