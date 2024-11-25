package app.pay.retailers.fragments.supportTicket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.retailers.BaseFragment
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.TicketHistoryAdapter
import app.pay.retailers.databinding.FragmentTicketHistoryBinding
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.ticketHistory.Data
import app.pay.retailers.responsemodels.ticketHistory.TicktHistoryResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class TicketHistoryFragment : BaseFragment<FragmentTicketHistoryBinding>(FragmentTicketHistoryBinding::inflate) {
    private lateinit var ticketHistoryList: MutableList<Data>
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var id: String? = ""
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        arguments?.let {
            id = it.getString("dispute_id").toString()

        }
        dmtdisputechat()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()

    }
    override fun addListeners() {
binding.ivBack.setOnClickListener{
    findNavController().popBackStack()
}
        binding.swipeRefreshLayout.setOnRefreshListener{
            dmtdisputechat()
        }
    }

    override fun setData() {

    }
    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }
    private fun dmtdisputechat() {
        binding.swipeRefreshLayout.isRefreshing = true
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.dmtdisputechat(requireContext(), token, id.toString(), "20", "0", object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: TicktHistoryResponse = Gson().fromJson(message, TicktHistoryResponse::class.java)
                if (!response.error) {
                    binding.imageView.visibility = VISIBLE
                    ticketHistoryList = mutableListOf()

                    // Clear previous data
                    ticketHistoryList.clear()

                    // Add new data
                    ticketHistoryList.addAll(response.data)

                    if (ticketHistoryList.isNotEmpty()) {
                        binding.swipeRefreshLayout.isRefreshing = false
                        // If data is available, show the RecyclerView and hide the "No Data" layout
                        val ticketHistoryAdapter = TicketHistoryAdapter(myActivity, ticketHistoryList)
                        binding.rvTicketHostoryList.adapter = ticketHistoryAdapter
                        binding.rvTicketHostoryList.layoutManager = LinearLayoutManager(myActivity)

                        binding.llNoData.visibility = GONE
                        binding.rvTicketHostoryList.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    } else {
                        binding.swipeRefreshLayout.isRefreshing = false
                        // If the list is empty, show the "No Data" layout
                        binding.llNoData.visibility = VISIBLE
                        binding.rvTicketHostoryList.visibility = GONE
                        binding.imageView.visibility = GONE
                    }
                } else {
                    binding.swipeRefreshLayout.isRefreshing = false
                    // Handle error case
                    binding.llNoData.visibility = VISIBLE
                    binding.imageView.visibility = VISIBLE
                    binding.rvTicketHostoryList.visibility = GONE
                }
            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false
                // Handle API failure case
                binding.imageView.visibility = GONE
                binding.llNoData.visibility = VISIBLE
                binding.rvTicketHostoryList.visibility = GONE
            }
        })
    }

}