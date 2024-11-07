package app.pay.pandapro.fragments.reports

import CategoryListIdResponse
import android.annotation.SuppressLint
import android.app.Dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.CategoryAdapter
import app.pay.pandapro.adapters.CategoryIDAdapter
import app.pay.pandapro.adapters.UtilityTransactionAdapter
import app.pay.pandapro.databinding.DialogCategoryListBinding
import app.pay.pandapro.databinding.FragmentUtilityTransactionsBinding
import app.pay.pandapro.databinding.LytUtilityFilterBinding

import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.interfaces.BbpsCategoryClick
import app.pay.pandapro.interfaces.BbpsCategoryIDClick
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.UtilityTransactionClick

import app.pay.pandapro.responsemodels.bbpscatagory.CatagoryListResponse
import app.pay.pandapro.responsemodels.bbpsenquiry.BbpsEnquiry

import app.pay.pandapro.responsemodels.utilitytxn.Data

import app.pay.pandapro.responsemodels.utilitytxn.UtilityTxnResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson


class UtilityTransactions : BaseFragment<FragmentUtilityTransactionsBinding>(FragmentUtilityTransactionsBinding::inflate){
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var start_date = ""
    private var end_date = ""
    private var biller_id = ""
    private var page = "0"
    private var count = "50"
    private var category_id = ""
    private lateinit var list:MutableList<Data>
    private lateinit var cateList:MutableList<app.pay.pandapro.responsemodels.bbpscatagory.Data>
    private lateinit var cateListId:MutableList<app.pay.pandapro.responsemodels.CategoryIdResponse.Data>
    private lateinit var dBinding: LytUtilityFilterBinding
    var tagValueId=""
    private lateinit var txnAdapter:UtilityTransactionAdapter

    override fun init() {

        nullActivityCheck()
        userSession = UserSession(requireContext())
        getTransactionList(start_date, end_date, biller_id, page, count, category_id)

    }

    private fun getTransactionList(startDate: String, endDate: String, billerId: String, pageNo: String, txnCount: String, categoryId: String) {
        binding.rvTransactionList.visibility=GONE
        binding.imageView.visibility= VISIBLE
        binding.llNoData.visibility= GONE
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getUtilityTransactions(requireContext(), token,startDate, endDate, billerId, pageNo, txnCount, categoryId, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response:UtilityTxnResponse=Gson().fromJson(message,UtilityTxnResponse::class.java)
                if (response.error==false){
                    if (response.data?.isNotEmpty() == true){
                        list= mutableListOf()
                        list.addAll(response.data)
                        val clickListner = object : UtilityTransactionClick {
                            override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int, type: Int) {
                                when (type) {
                                    1 -> {
                                        if(model[pos].service_name.equals("Recharge")){
                                            bbpsRechargEnquiry(model[pos]._id.toString())
                                        }else{
                                            bbpsEnquiry(model[pos]._id.toString())
                                        }
                                    }

                                    2 -> {
                                        if(model[pos].service_name.equals("Recharge")){
                                            bbpsRechargEnquiry(model[pos]._id.toString())
                                        }else{
                                            bbpsEnquiry(model[pos]._id.toString())
                                        }

                                    }

                                    else -> {
                                        openShareReceipt(model[pos]._id.toString())
                                    }
                                }
                            }
                        }
                        txnAdapter=UtilityTransactionAdapter(myActivity,list,clickListner)
                        binding.rvTransactionList.adapter=txnAdapter
                        binding.rvTransactionList.layoutManager=LinearLayoutManager(myActivity)


                        binding.rvTransactionList.visibility= VISIBLE
                        binding.imageView.visibility= GONE
                        binding.llNoData.visibility= GONE
                    }else{
                        binding.rvTransactionList.visibility=GONE
                        binding.imageView.visibility= GONE
                        binding.llNoData.visibility= VISIBLE
                    }

                }else{
                    binding.rvTransactionList.visibility=GONE
                    binding.imageView.visibility= GONE
                    binding.llNoData.visibility= VISIBLE
                }

            }

            override fun fail(from: String) {
                binding.rvTransactionList.visibility=GONE
                binding.imageView.visibility= GONE
                binding.llNoData.visibility= VISIBLE
            }
        })
    }

    private fun getCategoryList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.bbpsCategoryList(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CatagoryListResponse = Gson().fromJson(message, CatagoryListResponse::class.java)
                if (!response.error) {
                    cateList= mutableListOf()
                    cateList.clear()
                    cateList.addAll(response.data)
                    // Open the dialog after the list is updated
                    openServiceTypeListDialog(dBinding.edtServiceType,dBinding.lytBillerName,tagValueId)
                } else {
                    Toast.makeText(requireContext(), "Please Select Category", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun bbpsCategoryList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getBbpsServiceId(requireContext(), tagValueId,token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CategoryListIdResponse = Gson().fromJson(message, CategoryListIdResponse::class.java)
                if (!response.error) {
                    cateListId= mutableListOf()
                    cateListId.clear()
                    cateListId.addAll(response.data)
                    // Open the dialog after the list is updated
                    openServiceTypeListDialogId(dBinding.edtBillers)
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
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
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener {
            openFilterDialog()
        }
    }
    private fun openFilterDialog() {

        val filterDialog: Dialog = Dialog(myActivity)
        dBinding = LytUtilityFilterBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)
        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)
        dBinding.edtFromDate.setOnClickListener { CommonClass.showDatePickerDialog(myActivity,dBinding.edtFromDate) }
        dBinding.edtToDate.setOnClickListener {
            if (dBinding.edtFromDate.text.toString().isEmpty()){
                dBinding.edtFromDate.error="Select From Date"
                showToast(requireContext(),"Select From Date")
            }else{
                CommonClass.showDatePickerDialog(myActivity,dBinding.edtToDate) }
        }
        /*categoryList= mutableListOf()*/
      //  val catList=Category.getAllCategories()
        dBinding.edtServiceType.setOnClickListener {
            getCategoryList()
        }
dBinding.edtBillers.setOnClickListener{
    bbpsCategoryList()
}
        dBinding.rgCount.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb25->{
                    count="25"
                }
                R.id.rb50->{
                    count="50"
                }
                R.id.rb100->{
                    count="100"
                }
                R.id.rb150->{
                    count="150"
                }
                R.id.rb200->{
                    count="200"
                }
            }
        }

        dBinding.btnSubmit.setOnClickListener {
            val from= dBinding.edtFromDate.text.toString().ifEmpty { "" }
            val to= dBinding.edtToDate.text.toString().ifEmpty { "" }
            val caNumber=dBinding.edtCANumber.text.toString().ifEmpty { "" }
            getTransactionList(from, to, caNumber, page, count, tagValueId)
            filterDialog.dismiss()
        }


        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    private fun openServiceTypeListDialog(edtServiceType: EditText,lytBillerName: TextInputLayout,tagValueId:String) {
        val categoryDialog: Dialog = Dialog(myActivity)

        val dBinding = DialogCategoryListBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        categoryDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        categoryDialog.setContentView(dBinding.root)
        categoryDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        categoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        categoryDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom
        categoryDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.edtCateList.requestFocus()

        val clickListener = object : BbpsCategoryClick {
            override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.pandapro.responsemodels.bbpscatagory.Data>, pos: Int) {
                edtServiceType.setText(model[pos].service_name)

                this@UtilityTransactions.tagValueId = model[pos]._id.toString()

                categoryDialog.dismiss()
                if(model[pos].service_name.equals("Electricity")){
                    lytBillerName.visibility= View.VISIBLE
                }else{
                    lytBillerName.visibility= GONE
                }
            }
        }

        val adapter = CategoryAdapter(myActivity, cateList, clickListener)
        dBinding.rvBankList.adapter = adapter
        dBinding.rvBankList.layoutManager = LinearLayoutManager(myActivity)
        dBinding.edtCateList.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                val itemCount = dBinding.rvBankList.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    dBinding.llNoData.visibility = View.GONE
                    dBinding.rvBankList.visibility = View.VISIBLE
                } else {
                    dBinding.llNoData.visibility = View.VISIBLE
                    dBinding.rvBankList.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        categoryDialog.setCancelable(true)
        categoryDialog.show()
    }


    private fun openServiceTypeListDialogId(edtBillers: EditText) {
        val categoryDialog: Dialog = Dialog(myActivity)
        val dBinding = DialogCategoryListBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        categoryDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        categoryDialog.setContentView(dBinding.root)
        categoryDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        categoryDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        categoryDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom
        categoryDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.edtCateList.requestFocus()

        val click = object : BbpsCategoryIDClick {
            override fun onItemClickedId(holder: RecyclerView.ViewHolder, model: List<app.pay.pandapro.responsemodels.CategoryIdResponse.Data>, pos: Int) {
                Log.d("CategoryClick", "Clicked on: ${model[pos].name}")
                edtBillers.setText(model[pos].name)
                categoryDialog.dismiss()
            }
        }

        val adapter = CategoryIDAdapter(myActivity, cateListId, click)
        dBinding.rvBankList.adapter = adapter
        dBinding.rvBankList.layoutManager = LinearLayoutManager(myActivity)

        dBinding.edtCateList.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
                val itemCount = dBinding.rvBankList.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    dBinding.llNoData.visibility = View.GONE
                    dBinding.rvBankList.visibility = View.VISIBLE
                } else {
                    dBinding.llNoData.visibility = View.VISIBLE
                    dBinding.rvBankList.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        categoryDialog.setCancelable(true)
        categoryDialog.show()
    }

    override fun setData() {

    }

    private fun openShareReceipt(id: String) {
        val bottomSheet = SingleUtilityTransaction()
        val bundle = Bundle()
        bundle.putString("id", id)
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
    private fun bbpsEnquiry(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.bbpsEnquiry(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: BbpsEnquiry = Gson().fromJson(message, BbpsEnquiry::class.java)
                if (!response.error) {
                    txnAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun bbpsRechargEnquiry(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.bbpsRechargEnquiry(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: BbpsEnquiry = Gson().fromJson(message, BbpsEnquiry::class.java)
                if (!response.error) {
                    txnAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

