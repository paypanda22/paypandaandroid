package app.pay.retailers.fragments.reports

import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.retailers.BaseFragment
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.CompanyBanksAdapter
import app.pay.retailers.databinding.FragmentCompanyBankAccountListBinding
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.companyBanks.CompanyBanksResponse
import app.pay.retailers.responsemodels.companyBanks.Data
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class CompanyBankAccountList : BaseFragment<FragmentCompanyBankAccountListBinding>(FragmentCompanyBankAccountListBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var bankList:MutableList<Data>

    override fun init() {
        nullActivityCheck()
        userSession= UserSession(requireContext())
        getCompanyBankAccountList()

    }

    private fun getCompanyBankAccountList() {
        binding.swipeRefreshLayout.isRefreshing = true
        UtilMethods.getCompanyBankList(requireContext(),object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: CompanyBanksResponse =Gson().fromJson(message,CompanyBanksResponse::class.java)
                if (!response.error){
                    if (response.data.isEmpty()){
                        binding.llNoData.visibility=VISIBLE
                        binding.rlMainContent.visibility= GONE
                        binding.imageView.visibility= GONE
                    }else{
                        bankList= mutableListOf()
                        bankList.addAll(response.data)
                        binding.swipeRefreshLayout.isRefreshing = false
                        val adapter=CompanyBanksAdapter(myActivity,bankList)
                        binding.rvBankList.adapter=adapter
                        binding.rvBankList.layoutManager=LinearLayoutManager(myActivity)

                        binding.llNoData.visibility= GONE
                        binding.rlMainContent.visibility= VISIBLE
                        binding.imageView.visibility= GONE

                    }


                }else{
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.llNoData.visibility=VISIBLE
                    binding.rlMainContent.visibility= GONE
                    binding.imageView.visibility= GONE
                }
            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.llNoData.visibility=VISIBLE
                binding.rlMainContent.visibility= GONE
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
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.swipeRefreshLayout.setOnRefreshListener{
            getCompanyBankAccountList()
        }
    }

    override fun setData() {

    }

}