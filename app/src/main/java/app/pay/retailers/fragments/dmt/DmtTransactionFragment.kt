package app.pay.retailers.fragments.dmt

import android.app.AlertDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.DmtTransactionListAdapter
import app.pay.retailers.databinding.DialogViewDmtBinding
import app.pay.retailers.databinding.FragmentDmtTransactionBinding
import app.pay.retailers.databinding.LytDmtFilterBinding
import app.pay.retailers.helperclasses.ActivityExtensions
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.DmtTxnClickListener
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.dmtTxnEnq.DmtTxnEnqResponse
import app.pay.retailers.responsemodels.dmttxnlist.DmtTransactionListResponse
import app.pay.retailers.responsemodels.dmttxnlist.Tran
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

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
    private var  selectdStatus:String = ""
    var Types = listOf<String>()
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())

        binding.rvDmtTransactions.visibility = GONE
        binding.llNoData.visibility = GONE
        binding.imageView.visibility = VISIBLE
        getTransactionList(start_date, end_date, count,search,selectdStatus);

    }

    private fun getTransactionList(startDate: String, endDate: String, count: Int,search:String,status:String) {

        val token = userSession.getData(Constant.USER_TOKEN).toString()
        binding.swipeRefreshLayout.isRefreshing = true
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["count"] = count
        requestData["page"] = 0
        requestData["min_amt"] = 0
        requestData["status"] = status
        requestData["max_amt"] = 0
        requestData["start_date"] = startDate
        requestData["search"] = search
        requestData["end_date"] = endDate

        UtilMethods.dmtTransactionList(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DmtTransactionListResponse =
                    Gson().fromJson(message, DmtTransactionListResponse::class.java)

                if (!response.error) {
                    // Check if trans data exists and is not empty
                    if (response.data.trans.isNullOrEmpty()) {
                        // No transactions found; show 'No Data' view
                        binding.rvDmtTransactions.visibility = GONE
                        binding.llNoData.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    } else {
                        // Transactions found; clear and add data to the list
                        if (txnList.isNotEmpty()) {
                            txnList.clear()
                        }
                        txnList.addAll(response.data.trans)

                        // Set up the adapter and show the RecyclerView
                        val clickListener = object : DmtTxnClickListener {
                            override fun onItemClicked(
                                holder: RecyclerView.ViewHolder,
                                model: List<Tran>,
                                pos: Int,
                                type: Int
                            ) {
                                when (type) {
                                    1 -> refreshTransactionStatus(model[pos])
                                    2 -> openOtpDialog(model[pos])
                                    4 -> openViewDetailDialog(model[pos])
                                    else -> openShareReceipt(model[pos].batchId.toString())
                                }
                            }
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                        dmtTxnAdapter =
                            DmtTransactionListAdapter(myActivity, txnList, clickListener)
                        binding.rvDmtTransactions.adapter = dmtTxnAdapter
                        binding.rvDmtTransactions.layoutManager = LinearLayoutManager(myActivity)

                        // Show the RecyclerView and hide 'No Data' layout
                        binding.rvDmtTransactions.visibility = VISIBLE
                        binding.llNoData.visibility = GONE
                        binding.imageView.visibility = GONE
                    }
                } else {
                    binding.swipeRefreshLayout.isRefreshing = false
                    // Error in response; show 'No Data' view
                    binding.rvDmtTransactions.visibility = GONE
                    binding.llNoData.visibility = VISIBLE
                    binding.imageView.visibility = GONE
                }
            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false
                // Failure in API call; show 'No Data' view
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
                getTransactionList(start_date, end_date, count,search,selectdStatus)
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
        binding.swipeRefreshLayout.setOnRefreshListener{
            getTransactionList(start_date, end_date, count,search,selectdStatus);
        }
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

    /*val zonedDateTime = ZonedDateTime.parse(model.createdAt)

    // Format it to a simpler form, like dd-MM-yyyy
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val formattedDate = zonedDateTime.format(formatter)*/

    binding.TransactionDate.text=model.createdAt.toString()
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
        binding.Status.text = "In PROCESS"
        binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
        binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_pending)
    } else if (model.response == 2.toString()) {
        binding.Status.text = "SUCCESS"
        binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
        binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_success)


    }else if(model.response == 3.toString()){
        binding.Status.text = "FAILED"
        binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
        binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_failed)
    }else if(model.response == 4.toString()){
        binding.Status.text = "Refunded"
        binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
        binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_grey)
    } else{
        binding.Status.text = "In PROCESS"
        binding.Status.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
        binding.Status.background=ContextCompat.getDrawable(myActivity, R.drawable.btn_pending)
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
                getTransactionList(start_date, end_date, count,dBinding.txnId.text.toString(),selectdStatus);
            }
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    override fun setData() {


    }


}