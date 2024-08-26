package app.pay.panda.fragments.supportTicket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import app.pay.panda.adapters.PackaeServiceAdapter
import app.pay.panda.adapters.SupportTicketListAdapter
import app.pay.panda.databinding.FragmentAddSupportTicketBinding
import app.pay.panda.databinding.FragmentSupportTicketListingBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.SupportTicketClick
import app.pay.panda.responsemodels.PackageServices.PackageServicesResponse
import app.pay.panda.responsemodels.supportTickets.DataX
import app.pay.panda.responsemodels.supportTickets.SupportTicketListResponse
import app.pay.panda.responsemodels.ticketHistory.Data
import app.pay.panda.responsemodels.ticketHistory.TicktHistoryResponse
import app.pay.panda.responsemodels.ticketReply.TicketReplyResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson


class SupportTicketListing : BaseFragment<FragmentSupportTicketListingBinding>(FragmentSupportTicketListingBinding::inflate), SupportTicketClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var list: MutableList<DataX>
    private lateinit var attachments: MutableList<String>

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getSupportTickets()

    }

    private fun getSupportTickets() {
        binding.rvDisputeList.visibility = GONE
        binding.llNoData.visibility = GONE
        binding.imageView.visibility = VISIBLE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["count"] = 50
        requestData["page"] = 0
        UtilMethods.getSupportTicketList(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: SupportTicketListResponse = Gson().fromJson(message, SupportTicketListResponse::class.java)
                if (!response.error) {
                    if (response.data.data.isNotEmpty()) {
                        list = mutableListOf()
                        list.addAll(response.data.data)

                        val adapter = SupportTicketListAdapter(myActivity, list, this@SupportTicketListing)
                        binding.rvDisputeList.adapter = adapter
                        binding.rvDisputeList.layoutManager = LinearLayoutManager(myActivity)


                        binding.rvDisputeList.visibility = VISIBLE
                        binding.llNoData.visibility = GONE
                        binding.imageView.visibility = GONE
                    } else {
                        binding.rvDisputeList.visibility = GONE
                        binding.llNoData.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    }

                } else {
                    binding.rvDisputeList.visibility = GONE
                    binding.llNoData.visibility = VISIBLE
                    binding.imageView.visibility = GONE
                }

            }

            override fun fail(from: String) {
                binding.rvDisputeList.visibility = GONE
                binding.llNoData.visibility = VISIBLE
                binding.imageView.visibility = GONE
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

    }

    override fun setData() {

    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<DataX>, pos: Int, type: String, editText: TextInputEditText, edt: View) {
        when (type) {
            "reply" -> {
                val token = userSession.getData(Constant.USER_TOKEN).toString()
                attachments = mutableListOf()
                val requestData = hashMapOf<String, Any?>()
                requestData["user_id"] = token
                requestData["dispute_id"] = model[pos]._id
                requestData["chat"] = editText.text.toString()
                requestData["attachments"] = attachments
                sendReply(requestData, editText, edt)
            }

            "history" -> {
                val bundle = Bundle().apply {
                    putString("dispute_id", model[pos]._id)
                }
                findNavController().navigate(R.id.supportTicketHistory, bundle)
            }
        }
    }

    private fun sendReply(requestData: HashMap<String, Any?>, editText:TextInputEditText,edt: View) {
        UtilMethods.replyToTicket(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response:TicketReplyResponse=Gson().fromJson(message,TicketReplyResponse::class.java)
                if (!response.error){
                    editText.text?.clear()
                    showToast(requireContext(),"Reply Send Successfully")
                }else{
                    showToast(requireContext(),"Unable to Send Reply")
                }
                edt.visibility = GONE
            }

            override fun fail(from: String) {
                edt.visibility = GONE
                showToast(requireContext(),from)
            }
        })
    }



}