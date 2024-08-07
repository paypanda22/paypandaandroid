package app.pay.panda.fragments.earningreport

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.DownStreamAdapter
import app.pay.panda.adapters.DownstreamRetailAdapter
import app.pay.panda.adapters.EarningReportAdapter

import app.pay.panda.adapters.RequestListAdapter
import app.pay.panda.databinding.FragmentEarnningReportBinding
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse

import app.pay.panda.responsemodels.earningreport.EarningReportResponse
import app.pay.panda.responsemodels.earningreport.Wallet

import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException


class FragmentEarnningReport : BaseFragment<FragmentEarnningReportBinding>(FragmentEarnningReportBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var earningreportlist: MutableList<Wallet>
    private lateinit var adapter: EarningReportAdapter
    private var page = "0"
    private var count = "50"

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        earningReport()
    }
    private fun earningReport() {
        val progressBar = CustomProgressBar()
        progressBar.showProgress(requireContext())
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["count"] = 10 // Example integer value
        requestData["end_date"] = ""
        requestData["max_amt"] = 0 // Example integer value
        requestData["min_amt"] = 0 // Example integer value
        requestData["order_id"] = ""
        requestData["page"] = 0 // Example integer value
        requestData["sortKey"] = ""
        requestData["sortType"] = ""
        requestData["start_date"] = ""
        requestData["trans_type"] = "dmt"
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
        binding.edtSearchearning.addTextChangedListener(object: TextWatcher {
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
        })
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