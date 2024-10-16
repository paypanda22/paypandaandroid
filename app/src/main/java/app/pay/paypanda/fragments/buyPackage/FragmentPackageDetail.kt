package app.pay.paypanda.fragments.buyPackage

import PackageDetailAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.adapters.PackaeServiceAdapter

import app.pay.paypanda.databinding.FragmentPackageDetailBinding
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.responsemodels.PackageServices.Data
import app.pay.paypanda.responsemodels.PackageServices.PackageServicesResponse

import app.pay.paypanda.responsemodels.packagedetail.BBPSWiseService

import app.pay.paypanda.responsemodels.packagedetail.OtherComm
import app.pay.paypanda.responsemodels.packagedetail.PackageDetailResponse
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
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



    // Inside your function
    private fun packageDatails() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.packageDatails(requireContext(), packageId.toString(), token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: PackageDetailResponse = Gson().fromJson(message, PackageDetailResponse::class.java)
                if (!response.error){

                    // Initialize dataid to avoid UninitializedPropertyAccessException
                    dataid = mutableListOf()

                    // Clear the list if not empty
                    if (txnDetailList.isNotEmpty()) {
                        txnDetailList.clear()
                    }

                    binding.imageView.visibility = View.GONE

                    // Check if BBPSWiseServices is not null or empty
                    val bbpsWiseServicesList = response.data.BBPSWiseServices
                    if (!bbpsWiseServicesList.isNullOrEmpty()) {
                        dataid = bbpsWiseServicesList.toMutableList() // Assign data to dataid
                        val bbpsServiceNames = bbpsWiseServicesList.map { it.service }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, bbpsServiceNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerServiceOperator.adapter = adapter
                    } else {
                        binding.llBBPSHeader.visibility= GONE
                        binding.spinnerServiceOperator.visibility = View.GONE
                    }

                    // Check if OtherComm is not null or empty
                    val otherCommList = response.data.OtherComm
                    if (!otherCommList.isNullOrEmpty()) {
                        txnDetailList.addAll(otherCommList)
                    } else {
                        binding.comDetail.visibility = View.GONE
                    }

                    // If both lists are empty or null, show "No Data" view
                    if (bbpsWiseServicesList.isNullOrEmpty() && otherCommList.isNullOrEmpty()) {
                        binding.llNoData.visibility = View.VISIBLE
                        binding.rvPackageList.visibility = View.GONE
                    } else {
                        binding.llNoData.visibility = View.GONE
                        binding.rvPackageList.visibility = View.VISIBLE
                    }

                } else {
                    binding.comDetail.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                    binding.rvPackageList.visibility = View.GONE
                    binding.imageView.visibility = View.GONE
                }
            }

            override fun fail(from: String) {
                binding.comDetail.visibility = View.GONE
                binding.llNoData.visibility = View.VISIBLE
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