package app.pay.panda.fragments.network

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentFragemntNetworkBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.network.Data
import app.pay.panda.responsemodels.network.GetNetworkResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class FragemntNetwork : BaseFragment<FragmentFragemntNetworkBinding>(FragmentFragemntNetworkBinding::inflate),NetWorkListAdapter.NetworkItemClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var parentID=""
    private lateinit var networkList:MutableList<Data>
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        getNetWorkList(parentID)
    }

    private fun getNetWorkList(parentID: String) {
        val token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjoiNjY5MTBhMjFjMGMxY2IzNzI0ZjFmZDczIiwiaWF0IjoxNzIyMDY0NjAwfQ.I5EyDpquWwqumdG8IT7cfYYHb_eIhIR-DNrOFjrhnkU"
        binding.llNoData.visibility = View.GONE
        binding.imageView.visibility = View.VISIBLE
        binding.downstreamlist.visibility = View.GONE
        UtilMethods.getNetwork(requireContext(), token, "0", "100", parentID, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: GetNetworkResponse = Gson().fromJson(message, GetNetworkResponse::class.java)
                if (!response.error) {
                    if (response.data.isNotEmpty()) {
                        networkList = response.data.toMutableList()
                        val networkAdapter = NetWorkListAdapter(myActivity, networkList, this@FragemntNetwork)
                        binding.downstreamlist.apply {
                            adapter = networkAdapter
                            layoutManager = LinearLayoutManager(requireContext())
                            visibility = View.VISIBLE
                        }
                        binding.llNoData.visibility = View.GONE
                        binding.imageView.visibility = View.GONE
                    } else {
                        showNoData()
                    }
                } else {
                    showNoData()
                }
            }

            override fun fail(from: String) {
                showNoData()
            }


        })
    }
    private fun showNoData() {
        binding.llNoData.visibility = View.VISIBLE
        binding.imageView.visibility = View.GONE
        binding.downstreamlist.visibility = View.GONE
    }
    private fun nullActivityCheck() {
        if (activity!=null){
            myActivity=activity as FragmentActivity
        }else {
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {

    }

    override fun setData() {

    }

    override fun onNetworkItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        if (model[pos].sponsor==0){
            showToast(requireContext(),"No Sponsor Found")
        }else{
            getNetWorkList(model[pos]._id)
        }
    }


}