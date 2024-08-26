package app.pay.panda.fragments.buyPackage

import PackageDetailAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.PackaeServiceAdapter

import app.pay.panda.databinding.FragmentPackageDetailBinding
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.PackageServices.Data
import app.pay.panda.responsemodels.PackageServices.PackageServicesResponse
import app.pay.panda.responsemodels.PackageServices.Slot

import app.pay.panda.responsemodels.packagedetail.BBPSWiseService

import app.pay.panda.responsemodels.packagedetail.OtherComm
import app.pay.panda.responsemodels.packagedetail.PackageDetailResponse
import app.pay.panda.retrofit.ApiMethods
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class FragmentPackageDetail : BaseFragment<FragmentPackageDetailBinding>(FragmentPackageDetailBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var txnDetailList:MutableList<OtherComm>
    private lateinit var dataid:MutableList<BBPSWiseService>
    private lateinit var bbpsWiseService:MutableList<Data>

    private var packageId: String? = null
    private var selectedServiceId: String? = null
    private var bbpsServiceNames: String? = null


    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        packageId = arguments?.getString("id")
        txnDetailList = mutableListOf()
        packageDatails()

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        // Toggle BBPS Services Section
        binding.llBBPSHeader.setOnClickListener {
            if (binding.llBBPSContent.visibility == View.GONE) {
                binding.llBBPSContent.visibility = View.VISIBLE
                binding.ivBBPSArrow.setImageResource(R.drawable.down_line) // Arrow pointing right when collapsed
            } else {
                binding.llBBPSContent.visibility = View.GONE
                binding.ivBBPSArrow.setImageResource(R.drawable.arrow_right_1) // Arrow pointing down when expanded
            }
        }

        // Toggle Commissions Section
        binding.llCommissionsHeader.setOnClickListener {

            if (binding.llCommissionsContent.visibility == View.VISIBLE) {
                binding.llCommissionsContent.visibility = View.GONE
                binding.ivCommissionsArrow.setImageResource(R.drawable.arrow_right_1)
                binding.llNoData.visibility = GONE
                binding.rvPackageList.visibility = GONE


            } else {
                binding.llCommissionsContent.visibility = View.VISIBLE
                binding.ivCommissionsArrow.setImageResource(R.drawable.down_line) // Arrow pointing down when expanded

                if (::myActivity.isInitialized) {
                    // Ensure txnDetailList has data before setting the adapter
                    if (txnDetailList.isNotEmpty()) {

                        val packageAdapter = PackageDetailAdapter(txnDetailList)
                        binding.rvPackageList.adapter = packageAdapter
                        binding.rvPackageList.layoutManager = LinearLayoutManager(myActivity)
                        binding.llNoData.visibility = GONE
                        binding.rvPackageList.visibility = VISIBLE
                        binding.imageView.visibility = GONE

                    } else {
                        // Handle case where txnDetailList is empty
                        binding.llNoData.visibility = VISIBLE
                        binding.rvPackageList.visibility = GONE
                        binding.imageView.visibility = GONE
                        binding.comDetail.visibility= GONE

                    }
                } else {

                    // Handle case where myActivity is not initialized
                    Toast.makeText(requireContext(), "Activity is not initialized", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }
    override fun setData() {

    }

    private fun packageDatails() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.packageDatails(requireContext(), packageId.toString(), token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: PackageDetailResponse = Gson().fromJson(message, PackageDetailResponse::class.java)
                if (!response.error){
                    if (txnDetailList.isNotEmpty()){
                        txnDetailList.clear()
                    }
                    binding.imageView.visibility=View.GONE
                    dataid= response.data.BBPSWiseServices.toMutableList()
                    txnDetailList.addAll(response.data.OtherComm)
                        // Map the IDs for the BBPSWiseServices
                        val bbpsServiceNames = response.data.BBPSWiseServices.map { it.service }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bbpsServiceNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerServiceOperator.adapter = adapter

                    } else {
                        binding.comDetail.visibility= GONE
                        binding.llNoData.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                        binding.rvPackageList.visibility = GONE
                    }
            }

            override fun fail(from: String) {
                // Handle the failure
                binding.comDetail.visibility= GONE
            }
        })
    }




    private fun bbpsServices() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.bbpsServices(requireContext(), token,packageId.toString(),selectedServiceId.toString(),"0","20", object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: PackageServicesResponse = Gson().fromJson(message, PackageServicesResponse::class.java)
                if (!response.error){
                    bbpsWiseService = mutableListOf()
                    if (bbpsWiseService.isNotEmpty()){
                        bbpsWiseService.clear()
                    }
                    bbpsWiseService.addAll(response.data)
                    val packageserviceAdapter = PackaeServiceAdapter(bbpsWiseService)
                    binding.rvPackageserviceList.adapter = packageserviceAdapter
                    binding.rvPackageserviceList.layoutManager = LinearLayoutManager(myActivity)

                    binding.llNoData1.visibility = GONE
                    binding.rvPackageserviceList.visibility = VISIBLE
                    binding.imageView1.visibility = GONE
                    } else {
                        binding.llNoData1.visibility = GONE
                        binding.imageView.visibility = GONE
                        binding.rvPackageserviceList.visibility = GONE
                    }
                }
            override fun fail(from: String) {
                binding.imageView1.visibility = GONE
                binding.rvPackageserviceList.visibility = GONE
             //   Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.spinnerServiceOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val selectedServiceName = parent.getItemAtPosition(position).toString()

                // Get the corresponding service ID
                selectedServiceId =dataid[position]._id
                bbpsServices()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no item is selected if needed
            }
        }
    }
}