package app.pay.pandapro.fragments.dmt

import android.app.AlertDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.DmtTransactionListAdapter
import app.pay.pandapro.databinding.DialogViewDmtBinding
import app.pay.pandapro.databinding.FragmentDmtTransactionBinding
import app.pay.pandapro.databinding.LytDmtFilterBinding
import app.pay.pandapro.helperclasses.ActivityExtensions
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.DmtTxnClickListener
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.dmtTxnEnq.DmtTxnEnqResponse
import app.pay.pandapro.responsemodels.dmttxnlist.DmtTransactionListResponse
import app.pay.pandapro.responsemodels.dmttxnlist.Tran
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DmtTransactionFragment : BaseFragment<FragmentDmtTransactionBinding>(FragmentDmtTransactionBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var txnList = ArrayList<Tran>()
    private lateinit var dmtTxnAdapter: DmtTransactionListAdapter
    private var customerMobile = ""
    private var accountNumber = ""
    private var search = ""
    private var count = 25
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())

        binding.rvDmtTransactions.visibility = GONE
        binding.llNoData.visibility = GONE
        binding.imageView.visibility = VISIBLE
        getTransactionList(start_date, end_date, count,search);

    }

    private fun getTransactionList(startDate: String, endDate: String, count: Int,search:String) {

        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["count"] = count
        requestData["page"] = 0
        requestData["min_amt"] = 0
        requestData["max_amt"] = 0
        requestData["start_date"] = startDate
        requestData["search"] = search
        requestData["end_date"] = endDate

        UtilMethods.dmtTransactionList(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DmtTransactionListResponse = Gson().fromJson(message, DmtTransactionListResponse::class.java)
                if (!response.error) {
                    if (txnList.isNotEmpty()) {
                        txnList.clear()
                    }
                    txnList.addAll(response.data.trans)
                    val clickListner = object : DmtTxnClickListener {
                        override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Tran>, pos: Int, type: Int) {
                            when (type) {
                                1 -> {
                                    refreshTransactionStatus(model[pos])
                                }

                                2 -> {
                                    openOtpDialog(model[pos])
                                }
                                4->{

                                    openViewDetailDialog(model[pos])
                                }

                                else -> {
                                    openShareReceipt(model[pos].batchId.toString())
                                }
                            }
                        }
                    }
                    dmtTxnAdapter = DmtTransactionListAdapter(myActivity, txnList, clickListner)
                    binding.rvDmtTransactions.adapter = dmtTxnAdapter
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


    private fun openShareReceipt(batchId: String) {
        val bottomSheet = SingleDmtTransaction()
        val bundle = Bundle()
        bundle.putString("batchId", batchId)
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    private fun openOtpDialog(model: Tran) {
        val dialogView = myActivity.layoutInflater.inflate(R.layout.lyt_dialog_dmt_refund, null)
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dialogView)
        }
        val tvAmount: TextView = dialogView.findViewById(R.id.tvAmount)
        val tvCustomerMobile: TextView = dialogView.findViewById(R.id.tvCustomerMobile)
        val tvTid: TextView = dialogView.findViewById(R.id.tvTid)
        val edtOtp: TextInputEditText = dialogView.findViewById(R.id.edtOtp)
        val btnResendOtp: AppCompatButton = dialogView.findViewById(R.id.btnResendOtp)
        val btnProceed: AppCompatButton = dialogView.findViewById(R.id.btnProceed)

        tvAmount.text = model.amount.toString()
        tvCustomerMobile.text = model.customer_mobile.toString()
        tvTid.text = model.txn_id


        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        btnProceed.setOnClickListener {
            if (edtOtp.text.toString().isEmpty()) {
                edtOtp.error = "Enter a Valid OTP"
            } else {
                processRefund(edtOtp.text.toString(), model, alertDialog);
            }
        }

        btnResendOtp.setOnClickListener {
            resendRefundOtp(model)
        }


    }

    private fun processRefund(otp: String, model: Tran, alertDialog: AlertDialog?) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["id"] = model._id
        requestData["otp"] = otp
        UtilMethods.processDmtRefundWithOtp(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {

            }

            override fun fail(from: String) {

            }
        })
    }

    private fun resendRefundOtp(model: Tran) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["id"] = model._id
        UtilMethods.dmtInitiateRefund(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {

            }

            override fun fail(from: String) {

            }
        })

    }


    private fun refreshTransactionStatus(model: Tran) {
        binding.rvDmtTransactions.visibility = VISIBLE
        binding.llNoData.visibility = GONE
        binding.imageView.visibility = GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.dmtTxnEnquiry(requireContext(), model._id.toString(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DmtTxnEnqResponse = Gson().fromJson(message, DmtTxnEnqResponse::class.java)
                getTransactionList(start_date, end_date, count,"")
               // getTransactionList(start_date, end_date, count);
                if (!response.error) {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    dmtTxnAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
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
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }
    }
private fun openViewDetailDialog(model: Tran){
    val openDialog: Dialog=Dialog(myActivity)
    val binding= DialogViewDmtBinding.inflate(myActivity.layoutInflater)
    binding.root.background= ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
    openDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    openDialog.setContentView(binding.root)
    openDialog.window
        ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    openDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    openDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
    openDialog.window?.setGravity(Gravity.BOTTOM)
    binding.OpeningBalance.text=model.o_bal.toString()
    binding.ClosingBalance.text=model.c_bal.toString()
    binding.txnId.text=model.txn_id.toString()
    binding.BeneficiaryName.text=model.beneficiary_name.toString()
    binding.AccountNo.text=model.account_number.toString()

    val zonedDateTime = ZonedDateTime.parse(model.updatedAt)

    // Format it to a simpler form, like dd-MM-yyyy
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val formattedDate = zonedDateTime.format(formatter)

    binding.TransactionDate.text=formattedDate.toString()
    binding.BankName.text=model.bank_name.toString()
    binding.IFSCCode.text=model.ifsc_code.toString()
    binding.RemitterMobileNo.text=model.customer_mobile.toString()
    binding.Amount.text=model.amount.toString()
    binding.Charges.text=model.charge.toString()
    binding.Commissions.text=model.commission.toString()
    binding.TDS.text=model.tds.toString()
  /*  if(binding.Commissions.text.equals("")){
        binding.Commissions.text="N/A"
    }else{
        binding.Commissions.text=model.commission.toString()
    }
   if(binding.TDS.text.equals("")){
       binding.TDS.text="N/A"
   }else{
       binding.TDS.text=model.TDS.toString()
   }*/
    if (model.response == 1.toString()) {
        when (model.tx_status.toString()) {
            "0" -> {
                binding.Status.text = "SUCCESS"
                binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_success)
            }
            "1" -> {
                binding.Status.text = "FAILED"
                binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_failed)
            }
            "2" -> {
                binding.Status.text = "In PROCESS"
                binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_pending)
            }
            "3" -> {
                binding.Status.text = "Initiate Refund"
                binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_grey)
            }
            "4" -> {
                binding.Status.text = "Refunded"
                binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_grey)
            }
            "5" -> {
                binding.Status.text = "In PROCESS"
                binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_pending)
            }
        }
    } else if (model.response == 2.toString()) {
        binding.Status.text = "SUCCESS"



    }else if(model.response == 3.toString()){
        binding.Status.text = "FAILED"
    }else{
        binding.Status.text = "Pending"
    }
    binding.UTRNoRRNNo.text=model.utr.toString()

    binding.btnProceed.setOnClickListener{
        openDialog.dismiss()
    }
    openDialog.setCancelable(true)
    openDialog.show()

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
        dBinding.edtCustomerNumber.hint="Filter By Mobile No"
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)
           dBinding.aadharTv.visibility= GONE
           dBinding.edtAccountNumber1.visibility= GONE

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
                getTransactionList(start_date, end_date, count,dBinding.txnId.text.toString());
            }
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    override fun setData() {


    }


}