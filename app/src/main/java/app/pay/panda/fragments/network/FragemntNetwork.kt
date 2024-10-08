package app.pay.panda.fragments.network


import android.view.View.GONE
import android.view.View.VISIBLE
import TransferMoneyDialogFragment
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.DownStreamAdapter
import app.pay.panda.adapters.DownStreamDistributerAdapter
import app.pay.panda.adapters.DownstreamRetailAdapter
import app.pay.panda.adapters.UtilityTransactionAdapter
import app.pay.panda.databinding.FragmentFragemntNetworkBinding
import app.pay.panda.databinding.LytDmtFilterBinding
import app.pay.panda.databinding.LytNetworkFilterBinding
import app.pay.panda.fragments.ReverseMoneyDialogFragment
import app.pay.panda.fragments.ViewReportDialogFragment
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass

import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.DownStreamClick
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.UtilityTransactionClick
import app.pay.panda.responsemodels.downstramdistributer.DonwStreamDistributerResponse
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
    private lateinit var downstramlistDistributer:MutableList<app.pay.panda.responsemodels.downstramdistributer.Data>
    private var page = "0"
    private var count = 50
    private var start_date = ""
    private var end_date = ""
    private var name = ""
    private var mobile = ""
    private var refer_id = ""
    private var customerMobile = ""
    private var accountNumber = ""
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())

        val userType = userSession.getData(Constant.USER_TYPE_NAME)
        if (userType.equals("Super Distributor") ) {
            getNetwork(page,count,"",name,mobile,refer_id)
        }else if(userType.equals("Distributor")){
            val id = userSession.getData(Constant.ID)
            getNetworkRetailerDist(page,count, id.toString(),name,mobile,refer_id)
        }
    }
    private fun getNetwork(pageNo: String, txnCount: Int,id:String,name:String,mobile:String,refer_id:String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetwork(myActivity, token, pageNo, txnCount,id,name,mobile, refer_id, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    val response: DownstreamResponse = Gson().fromJson(message, DownstreamResponse::class.java)
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            downstramlist = response.data.toMutableList()
                            val clickListner = object : DownStreamClick {
                                override fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.panda.responsemodels.downstreamRetailerResponse.Data>) -> Unit) {
                                    // Fetch the retailer data and then call the callback

                                    getNetworkRetailer(page,count,downstramlist[position]._id,name,mobile,refer_id,holder)

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

        private fun getNetworkRetailer(pageNo: String, txnCount: Int, id:String,name:String,mobile:String,refer_id:String, holder: RecyclerView.ViewHolder) {
            binding.llNoData.visibility = View.GONE
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.getNetworkRetailer(requireContext(), token, pageNo, txnCount, id,name,mobile,refer_id, object : MCallBackResponse {
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


    private fun getNetworkRetailerDist(pageNo: String, txnCount: Int,id:String,name:String,mobile:String,refer_id:String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetworkRetailer(requireContext(), token, pageNo, txnCount, id,name,mobile,refer_id, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    // Use the custom Gson instance with the deserializer
                    val response: DonwStreamDistributerResponse = Gson().fromJson(message, DonwStreamDistributerResponse::class.java)

                    // Proceed with your existing logic
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            if (response.data.isNotEmpty()) {


                                downstramlistDistributer = response.data.toMutableList()
                                downstramlistDistributer.clear() // Clear existing data
                                downstramlistDistributer.addAll(response.data) // Add new data
                                // Set up the nested RecyclerView
                                val adapter = DownStreamDistributerAdapter(myActivity, downstramlistDistributer)
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
        binding.ivMenu.setOnClickListener { openTransactionFilterDialog() }
    }
    private fun openTransactionFilterDialog() {
        val filterDialog = Dialog(myActivity)
        val dBinding = LytNetworkFilterBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)
         dBinding.date.visibility=GONE
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)
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

                val userType = userSession.getData(Constant.USER_TYPE_NAME)
                if (userType.equals("Super Distributor") ) {
                    getNetwork(page,count,"",dBinding.editname.text.toString(),dBinding.edtMob.text.toString(),dBinding.edtrefer.text.toString())
                }else if(userType.equals("Distributor")){
                    val id = userSession.getData(Constant.ID)
                    getNetworkRetailerDist(page,count, id.toString(),dBinding.editname.text.toString(),dBinding.edtMob.text.toString(),dBinding.edtrefer.text.toString())
                }
               // getTransactionList(start_date, end_date, count);

            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    override fun setData() {

    }


}