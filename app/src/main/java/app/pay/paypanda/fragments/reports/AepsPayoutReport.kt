package app.pay.paypanda.fragments.reports

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
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.adapters.AepsPayoutAdapter
import app.pay.paypanda.databinding.FragmentAepsPayoutReportBinding
import app.pay.paypanda.databinding.LytDmtFilterBinding
import app.pay.paypanda.helperclasses.ActivityExtensions
import app.pay.paypanda.helperclasses.CommonClass
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.AepsPayoutClickListener
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.responsemodels.aepsenquiry.AepsEnquiryResponse
import app.pay.paypanda.responsemodels.payoutresponse.AepsPayoutResponse
import app.pay.paypanda.responsemodels.payoutresponse.Data

import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson


class AepsPayoutReport : BaseFragment<FragmentAepsPayoutReportBinding>(FragmentAepsPayoutReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var count = 25
    private var customerMobile = ""
    private var accountNumber = ""
    private lateinit var aepsPayoutAdapter: AepsPayoutAdapter
    private var txnList = ArrayList<Data>()
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        payoutReport(start_date, end_date, count, "","")
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }
    }

    override fun setData() {

    }
    private fun payoutReport(startDate: String, endDate: String, count: Int,txnID:String,AadharNo:String) {

        val token = userSession.getData(Constant.USER_TOKEN).toString()


        UtilMethods.payoutReport(requireContext(), start_date,endDate,txnID,"0",count.toString(),token, object :
            MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AepsPayoutResponse = Gson().fromJson(message, AepsPayoutResponse::class.java)
                if (!response.error) {
                    if (txnList.isNotEmpty()) {
                        txnList.clear()
                    }
                    txnList.addAll(response.data)
                    val clickListner = object : AepsPayoutClickListener {
                        override fun onItemClick(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {

                                    openShareReceipt(model[pos]._id)

                                }

                        override fun onItemClickenquiry(
                            holder: RecyclerView.ViewHolder,
                            model: List<Data>,
                            pos: Int
                        ) {
                            aepsPayoutEnquiry(model[pos]._id)
                        }

                    }
                    aepsPayoutAdapter = AepsPayoutAdapter(myActivity, txnList,clickListner)
                    binding.rvDmtTransactions.adapter = aepsPayoutAdapter
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
    private fun aepsPayoutEnquiry(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.aepsPayoutEnquiry(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: AepsEnquiryResponse = Gson().fromJson(message, AepsEnquiryResponse::class.java)
                if (!response.error) {
                    aepsPayoutAdapter.notifyDataSetChanged()
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
                    .isNotEmpty() && !ActivityExtensions.isValidMobile(dBinding.edtCustomerNumber.text.toString())) {
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
                payoutReport(start_date, end_date, count,  dBinding.txnId.text.toString(),dBinding.edtCustomerNumber.text.toString());
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
    private fun openShareReceipt(id: String) {
        val bottomSheet = SingleAepsPayoutInvoice()
        val bundle = Bundle()
        bundle.putString("id", id)
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
}