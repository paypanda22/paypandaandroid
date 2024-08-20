package app.pay.panda.fragments.reports

import android.content.Intent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.panda.BaseFragment
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.WalletTxnAdapter
import app.pay.panda.databinding.FragmentWalletTransactionReportBinding
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.walletTxn.Wallet
import app.pay.panda.responsemodels.walletTxn.WalletTxnReportResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class WalletTransactionReport : BaseFragment<FragmentWalletTransactionReportBinding>(FragmentWalletTransactionReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var list=ArrayList<Wallet>()
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        getTransactionList(start_date,end_date)
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

    private fun getTransactionList(startDate:String,endDate:String) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["count"]=900
        requestData["page"]=0
        requestData["start_date"]=startDate
        requestData["end_date"]=endDate
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
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener { toggleFilterLayout() }
binding.tvFilterTransactions.setOnClickListener{
    getTransactionList(binding.edtFromDate.text.toString(),binding.edtToDate.text.toString())
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
        val liveDate=CommonClass.getLiveTime("yyyy-MM-dd")
        binding.edtFromDate.setText(liveDate)
        binding.edtToDate.setText(liveDate)

    }


}