package app.pay.retailers.fragments.network

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity

import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.AsmAdapter
import app.pay.retailers.adapters.AsmDownStreamAdapter

import app.pay.retailers.adapters.ZsmDownStreamAdapter
import app.pay.retailers.databinding.FragmentZSMNetworkBinding
import app.pay.retailers.databinding.LytNetworkFilterBinding
import app.pay.retailers.fragments.ReverseMoneyDialogFragment
import app.pay.retailers.fragments.ViewReportDialogFragment
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.AsmClick
import app.pay.retailers.interfaces.DownStreamAsmClick
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.ZSMDownStreamClick
import app.pay.retailers.responsemodels.asmresponse.AsmResponse
import app.pay.retailers.responsemodels.zsmresponse.ZSMResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException


class ZSMNetwork : BaseFragment<FragmentZSMNetworkBinding>(FragmentZSMNetworkBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var downstramlist:MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>
    private lateinit var downstramlistRetailer:MutableList<app.pay.retailers.responsemodels.asmresponse.Data>
    private lateinit var downstramlistDistributer:MutableList<app.pay.retailers.responsemodels.asmresponse.Data>
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
        if (userType.equals("zsm") ) {
            getNetwork(page,count,"",name,mobile,refer_id)
        }else if(userType.equals("asm")){
            val id = userSession.getData(Constant.ID)
           getNetworkRetailerDist(page,count, id.toString(),name,mobile,refer_id)
        }

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
    private fun getNetwork(pageNo: String, txnCount: Int,id:String,name:String,mobile:String,refer_id:String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetwork(myActivity, token, pageNo, txnCount,id,name,mobile, refer_id, object :
            MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    val response: ZSMResponse = Gson().fromJson(message, ZSMResponse::class.java)
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            downstramlist = response.data.toMutableList()
                            val clickListner = object : ZSMDownStreamClick {
                                override fun onItemClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int, callback: (List<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>) -> Unit) {
                                    // Fetch the retailer data and then call the callback

                                    getNetworkRetailer(page,count,downstramlist[position]._id,name,mobile,refer_id,holder)

                                }
                                override fun onTransferMoneyClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int) {
                                    val value = downstramlist[position]._id
                                    val refer_id = downstramlist[position].refer_id
                                    val balance = downstramlist[position].main_wallet
                                    val mobile = downstramlist[position].mobile
                                    val name = downstramlist[position].name
                                    val transferMoneyDialogFragment = TransferMoneyDialogFragment.newInstance(value,refer_id,balance,mobile,name)
                                    transferMoneyDialogFragment.show(childFragmentManager, "TransferMoneyDialogFragment")
                                }

                                override fun onReverseMoneyClicked(
                                    holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int
                                ) {
                                    val value = downstramlist[position]._id
                                    val refer_id = downstramlist[position].refer_id
                                    val balance = downstramlist[position].main_wallet
                                    val mobile = downstramlist[position].mobile
                                    val name = downstramlist[position].name
                                    val reverseMoneyDialogFragment = ReverseMoneyDialogFragment.newInstance(value,refer_id,balance,mobile,name)
                                    reverseMoneyDialogFragment.show(childFragmentManager, "ReverseMoneyDialogFragment")

                                }

                                override fun onViewReportClicked(
                                    holder: ZsmDownStreamAdapter.ViewHolder,
                                    downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>,
                                    position: Int
                                ) {
                                    val value = downstramlist[position]._id
                                    val viewReportDialogFragment = ViewReportDialogFragment.newInstance(value)
                                    viewReportDialogFragment.show(childFragmentManager, "ViewReportDialogFragment")
                                }

                                override fun onViewWalletReportClicked(
                                    holder: ZsmDownStreamAdapter.ViewHolder,
                                    downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>,
                                    position: Int
                                ) {
                                    val bundle = Bundle()
                                    bundle.putString("value",downstramlist[position]._id)
                                    findNavController().navigate(R.id.WalletReportdownStarem,bundle)
                                }

                            }

                            val txnAdapter = ZsmDownStreamAdapter(myActivity, downstramlist,clickListner)
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
                    val response: AsmResponse = Gson().fromJson(message, AsmResponse::class.java)

                    // Proceed with your existing logic
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            if (response.data.isNotEmpty()) {


                                downstramlistRetailer = response.data.toMutableList()
                                downstramlistRetailer.clear() // Clear existing data
                                downstramlistRetailer.addAll(response.data) // Add new data
                                // Set up the nested RecyclerView
                                val clickListner = object : DownStreamAsmClick {

                                    override fun onItemClicked(
                                        holder: AsmDownStreamAdapter.ViewHolder,
                                        downstramlist: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val bundle = Bundle()
                                        bundle.putString("id",downstramlist[position]._id)
                                       findNavController().navigate(R.id.FragmentNetwork,bundle)
                                    }

                                    override fun onTransferMoneyClicked(
                                        holder: AsmDownStreamAdapter.ViewHolder,
                                        downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistRetailer[position]._id
                                        val refer_id = downstramlistRetailer[position].refer_id
                                        val balance = downstramlistRetailer[position].main_wallet
                                        val mobile = downstramlistRetailer[position].mobile
                                        val name = downstramlistRetailer[position].name
                                        val transferMoneyDialogFragment = TransferMoneyDialogFragment.newInstance(value,refer_id,balance,mobile,name)
                                        transferMoneyDialogFragment.show(childFragmentManager, "TransferMoneyDialogFragment")
                                    }

                                    override fun onReverseMoneyClicked(
                                        holder: AsmDownStreamAdapter.ViewHolder,
                                        downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistRetailer[position]._id
                                        val refer_id = downstramlistRetailer[position].refer_id
                                        val balance = downstramlistRetailer[position].main_wallet
                                        val mobile = downstramlistRetailer[position].mobile
                                        val name = downstramlistRetailer[position].name
                                        val reverseMoneyDialogFragment = ReverseMoneyDialogFragment.newInstance(value,refer_id,balance,mobile,name)
                                        reverseMoneyDialogFragment.show(childFragmentManager, "ReverseMoneyDialogFragment")
                                    }

                                    override fun onViewReportClicked(
                                        holder: AsmDownStreamAdapter.ViewHolder,
                                        downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlist[position]._id
                                        val viewReportDialogFragment = ViewReportDialogFragment.newInstance(value)
                                        viewReportDialogFragment.show(childFragmentManager, "ViewReportDialogFragment")
                                    }

                                    override fun onViewWalletReportClicked(
                                        holder: AsmDownStreamAdapter.ViewHolder,
                                        downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val bundle = Bundle()
                                        bundle.putString("value",downstramlist[position]._id)
                                        findNavController().navigate(R.id.WalletReportdownStarem,bundle)
                                    }

                                }
                                val adapter = AsmDownStreamAdapter(myActivity, downstramlistRetailer,clickListner)
                                (holder as ZsmDownStreamAdapter.ViewHolder).recyclerRetailer.adapter = adapter
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
    private fun handleEmptyRetailerData(holder: RecyclerView.ViewHolder) {
        (holder as ZsmDownStreamAdapter.ViewHolder).recyclerRetailer.visibility = View.GONE
    }
    private fun getNetworkRetailerDist(pageNo: String, txnCount: Int,id:String,name:String,mobile:String,refer_id:String) {
        binding.llNoData.visibility = View.GONE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNetworkRetailer(requireContext(), token, pageNo, txnCount, id,name,mobile,refer_id, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    // Use the custom Gson instance with the deserializer
                    val response: AsmResponse = Gson().fromJson(message, AsmResponse::class.java)

                    // Proceed with your existing logic
                    if (!response.error) {
                        if (response.statusCode == 200) {
                            if (response.data.isNotEmpty()) {


                                downstramlistDistributer = response.data.toMutableList()
                                downstramlistDistributer.clear() // Clear existing data
                                downstramlistDistributer.addAll(response.data) // Add new data
                                // Set up the nested RecyclerView
                                val clickListner = object : AsmClick {
                                    override fun onItemClicked(
                                        holder: AsmAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val bundle = Bundle()
                                        bundle.putString("id",downstramlistDistributer[position]._id)
                                        findNavController().navigate(R.id.FragmentNetwork,bundle)
                                    }

                                    override fun onTransferMoneyClicked(
                                        holder: AsmAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistDistributer[position]._id
                                        val refer_id = downstramlistDistributer[position].refer_id
                                        val balance = downstramlistDistributer[position].main_wallet
                                        val mobile = downstramlistDistributer[position].mobile
                                        val name = downstramlistDistributer[position].name
                                        val transferMoneyDialogFragment = TransferMoneyDialogFragment.newInstance(value,refer_id,balance,mobile,name)
                                        transferMoneyDialogFragment.show(childFragmentManager, "TransferMoneyDialogFragment")
                                    }

                                    override fun onReverseMoneyClicked(
                                        holder: AsmAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistDistributer[position]._id
                                        val refer_id = downstramlistDistributer[position].refer_id
                                        val balance = downstramlistDistributer[position].main_wallet
                                        val mobile = downstramlistDistributer[position].mobile
                                        val name = downstramlistDistributer[position].name
                                        val reverseMoneyDialogFragment = ReverseMoneyDialogFragment.newInstance(value,refer_id,balance,mobile,name)
                                        reverseMoneyDialogFragment.show(childFragmentManager, "ReverseMoneyDialogFragment")
                                    }

                                    override fun onViewReportClicked(
                                        holder: AsmAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val value = downstramlistDistributer[position]._id
                                        val viewReportDialogFragment = ViewReportDialogFragment.newInstance(value)
                                        viewReportDialogFragment.show(childFragmentManager, "ViewReportDialogFragment")

                                    }

                                    override fun onViewWalletReportClicked(
                                        holder: AsmAdapter.ViewHolder,
                                        downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>,
                                        position: Int
                                    ) {
                                        val bundle = Bundle()
                                        bundle.putString("value",downstramlistDistributer[position]._id)
                                        findNavController().navigate(R.id.WalletReportdownStarem,bundle)
                                    }


                                }
                                val adapter = AsmAdapter(myActivity, downstramlistDistributer,clickListner)
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
        dBinding.date.visibility= GONE
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

            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    override fun setData() {

    }


}