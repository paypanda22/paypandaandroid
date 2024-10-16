package app.pay.paypanda.fragments.network


import android.view.View.GONE
import TransferMoneyDialogFragment
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.adapters.DownStreamAdapter
import app.pay.paypanda.adapters.DownStreamDistributerAdapter
import app.pay.paypanda.adapters.DownstreamRetailAdapter
import app.pay.paypanda.databinding.FragmentFragemntNetworkBinding
import app.pay.paypanda.databinding.LytNetworkFilterBinding
import app.pay.paypanda.fragments.ReverseMoneyDialogFragment
import app.pay.paypanda.fragments.ViewReportDialogFragment
import app.pay.paypanda.helperclasses.CommonClass

import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.DownStreamClick
import app.pay.paypanda.interfaces.DownStreamRetailerClick
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.responsemodels.downstramdistributer.DonwStreamDistributerResponse
import app.pay.paypanda.responsemodels.downstreamRetailerResponse.DownstreamRetailarResp
import app.pay.paypanda.responsemodels.downstreamresponse.Data
import app.pay.paypanda.responsemodels.downstreamresponse.DownstreamResponse
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException


class FragemntNetwork : BaseFragment<FragmentFragemntNetworkBinding>(FragmentFragemntNetworkBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var downstramlist:MutableList<Data>
    private lateinit var downstramlistRetailer:MutableList<app.pay.paypanda.responsemodels.downstreamRetailerResponse.Data>
    private lateinit var downstramlistDistributer:MutableList<app.pay.paypanda.responsemodels.downstramdistributer.Data>
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
                                override fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.paypanda.responsemodels.downstreamRetailerResponse.Data>) -> Unit) {
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
                                val clickListner = object : DownStreamRetailerClick {
                                    override fun onTransferMoneyClicked(
                                        holder: DownStreamDistributerAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.paypanda.responsemodels.downstramdistributer.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistDistributer[position]._id
                                        val refer_id = downstramlistDistributer[position].refer_id
                                        val balance = downstramlistDistributer[position].main_wallet
                                        val transferMoneyDialogFragment = TransferMoneyDialogFragment.newInstance(value,refer_id,balance)
                                        transferMoneyDialogFragment.show(childFragmentManager, "TransferMoneyDialogFragment")
                                    }

                                    override fun onReverseMoneyClicked(
                                        holder: DownStreamDistributerAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.paypanda.responsemodels.downstramdistributer.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistDistributer[position]._id
                                        val refer_id = downstramlistDistributer[position].refer_id
                                        val balance = downstramlistDistributer[position].main_wallet
                                        val reverseMoneyDialogFragment = ReverseMoneyDialogFragment.newInstance(value,refer_id,balance)
                                        reverseMoneyDialogFragment.show(childFragmentManager, "ReverseMoneyDialogFragment")
                                    }


                                }
                                val adapter = DownStreamDistributerAdapter(myActivity, downstramlistDistributer,clickListner)
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