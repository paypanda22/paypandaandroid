package app.pay.panda.fragments.network

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.DownStreamAdapter
import app.pay.panda.adapters.DownstreamRetailAdapter
import app.pay.panda.adapters.UtilityTransactionAdapter
import app.pay.panda.databinding.FragmentFragemntNetworkBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.DownStreamClick
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.UtilityTransactionClick
import app.pay.panda.responsemodels.downstreamRetailerResponse.DownstreamRetailarResp
import app.pay.panda.responsemodels.downstreamresponse.Data
import app.pay.panda.responsemodels.downstreamresponse.DownstreamResponse
import app.pay.panda.responsemodels.utilitytxn.UtilityTxnResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException


class FragemntNetwork : BaseFragment<FragmentFragemntNetworkBinding>(FragmentFragemntNetworkBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var downstramlist:MutableList<Data>
    private lateinit var downstramlistRetailer:MutableList<app.pay.panda.responsemodels.downstreamRetailerResponse.Data>
    private var page = "0"
    private var count = "50"

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getNetwork(page,count)
    }
    private fun getNetwork(pageNo: String, txnCount: String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetwork(requireContext(), token, pageNo, txnCount, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    val response: DownstreamResponse = Gson().fromJson(message, DownstreamResponse::class.java)
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            downstramlist = response.data.toMutableList()
                            val clickListner = object : DownStreamClick {
                                override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
                                    Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
                                }
                            }
                            val txnAdapter = DownStreamAdapter(myActivity, downstramlist,clickListner)
                            binding.recydlerlist.adapter = txnAdapter
                            binding.recydlerlist.layoutManager = LinearLayoutManager(myActivity)
                            binding.recydlerlist.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.llNoData.visibility = View.GONE
                        } else {
                            binding.recydlerlist.visibility = View.GONE
                            binding.imageView.visibility = View.GONE
                            binding.llNoData.visibility = View.VISIBLE
                        }
                    } else {
                        binding.recydlerlist.visibility = View.GONE
                        binding.imageView.visibility = View.GONE
                        binding.llNoData.visibility = View.VISIBLE
                    }
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    binding.recydlerlist.visibility = View.GONE
                    binding.imageView.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                }
            }

            override fun fail(from: String) {
                binding.recydlerlist.visibility = View.GONE
                binding.imageView.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
            }
        })
    }

    private fun getNetworkRetailer(id: String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetworkRetailer(requireContext(), token, "id", object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    val response: DownstreamRetailarResp = Gson().fromJson(message, DownstreamRetailarResp::class.java)
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            downstramlistRetailer = response.data.toMutableList()


                            val txnAdapter = DownstreamRetailAdapter(myActivity, downstramlistRetailer)
                            binding.recydlerlist.adapter = txnAdapter
                            binding.recydlerlist.layoutManager = LinearLayoutManager(myActivity)
                            binding.recydlerlist.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.llNoData.visibility = View.GONE

                        } else {
                            binding.recydlerlist.visibility = View.GONE
                            binding.imageView.visibility = View.GONE
                            binding.llNoData.visibility = View.VISIBLE
                        }
                    } else {
                        binding.recydlerlist.visibility = View.GONE
                        binding.imageView.visibility = View.GONE
                        binding.llNoData.visibility = View.VISIBLE
                    }
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    binding.recydlerlist.visibility = View.GONE
                    binding.imageView.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                }
            }

            override fun fail(from: String) {
                binding.recydlerlist.visibility = View.GONE
                binding.imageView.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
            }
        })
    }


    fun nullActivityCheck() {
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


}