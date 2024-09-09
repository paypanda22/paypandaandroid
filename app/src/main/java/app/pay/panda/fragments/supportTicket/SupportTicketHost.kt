package app.pay.panda.fragments.supportTicket

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.ViewPagerAdapter
import app.pay.panda.databinding.FragmentSupportTicketHostBinding
import app.pay.panda.databinding.LytDmtFilterBinding
import app.pay.panda.databinding.LytTicketListBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.OnFilterSubmitListener
import app.pay.panda.responsemodels.bbpsenquiry.BbpsEnquiry
import app.pay.panda.responsemodels.dmtstatus.DmtDisputePriority
import app.pay.panda.responsemodels.dmtstatusfilter.DMTStatusFilter
import app.pay.panda.responsemodels.filter.Data
import app.pay.panda.responsemodels.filter.DepartmentReposne
import app.pay.panda.responsemodels.supportTickets.DataX
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class SupportTicketHost : BaseFragment<FragmentSupportTicketHostBinding>(FragmentSupportTicketHostBinding::inflate) {
    private lateinit var userSession: UserSession
    private var count = 50
    private var start_date = ""
    private var end_date = ""
    private lateinit var myActivity: FragmentActivity
    private lateinit var dBinding: LytTicketListBinding
    private lateinit var departmentList: MutableList<Data>
    private lateinit var priorityList: MutableList<app.pay.panda.responsemodels.dmtstatus.Data>
    private lateinit var statusList: MutableList<app.pay.panda.responsemodels.dmtstatusfilter.Data>
    var selectedDepartment=""
    var   selectedPriority =""
    var   selectedStatus =""
    private var filterListener: OnFilterSubmitListener? = null
    override fun init() {
       nullActivityCheck()
        userSession= UserSession(requireContext())


        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter=adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {

                0 -> "Add Ticket"
                1->"Support Ticket List"
                else -> "Support Ticket List"

            }
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> binding.ivMenu.visibility = View.GONE // Hide ivMenu when on the "Add Ticket" tab
                    else -> binding.ivMenu.visibility = View.VISIBLE // Show ivMenu on other tabs
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Optionally handle unselected tab state
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Optionally handle reselected tab state
            }
        })
    }


    private fun nullActivityCheck() {
        if(activity !=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivMenu.setOnClickListener {
            //filterListener?.let { it1 -> openTransactionFilterDialog(it1) }
        }

        dBinding.spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedDepartment = departmentList[position].toString()
                // Do something with the selected department
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected, if necessary
            }
        }

        dBinding.spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                 selectedPriority = priorityList[position].toString()
                // Do something with the selected priority
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected, if necessary
            }
        }

        dBinding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedStatus = statusList[position].toString()
                // Do something with the selected status
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected, if necessary
            }
        }

    }

    private fun department() {
        departmentList = mutableListOf()  // Initialize here
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.department(requireContext(), token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: DepartmentReposne = Gson().fromJson(message, DepartmentReposne::class.java)
                if (!response.error) {
                    departmentList.clear()  // Clear previous data if any
                    departmentList.addAll(response.data)  // Add new data
                    val departmentAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        departmentList.map { it.department }  // Assuming Data has a 'name' property
                    )
                    departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dBinding.spinnerDepartment.adapter = departmentAdapter
                } else {
                   // Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dmtDisputePriority() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.dmtDisputePriority(requireContext(), token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: DmtDisputePriority = Gson().fromJson(message, DmtDisputePriority::class.java)
                if (!response.error) {
                    // Initialize or clear the list before adding new data
                    priorityList = response.data.toMutableList()

                    // Create a list of strings to use with the ArrayAdapter
                    val priorityNames = priorityList.map { it.priority } // Assuming Data has a 'name' property

                    val priorityAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        priorityNames
                    )
                    priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dBinding.spinnerPriority.adapter = priorityAdapter
                } else {
                    // Handle error case
                   // Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dmtstatus() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.dmtstatus(requireContext(), token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: DMTStatusFilter = Gson().fromJson(message, DMTStatusFilter::class.java)
                if (!response.error) {
                    // Initialize or clear the list before adding new data
                    statusList = response.data.toMutableList()

                    // Create a list of strings to use with the ArrayAdapter
                    val statusNames = statusList.map { it.name } // Assuming Data has a 'name' property

                    val statusAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        statusNames
                    )
                    statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    dBinding.spinnerStatus.adapter = statusAdapter
                } else {
                    // Handle error case
                   // Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openTransactionFilterDialog(listener: OnFilterSubmitListener) {
        val filterDialog: Dialog = Dialog(myActivity)
        dBinding = LytTicketListBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background = ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)

        // Fetch data for spinners and setup listeners
        dmtstatus()
        department()
        dmtDisputePriority()

        val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
        dBinding.edtFromDate.setText(todayDate)
        dBinding.edtToDate.setText(todayDate)

        dBinding.edtFromDate.setOnClickListener { CommonClass.showDatePickerDialog(myActivity, dBinding.edtFromDate) }
        dBinding.edtToDate.setOnClickListener {
            if (dBinding.edtFromDate.text.toString().isEmpty()) {
                dBinding.edtFromDate.error = "Select From Date"
            } else {
                CommonClass.showDatePickerDialog(myActivity, dBinding.edtToDate)
            }
        }

        dBinding.rgCount.setOnCheckedChangeListener { group, checkedId ->
            count = when (checkedId) {
                R.id.rb25 -> 25
                R.id.rb50 -> 50
                R.id.rb100 -> 100
                R.id.rb150 -> 150
                R.id.rb200 -> 200
                R.id.more -> 400
                else -> 50 // Default value
            }
        }

        dBinding.btnSubmit.setOnClickListener {
            // Validate selections and invoke the listener
            listener.onFilterSubmit(
                dBinding.edtFromDate.text.toString(),
                dBinding.edtToDate.text.toString(),
                selectedDepartment,
                selectedPriority,
                selectedStatus,
                count
            )
            filterDialog.dismiss()
        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }



    override fun setData() {

    }


}