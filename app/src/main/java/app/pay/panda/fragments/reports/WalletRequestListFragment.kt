package app.pay.panda.fragments.reports

import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.RequestListAdapter
import app.pay.panda.adapters.RequestListAdapterDist
import app.pay.panda.adapters.TransferToAdapter
import app.pay.panda.databinding.FragmentWalletRequestListBinding
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.bbpscatagory.CatagoryListResponse
import app.pay.panda.responsemodels.requestList.Request
import app.pay.panda.responsemodels.requestList.WalletRequestListResponse
import app.pay.panda.responsemodels.transferTo.Data
import app.pay.panda.responsemodels.transferTo.TransferToResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class WalletRequestListFragment : BaseFragment<FragmentWalletRequestListBinding>(FragmentWalletRequestListBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var txnList:MutableList<Request>
    private var transferTo = ""
    private lateinit var toList: MutableList<Data>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getWalletRequestList()
        //   getTransferToList()


    }
            /* private fun getTransferToList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getTransferTo(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: TransferToResponse = Gson().fromJson(message, TransferToResponse::class.java)
                if (!response.error) {
                    toList = mutableListOf()
                    toList.addAll(response.data)

                    val click = object : TransferToAdapter.TransferToClickListener {
                        override fun onItemClick(position: Int) {
                            transferTo = toList[position].value

                            if (toList[position].id.toInt() == 1) {
                                getWalletRequestList()
                            } else if(toList[position].id.toInt() == 2){
                                walletRequestListDist()
                            }else{
                                getWalletRequestList()
                            }

                        }
                    }

                    val transferToAdapter = TransferToAdapter(myActivity, toList, click)
                    binding.rvTransferTo.adapter = transferToAdapter
                    binding.rvTransferTo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

                    binding.imageView.visibility = GONE
                } else {

                    binding.imageView.visibility = GONE

                }

            }

            override fun fail(from: String) {

                binding.imageView.visibility = GONE
            }
        })
    }*/


    private fun getWalletRequestList() {
        val progressBar = CustomProgressBar()
        progressBar.showProgress(requireContext())
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["count"]=50
        requestData["page"]=0
        UtilMethods.walletRequestList(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                progressBar.hideProgress()
                val response: WalletRequestListResponse =
                    Gson().fromJson(message, WalletRequestListResponse::class.java)
                if (!response.error){
                    progressBar.hideProgress()
                    txnList= mutableListOf()
                    txnList.addAll(response.data.requestList)
                    val adapter=RequestListAdapter(myActivity,txnList)
                    binding.rvWalletRequestList.adapter=adapter
                    binding.rvWalletRequestList.layoutManager=LinearLayoutManager(myActivity)


                    binding.rvWalletRequestList.visibility= VISIBLE
                    binding.llNoData.visibility= GONE
                    binding.imageView.visibility= GONE
                }else{
                    progressBar.hideProgress()
                    binding.rvWalletRequestList.visibility=GONE
                    binding.llNoData.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }
            }

            override fun fail(from: String) {
                progressBar.hideProgress()
                binding.rvWalletRequestList.visibility=GONE
                binding.llNoData.visibility= VISIBLE
                binding.imageView.visibility= GONE
            }
        })
    }

    private fun walletRequestListDist() {
        val progressBar = CustomProgressBar()
        progressBar.showProgress(requireContext())
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
        requestData["count"]=50
        requestData["page"]=0
        UtilMethods.walletRequestListDist(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                progressBar.hideProgress()
                val response: WalletRequestListResponse =
                    Gson().fromJson(message, WalletRequestListResponse::class.java)
                if (!response.error){
                    progressBar.hideProgress()
                    txnList= mutableListOf()
                    txnList.addAll(response.data.requestList)
                    val adapter= RequestListAdapterDist(myActivity,txnList)
                    binding.rvWalletRequestList.adapter=adapter
                    binding.rvWalletRequestList.layoutManager=LinearLayoutManager(myActivity)


                    binding.rvWalletRequestList.visibility= VISIBLE
                    binding.llNoData.visibility= GONE
                    binding.imageView.visibility= GONE
                }else{
                    progressBar.hideProgress()
                    binding.rvWalletRequestList.visibility=GONE
                    binding.llNoData.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }
            }

            override fun fail(from: String) {
                progressBar.hideProgress()
                binding.rvWalletRequestList.visibility=GONE
                binding.llNoData.visibility= VISIBLE
                binding.imageView.visibility= GONE
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
        binding.rbLoginMethod.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.Company.id) {
                getWalletRequestList()
            } else {

                walletRequestListDist()
            }
        }
    }

    override fun setData() {

    }

}