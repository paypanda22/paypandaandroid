package app.pay.pandapro.fragments.buyPackage

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
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.PackaeServiceAdapter

import app.pay.pandapro.databinding.FragmentPackageDetailBinding
import app.pay.pandapro.helperclasses.CustomProgressBar
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.PackageServices.Data
import app.pay.pandapro.responsemodels.PackageServices.PackageServicesResponse

import app.pay.pandapro.responsemodels.packagedetail.BBPSWiseService

import app.pay.pandapro.responsemodels.packagedetail.OtherComm
import app.pay.pandapro.responsemodels.packagedetail.PackageDetailResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
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
                // Show content and update arrow
                binding.llCommissionsContent.visibility = View.VISIBLE
                binding.ivCommissionsArrow.setImageResource(R.drawable.down_line) // Arrow pointing down when expanded

                if (::myActivity.isInitialized) {
                    // Show progress bar before loading data
                    val progressBar = CustomProgressBar()
                    progressBar.showProgress(myActivity)

                    // Run the loading process in a background thread to avoid blocking the UI
                    Thread {
                        // Simulate heavy data loading (replace this with your actual data-loading logic)
                        val dataLoaded = loadTransactionData()

                        // Update the UI on the main thread after data loading is completed
                        activity?.runOnUiThread {
                            if (txnDetailList.isNotEmpty()) {
                                // Set up adapter after data is loaded
                                val packageAdapter = PackageDetailAdapter(txnDetailList)
                                binding.rvPackageList.adapter = packageAdapter
                                binding.rvPackageList.layoutManager =
                                    LinearLayoutManager(myActivity)

                                // Show/hide views based on data
                                binding.llNoData.visibility = GONE
                                binding.rvPackageList.visibility = VISIBLE
                                binding.imageView.visibility = GONE
                            } else {
                                // Handle case where txnDetailList is empty
                                binding.llNoData.visibility = VISIBLE
                                binding.rvPackageList.visibility = GONE
                                binding.imageView.visibility = GONE
                                binding.comDetail.visibility = GONE
                            }

                            // Hide the progress bar after the data has been loaded
                            progressBar.hideProgress()
                        }
                    }.start()

                } else {
                    // Handle case where myActivity is not initialized
                    Toast.makeText(
                        requireContext(),
                        "Activity is not initialized",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }



    }
    // Method to simulate data loading
    private fun loadTransactionData(): Boolean {
        // Simulate some delay (replace this with actual data loading logic)
        Thread.sleep(3000)  // Simulating a delay of 3 seconds
        return true  // Indicate that data loading is completed
    }

    override fun setData() {

    }



    // Inside your function
    private fun packageDatails() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val progressBar = CustomProgressBar()
        progressBar.showProgress(context)
        UtilMethods.packageDatails(requireContext(), packageId.toString(), token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: PackageDetailResponse = Gson().fromJson(message, PackageDetailResponse::class.java)
                if (!response.error){
progressBar.hideProgress()
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
                        binding.cardCommision.visibility = View.GONE
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
                    progressBar.hideProgress()
                    binding.comDetail.visibility = View.GONE
                    binding.llNoData.visibility = View.VISIBLE
                    binding.rvPackageList.visibility = View.GONE
                    binding.imageView.visibility = View.GONE
                }
            }

            override fun fail(from: String) {
                progressBar.hideProgress()
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