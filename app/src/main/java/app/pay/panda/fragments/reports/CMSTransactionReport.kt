package app.pay.panda.fragments.reports

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.CMSReportAdapter
import app.pay.panda.adapters.UtilityTransactionAdapter
import app.pay.panda.databinding.FragmentCMSTransactionReportBinding
import app.pay.panda.databinding.LytUtilityFilterBinding
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.UtilityTransactionClick
import app.pay.panda.responsemodels.cmsreportresponse.CMSReportResponse
import app.pay.panda.responsemodels.utilitytxn.Data
import app.pay.panda.responsemodels.utilitytxn.UtilityTxnResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class CMSTransactionReport : BaseFragment<FragmentCMSTransactionReportBinding>(FragmentCMSTransactionReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    private lateinit var dBinding: LytUtilityFilterBinding
    private lateinit var cmslist:MutableList<app.pay.panda.responsemodels.cmsreportresponse.Data>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getCmsPublicDataList("","","","0","50","")
    }

    override fun addListeners() {

            binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener {
            openFilterDialog()
        }

    }

    override fun setData() {

    }
    private fun getCmsPublicDataList(
        startDate: String,
        endDate: String,
        customerMobile: String,
        pageNo: String,
        txnCount: String,
        txnId: String
    ) {
        binding.rvcmsTransactions.visibility = GONE
        binding.imageView.visibility = VISIBLE
        binding.llNoData.visibility = GONE

        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.getCMsTransactionList(requireContext(), token, startDate, endDate, customerMobile, pageNo, txnCount, txnId, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CMSReportResponse = Gson().fromJson(message, CMSReportResponse::class.java)
                if (response.error == false) {
                    if (response.data.isNotEmpty()) {
                        cmslist = mutableListOf()
                        cmslist.addAll(response.data)

                        val txnAdapter = CMSReportAdapter(myActivity, cmslist)
                        binding.rvcmsTransactions.adapter = txnAdapter
                        binding.rvcmsTransactions.layoutManager = LinearLayoutManager(myActivity)

                        binding.rvcmsTransactions.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                        binding.llNoData.visibility = GONE
                    } else {
                        binding.rvcmsTransactions.visibility = GONE
                        binding.imageView.visibility = GONE
                        binding.llNoData.visibility = VISIBLE
                    }
                }
            }

            override fun fail(from: String) {
                binding.rvcmsTransactions.visibility = GONE
                binding.imageView.visibility = GONE
                binding.llNoData.visibility = VISIBLE
            }
        })
    }

    private fun openFilterDialog() {

        val filterDialog: Dialog = Dialog(myActivity)
        dBinding = LytUtilityFilterBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)
      dBinding.edtServiceType1.visibility= GONE
      dBinding.edtBillers1.visibility= GONE
        dBinding.number.visibility= VISIBLE
        dBinding.edtFromDate.setOnClickListener { CommonClass.showDatePickerDialog(myActivity,dBinding.edtFromDate) }
        dBinding.edtToDate.setOnClickListener {
            if (dBinding.edtFromDate.text.toString().isEmpty()){
                dBinding.edtFromDate.error="Select From Date"
                showToast(requireContext(),"Select From Date")
            }else{
                CommonClass.showDatePickerDialog(myActivity,dBinding.edtToDate) }
        }
        /*categoryList= mutableListOf()*/
        //  val catList=Category.getAllCategories()

        dBinding.rgCount.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb25->{
                    count=25
                }
                R.id.rb50->{
                    count=50
                }
                R.id.rb100->{
                    count=100
                }
                R.id.rb150->{
                    count=150
                }
                R.id.rb200->{
                    count=200
                }
            }
        }

        dBinding.btnSubmit.setOnClickListener {
            val from= dBinding.edtFromDate.text.toString().ifEmpty { "" }
            val to= dBinding.edtToDate.text.toString().ifEmpty { "" }
           // val caNumber=dBinding.edtCANumber.text.toString().ifEmpty { "" }
            getCmsPublicDataList(from,to,"","0",count.toString(),dBinding.txnID.text.toString())
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
}