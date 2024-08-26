package app.pay.panda.fragments.reports

import CustomTypefaceSpan
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.PaymentRequestDialogFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.RequestListAdapter
import app.pay.panda.adapters.RequestListAdapterDist
import app.pay.panda.adapters.RequestListAdminAdapter
import app.pay.panda.databinding.FragmentWalletRequestListBinding
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.RequetWalletClick
import app.pay.panda.responsemodels.PaymentRequestToAdminResponse

import app.pay.panda.responsemodels.requestList.Request
import app.pay.panda.responsemodels.requestList.WalletRequestListResponse
import app.pay.panda.responsemodels.transferTo.Data
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class WalletRequestListFragment : BaseFragment<FragmentWalletRequestListBinding>(FragmentWalletRequestListBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var txnList:MutableList<Request>
    var adminList: MutableList<app.pay.panda.responsemodels.Request> = mutableListOf()
    private var transferTo = ""
    private lateinit var toList: MutableList<Data>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getWalletRequestList()
        //   getTransferToList()
      //  walletRequestListDist()

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

        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["bank"]=""
        requestData["user_id"]=token
        requestData["end_date"]=""
        requestData["start_date"]=""
        requestData["status"]=""
        requestData["sortKey"]=""
        requestData["sortType"]=""
        requestData["count"]=50
        requestData["page"]=0
        UtilMethods.walletRequestList(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {

                val response: WalletRequestListResponse =
                    Gson().fromJson(message, WalletRequestListResponse::class.java)

                if (!response.error){


                    txnList= mutableListOf()
                    txnList.addAll(response.data.requestList)
                    val adapter=RequestListAdapter(myActivity,txnList)
                    binding.rvWalletRequestList.adapter=adapter
                    binding.rvWalletRequestList.layoutManager=LinearLayoutManager(myActivity)


                    binding.rvWalletRequestList.visibility= VISIBLE
                    binding.llNoData.visibility= GONE
                    binding.imageView.visibility= GONE
                }else{

                    binding.rvWalletRequestList.visibility=GONE
                    binding.llNoData.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }
            }

            override fun fail(from: String) {

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
                txnList.clear()
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



    private fun paymentRequestToAdmin() {
        val progressBar = CustomProgressBar()
        progressBar.showProgress(requireContext())
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["count"]=10
        requestData["end_date"]=""
        requestData["max_amt"]=""
        requestData["min_amt"]=""
        requestData["page"]=0
        requestData["sortKey"]=""
        requestData["sortType"]=""
        requestData["start_date"]=""
        requestData["status"]=""
        requestData["user_id"]=token
        UtilMethods.paymentRequestToAdmin(requireContext(),token,requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                progressBar.hideProgress()
                val response: PaymentRequestToAdminResponse =
                    Gson().fromJson(message, PaymentRequestToAdminResponse::class.java)
                if (!response.error){
                    progressBar.hideProgress()
                    adminList.addAll(response.data.requestList)
                    val clickListner = object : RequetWalletClick {
                        override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.panda.responsemodels.Request>, pos: Int, amount: String, date: String, status: String,
                                                   id:String,mobile:String,userType:String,remark:String) {
                            if (status.equals("Approved")){
                                Toast.makeText(myActivity, "Status is Already Approved", Toast.LENGTH_SHORT).show()
                            }else {
                                val paymentRequestDialogFragment = PaymentRequestDialogFragment()
                                val bundle = Bundle().apply {
                                    putString("amount", amount)
                                    putString("date", date)
                                    putString("status", status)
                                    putString("id", id)
                                    putString("mobile", mobile)
                                    putString("userType", userType)
                                    putString("remark", remark)
                                }

                                paymentRequestDialogFragment.arguments = bundle

                                paymentRequestDialogFragment.show(
                                    childFragmentManager,
                                    "PaymentRequestDialogFragment"
                                )
                            }
                        }
                    }
                    val adapter= RequestListAdminAdapter(myActivity,adminList,clickListner)
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
    /*    binding.rbLoginMethod.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.Company.id) {

            }else if(checkedId == binding.retailer.id) {

            }else {


            }*/

        binding.rbLoginMethod.setOnClickListener { view ->
            val popup = PopupMenu(activity, view)

            // Inflate the menu first
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_options, popup.menu)

            val userType = userSession.getData(Constant.USER_TYPE_NAME)
            Log.d("UserType", "User type: $userType")  // Log the user type

            // Now modify the visibility based on the user type
            when (userType) {
                "Retailer" -> {
                    Log.d("Menu", "Setting Retailer menu visibility")
                    popup.menu.findItem(R.id.Company)?.isVisible = true
                    popup.menu.findItem(R.id.Super_Distributer)?.isVisible = true
                    popup.menu.findItem(R.id.Distributer)?.isVisible = false
                    popup.menu.findItem(R.id.member)?.isVisible = false
                }
                "Distributor" -> {
                    Log.d("Menu", "Setting Distributor menu visibility")
                    popup.menu.findItem(R.id.Company)?.isVisible = true
                    popup.menu.findItem(R.id.Super_Distributer)?.isVisible = true
                    popup.menu.findItem(R.id.member)?.isVisible = true
                    popup.menu.findItem(R.id.Distributer)?.isVisible = false
                }
                "Super Distributor" -> {
                    Log.d("Menu", "Setting Super Distributor menu visibility")
                    popup.menu.findItem(R.id.Company)?.isVisible = true
                    popup.menu.findItem(R.id.Super_Distributer)?.isVisible = false
                    popup.menu.findItem(R.id.member)?.isVisible = true
                    popup.menu.findItem(R.id.Distributer)?.isVisible = false
                }
                else -> {
                    Log.d("Menu", "User type not recognized")
                }
            }

            // Customize menu item appearance
            for (i in 0 until popup.menu.size()) {
                val menuItem = popup.menu.getItem(i)
                val spannableTitle = SpannableString(menuItem.title)
                spannableTitle.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.grey_60)),
                    0,
                    spannableTitle.length,
                    0
                )
                spannableTitle.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    spannableTitle.length,
                    0
                )
                val typeface = ResourcesCompat.getFont(requireContext(), R.font.mostserrat_extra_bold)
                if (typeface != null) {
                    spannableTitle.setSpan(
                        CustomTypefaceSpan("", typeface),
                        0,
                        spannableTitle.length,
                        0
                    )
                }
                menuItem.title = spannableTitle
            }

            // Handle item clicks
            popup.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.Company -> {
                        getWalletRequestList()
                        true
                    }
                    R.id.Super_Distributer -> {
                        walletRequestListDist()
                        true
                    }
                    R.id.Distributer -> {
                        paymentRequestToAdmin()
                        true
                    }
                    R.id.member->{
                        paymentRequestToAdmin()
                        true
                    }
                    else -> false
                }
            }

            popup.gravity = Gravity.END or Gravity.BOTTOM
            popup.show() // Show the popup menu
        }

    }

    override fun setData() {

    }
   /* @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @Deprecated("Deprecated in Java")
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val userType = userSession.getData(Constant.USER_TYPE_NAME)
        Log.d("UserType", "User type: $userType")  // Log the user type

        when (userType) {
            "Retailer" -> {
                Log.d("Menu", "Setting Retailer menu visibility")
                menu.findItem(R.id.Company)?.isVisible = true
                menu.findItem(R.id.Super_Distributer)?.isVisible = true
                menu.findItem(R.id.Distributer)?.isVisible = false
                menu.findItem(R.id.member)?.isVisible = false
            }
            "Distributor" -> {
                Log.d("Menu", "Setting Distributor menu visibility")
                menu.findItem(R.id.Company)?.isVisible = true
                menu.findItem(R.id.Super_Distributer)?.isVisible = true
                menu.findItem(R.id.member)?.isVisible = true
                menu.findItem(R.id.Distributer)?.isVisible = false
            }
            "Super Distributor" -> {
                Log.d("Menu", "Setting Super Distributor menu visibility")
                menu.findItem(R.id.Company)?.isVisible = true
                menu.findItem(R.id.Super_Distributer)?.isVisible = false
                menu.findItem(R.id.member)?.isVisible = true
                menu.findItem(R.id.Distributer)?.isVisible = false
            }
            else -> {
                Log.d("Menu", "User type not recognized")
            }
        }
    }*/


}