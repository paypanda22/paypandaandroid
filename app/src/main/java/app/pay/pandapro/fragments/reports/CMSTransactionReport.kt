package app.pay.pandapro.fragments.reports

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.CMSReportAdapter
import app.pay.pandapro.databinding.FragmentCMSTransactionReportBinding
import app.pay.pandapro.databinding.LytUtilityFilterBinding
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.interfaces.CMSInvoicClick
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.aepsenquiry.AepsEnquiryResponse
import app.pay.pandapro.responsemodels.cmsreportresponse.CMSReportResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson


class CMSTransactionReport : BaseFragment<FragmentCMSTransactionReportBinding>(FragmentCMSTransactionReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    private lateinit var txnAdapter:CMSReportAdapter
    private lateinit var dBinding: LytUtilityFilterBinding
    private lateinit var cmslist:MutableList<app.pay.pandapro.responsemodels.cmsreportresponse.Data>
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
                        val clickListner = object : CMSInvoicClick {
                            override fun onItemClicked(
                                holder: RecyclerView.ViewHolder,
                                model: List<app.pay.pandapro.responsemodels.cmsreportresponse.Data>,
                                pos: Int,
                                stsus: String
                            ) {
                                when (stsus) {
                                    "1" -> {
                                        cmsEnquiry(model[pos]._id)
                                    }

                                    "2" -> {
                                        openShareReceipt(model[pos]._id.toString())
                                    }

                                    else -> {
                                        cmsEnquiry(model[pos]._id)
                                    }
                                }
                            }
                        }
                         txnAdapter = CMSReportAdapter(myActivity, cmslist,clickListner)
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
    private fun cmsEnquiry(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.cmsEnquiry(requireContext(), id, token, object : MCallBackResponse {
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
    private fun openShareReceipt(id: String) {
        val bottomSheet = SingleCMSTransaction()
        val bundle = Bundle()
        bundle.putString("id", id)
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
}