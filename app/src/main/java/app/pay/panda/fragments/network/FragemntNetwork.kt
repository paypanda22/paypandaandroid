package app.pay.panda.fragments.network

import TransferMoneyDialogFragment
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.DownStreamAdapter
import app.pay.panda.adapters.DownstreamRetailAdapter
import app.pay.panda.adapters.UtilityTransactionAdapter
import app.pay.panda.databinding.FragmentFragemntNetworkBinding
import app.pay.panda.fragments.ReverseMoneyDialogFragment
import app.pay.panda.fragments.ViewReportDialogFragment

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
import org.koin.android.scope.newScope


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

        val userType = userSession.getData(Constant.USER_TYPE_NAME)
        if (userType.equals("Super Distributor") ) {
            getNetwork(page,count)
        }else if(userType.equals("Distributor")){
            val id = userSession.getData(Constant.ID)
            getNetworkRetailerDist(page,count, id.toString())
        }
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
                                override fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.panda.responsemodels.downstreamRetailerResponse.Data>) -> Unit) {
                                    // Fetch the retailer data and then call the callback

                                    getNetworkRetailer(page,count,downstramlist[position]._id,holder)

                                }
                                override fun onTransferMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int) {
                                    val value = downstramlist[position]._id
                                    val refer_id = downstramlist[position].refer_id
                                    val balance = downstramlist[position].main_wallet
                                    val transferMoneyDialogFragment = TransferMoneyDialogFragment.newInstance(value,refer_id,balance)
                                    transferMoneyDialogFragment.show(childFragmentManager, "TransferMoneyDialogFragment")
                                }

                                override fun onReverseMoneyClicked(
                                    holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int
                                ) {
                                    val value = downstramlist[position]._id
                                    val refer_id = downstramlist[position].refer_id
                                    val balance = downstramlist[position].main_wallet
                                    val reverseMoneyDialogFragment = ReverseMoneyDialogFragment.newInstance(value,refer_id,balance)
                                    reverseMoneyDialogFragment.show(childFragmentManager, "ReverseMoneyDialogFragment")

                                }

                                override fun onViewReportClicked(
                                    holder: DownStreamAdapter.ViewHolder,
                                    downstramlist: MutableList<Data>,
                                    position: Int
                                ) {
                                    val value = downstramlist[position]._id
                                    val viewReportDialogFragment = ViewReportDialogFragment.newInstance(value)
                                    viewReportDialogFragment.show(childFragmentManager, "ViewReportDialogFragment")
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

        private fun getNetworkRetailer(pageNo: String, txnCount: String, id: String, holder: RecyclerView.ViewHolder) {
            binding.llNoData.visibility = View.GONE
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.getNetworkRetailer(requireContext(), token, pageNo, txnCount, id, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    try {
                        // Use the custom Gson instance with the deserializer
                        val response: DownstreamRetailarResp = Gson().fromJson(message, DownstreamRetailarResp::class.java)

                        // Proceed with your existing logic
                        if (!response.error) {
                            if (response.statusCode == 200) {
                                if (response.data.isNotEmpty()) {


                                    downstramlistRetailer = response.data.toMutableList()
                                    downstramlistRetailer.clear() // Clear existing data
                                    downstramlistRetailer.addAll(response.data) // Add new data
                                    // Set up the nested RecyclerView
                                    val adapter = DownstreamRetailAdapter(myActivity, downstramlistRetailer)
                                    (holder as DownStreamAdapter.ViewHolder).recyclerRetailer.adapter = adapter
                                    holder.recyclerRetailer.layoutManager = LinearLayoutManager(myActivity)
                                    holder.recyclerRetailer.visibility = View.VISIBLE
                                    binding.imageView.visibility = View.GONE
                                    binding.llNoData.visibility = View.GONE
                                    adapter.notifyDataSetChanged()

                                } else {
                                    // Handle empty data case
                                    handleEmptyRetailerData(holder)
                                    binding.imageView.visibility = View.GONE
                                    binding.llNoData.visibility = View.VISIBLE
                                }
                            } else {
                                // Handle non-200 status code
                                handleEmptyRetailerData(holder)
                                binding.imageView.visibility = View.GONE
                                binding.llNoData.visibility = View.VISIBLE
                            }
                        } else {
                            // Handle error case
                            handleEmptyRetailerData(holder)
                            binding.imageView.visibility = View.GONE
                            binding.llNoData.visibility = View.VISIBLE
                        }
                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                        handleEmptyRetailerData(holder)
                        binding.imageView.visibility = View.GONE
                        binding.llNoData.visibility = View.VISIBLE
                    }
                }

                override fun fail(from: String) {
                    handleEmptyRetailerData(holder)
                    binding.imageView.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                }
            })
        }


    private fun getNetworkRetailerDist(pageNo: String, txnCount: String, id: String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetworkRetailer(requireContext(), token, pageNo, txnCount, id, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    // Use the custom Gson instance with the deserializer
                    val response: DownstreamRetailarResp = Gson().fromJson(message, DownstreamRetailarResp::class.java)

                    // Proceed with your existing logic
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            if (response.data.isNotEmpty()) {


                                downstramlistRetailer = response.data.toMutableList()
                                downstramlistRetailer.clear() // Clear existing data
                                downstramlistRetailer.addAll(response.data) // Add new data
                                // Set up the nested RecyclerView
                                val adapter = DownstreamRetailAdapter(myActivity, downstramlistRetailer)
                               binding.recydlerlist.adapter = adapter
                                binding.recydlerlist.layoutManager = LinearLayoutManager(myActivity)
                                binding.recydlerlist.visibility = View.VISIBLE
                                binding.imageView.visibility = View.GONE
                                binding.llNoData.visibility = View.GONE
                                adapter.notifyDataSetChanged()

                            } else {
                                // Handle empty data case
                                binding.imageView.visibility = View.GONE
                                binding.llNoData.visibility = View.VISIBLE
                            }
                        } else {
                            // Handle non-200 status code

                            binding.imageView.visibility = View.GONE
                            binding.llNoData.visibility = View.VISIBLE
                        }
                    } else {
                        // Handle error case

                        binding.imageView.visibility = View.GONE
                        binding.llNoData.visibility = View.VISIBLE
                    }
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()

                    binding.imageView.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                }
            }

            override fun fail(from: String) {

                binding.imageView.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
            }
        })
    }


        private fun handleEmptyRetailerData(holder: RecyclerView.ViewHolder) {
            (holder as DownStreamAdapter.ViewHolder).recyclerRetailer.visibility = View.GONE
        }


    fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }
    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setData() {

    }


}