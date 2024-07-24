package app.pay.panda.fragments.aepsFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.AepsTxnAdapter
import app.pay.panda.adapters.ScannerListAdapter
import app.pay.panda.databinding.DialogScannerDevicesBinding
import app.pay.panda.databinding.FragmentAepsTransactionListBinding
import app.pay.panda.databinding.LytAepsFilterBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.FingerPrintScanner
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.AepsTxnClick
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.ScannerListClick
import app.pay.panda.responsemodels.aepsTxnList.AepsTransactionsResponse
import app.pay.panda.responsemodels.aepsTxnList.Data
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class AepsTransactionList : BaseFragment<FragmentAepsTransactionListBinding>(FragmentAepsTransactionListBinding::inflate),AepsTxnClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var txnList:MutableList<Data>
    private var txnCount=25
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getAepsTransactions("","","","",txnCount)
    }

    private fun getAepsTransactions(from:String,to:String,mobile:String,aadhaarNo:String,count:Int) {
        binding.llMainContent.visibility= GONE
        binding.rlNoData.visibility= GONE
        binding.imageView.visibility= VISIBLE

        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["start_date"]=from
        requestData["end_date"]=to
        requestData["page"]=0
        requestData["count"]=count
        requestData["customer_mobile"]=mobile
        requestData["adhaar_no"]=aadhaarNo
        requestData["txn_id"]=""
        UtilMethods.aepsTransactionList(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:AepsTransactionsResponse=Gson().fromJson(message,AepsTransactionsResponse::class.java)
                if (!response.error){
                    txnList= mutableListOf()
                    txnList.addAll(response.data)

                    val txnAdapter=AepsTxnAdapter(myActivity,txnList,this@AepsTransactionList)
                    binding.rvTxnList.adapter=txnAdapter
                    binding.rvTxnList.layoutManager=LinearLayoutManager(myActivity)


                    binding.llMainContent.visibility= VISIBLE
                    binding.rlNoData.visibility= GONE
                    binding.imageView.visibility= GONE
                }else{
                    binding.llMainContent.visibility=GONE
                    binding.rlNoData.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }

            }

            override fun fail(from: String) {
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
                getAepsTransactions(from,to,mobile,aadhaar,txnCount)
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
            putString("date",model[pos].createdAt)
            putString("bankRrn",model[pos].bank_rrn.toString())
            putString("bankName",model[pos].bank_name.toString())
            putString("balAmount",model[pos].bal_amount.toString())
            putString("aadharNumber",model[pos].last_adhar.toString())
            putString("amount",model[pos].amount.toString())
            putString("txnId",model[pos].txn_id)
        }
        receipt.arguments=bundle
        receipt.show(parentFragmentManager, receipt.tag)
    }

}