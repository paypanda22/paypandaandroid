package app.pay.retailers.fragments.dmt

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.ScannerListAdapter
import app.pay.retailers.databinding.DialogScannerDevicesBinding
import app.pay.retailers.databinding.FragmentAadhaarAuthBank2Binding
import app.pay.retailers.databinding.LytDmtOtoBinding
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.FingerPrintScanner
import app.pay.retailers.helperclasses.ScanFingelDMTPaysprint
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.interfaces.ScannerListClick
import app.pay.retailers.responsemodels.dmtkyc.DMTKycResponse
import app.pay.retailers.responsemodels.verifycustomer.VerifyCustomerResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class FragmentAadhaarAuthBank2 : BaseFragment<FragmentAadhaarAuthBank2Binding>(FragmentAadhaarAuthBank2Binding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var scanFinger: ScanFingelDMTPaysprint
    private lateinit var customerMobile: String
    private lateinit var apiID: String
    private lateinit var startForScannerResult: ActivityResultLauncher<Intent>
    private lateinit var myActivity: FragmentActivity
    private var selectedPackage = ""
    private var fData = ""
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        customerMobile = arguments?.getString("mobile") ?: ""
        apiID = arguments?.getString("apiID") ?: ""

        startForScannerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleScannerResult(result)
        }

        scanFinger = ScanFingelDMTPaysprint(requireActivity(), userSession, startForScannerResult)
    }

    override fun addListeners() {
        binding.atvScanner.setOnClickListener {
            openDeviceDialog()
        }
        binding.btnSubmit.setOnClickListener {
            if(binding.etAadhaarNumber.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Please Enter Aadhaar ", Toast.LENGTH_SHORT).show()
            } else if (binding.atvScanner.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Select Scanner Device First", Toast.LENGTH_SHORT).show()
            } else if (selectedPackage.isEmpty()) {
                Toast.makeText(requireContext(), "Select Device First", Toast.LENGTH_SHORT).show()
            } else {
                scanFinger.yourDevicePackage(selectedPackage)
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun handleScannerResult(result: ActivityResult) {
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK && data != null) {
            fData = data.getStringExtra("PID_DATA").toString()
            if (!fData.isNullOrEmpty()) {
                // Send fingerprint data to API
                paysprintsendotpForRemitter(fData)
            } else {
                Toast.makeText(requireContext(), "No FingerPrint Data Found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Unable to Capture FingerPrint Data", Toast.LENGTH_SHORT).show()
        }
    }
    private fun paysprintsendotpForRemitter(fData: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        val data = CommonClass.base64urlEncode(fData)

        // Include fingerprint and other necessary data in the API request
        requestData["mobile"] = customerMobile
        requestData["lat"] = userSession.getData(Constant.M_LAT).toString()
        requestData["long"] = userSession.getData(Constant.M_LONG).toString()
        requestData["data"] = fData
        requestData["aadhaar_number"] = binding.etAadhaarNumber.text.toString()
        requestData["userId"] = ""

        // Make the API call
        UtilMethods.paysprintsendotpForRemitter(requireContext(), token, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DMTKycResponse = Gson().fromJson(message, DMTKycResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(requireActivity(), "SUCCESS", response.message, "success", object :
                        MyClick {
                        override fun onClick() {
                            openTransactionFilterDialogKYC()
                        }
                    })
                } else {
                    ShowDialog.bottomDialogSingleButton(requireActivity(), "ERROR", response.message, "error", object :
                        MyClick {
                        override fun onClick() {
                           binding.etAadhaarNumber.text?.clear()
                        }
                    })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(requireActivity(), "ERROR", "Unable to Complete Authentication", "error", object :
                    MyClick {
                    override fun onClick() {
                        binding.etAadhaarNumber.text?.clear()
                    }
                })
            }
        })
    }

    private fun openTransactionFilterDialogKYC() {
        val filterDialog: Dialog = Dialog(myActivity)
        val dBinding = LytDmtOtoBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        filterDialog.setContentView(dBinding.root)
        filterDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        filterDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.btnVerify.setOnClickListener{
            if(!dBinding.etOtp.text.toString().isNotEmpty()){
                Toast.makeText(requireContext(), "Please Enter Otp...", Toast.LENGTH_SHORT).show()
            }else{
                paysprintCreateRemitter(dBinding.etOtp.text.toString(),filterDialog)
            }

        }

        filterDialog.setCancelable(true)
        filterDialog.show()
    }

    private fun paysprintCreateRemitter(otp: String, filterDialog: Dialog) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["mobile"] = customerMobile
        requestData["userId"] = token
        requestData["otp"] = otp
        UtilMethods.paysprintCreateRemitter(requireContext(),token,requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: VerifyCustomerResponse = Gson().fromJson(message, VerifyCustomerResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Congratulations!",
                        response.message,
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                filterDialog.dismiss()
                                findNavController().popBackStack()
                            }
                        })
                } else {
                    filterDialog.dismiss()
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "ERROR !",
                        response.message,
                        "error",
                        object : MyClick {
                            override fun onClick() {
                                filterDialog.dismiss()
                                findNavController().popBackStack()
                            }
                        })
                }
            }

            override fun fail(from: String) {
                filterDialog.dismiss()
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "ERROR !",
                    "Request Failed",
                    "error",
                    object : MyClick {
                        override fun onClick() {
                            filterDialog.dismiss()
                            findNavController().popBackStack()
                        }
                    })
            }
        })
    }

    private fun openDeviceDialog() {
        val scannerDialog: Dialog = Dialog(requireActivity())
        val dBinding = DialogScannerDevicesBinding.inflate(requireActivity().layoutInflater)
        dBinding.root.background = ContextCompat.getDrawable(requireActivity(), R.drawable.curved_background_with_shadow)
        scannerDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        scannerDialog.setContentView(dBinding.root)
        scannerDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scannerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        scannerDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom
        scannerDialog.window?.setGravity(Gravity.BOTTOM)

        val list = FingerPrintScanner.getScannerList()
        val clickListener = object : ScannerListClick {
            override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<FingerPrintScanner>, pos: Int) {
                userSession.setData(Constant.DEVICE_NAME, model[pos].getDeviceName())
                userSession.setData(Constant.SCANNER_PACKAGE, model[pos].getPackageName())
                binding.atvScanner.setText(model[pos].getDeviceName())
                selectedPackage = model[pos].getPackageName()
                scannerDialog.dismiss()
            }
        }

        val adapter = ScannerListAdapter(requireActivity(), list, clickListener)
        dBinding.rvScanner.adapter = adapter
        dBinding.rvScanner.layoutManager = LinearLayoutManager(requireActivity())

        scannerDialog.setCancelable(false)
        scannerDialog.show()
    }
    override fun setData() {
        if (userSession.getData(Constant.DEVICE_NAME) != null) {
            binding.atvScanner.setText(userSession.getData(Constant.DEVICE_NAME))
        }
        if (userSession.getData(Constant.SCANNER_PACKAGE) != null) {
            selectedPackage = userSession.getData(Constant.SCANNER_PACKAGE).toString()
        }
    }
    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }
}