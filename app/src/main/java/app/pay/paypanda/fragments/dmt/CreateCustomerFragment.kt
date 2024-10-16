package app.pay.paypanda.fragments.dmt

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.databinding.FragmentCreateCustomerBinding
import app.pay.paypanda.helperclasses.ActivityExtensions
import app.pay.paypanda.helperclasses.CommonClass
import app.pay.paypanda.helperclasses.ShowDialog
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.MyClick
import app.pay.paypanda.responsemodels.createcustomer.CreateCustomerResponse
import app.pay.paypanda.responsemodels.customerotp.CustomerOtpResponse
import app.pay.paypanda.responsemodels.pincode.PinCodeResponse
import app.pay.paypanda.responsemodels.verifycustomer.VerifyCustomerResponse
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import app.pay.paypanda.retrofit.UtilMethods.createCustomer
import com.google.gson.Gson
import java.time.LocalDate


class CreateCustomerFragment : BaseFragment<FragmentCreateCustomerBinding>(FragmentCreateCustomerBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var customerMobile = ""
    private var type = ""
    private var apiID = ""
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        customerMobile = arguments?.getString("mobile").toString()
        type = arguments?.getString("type").toString()
        apiID = arguments?.getString("apiID").toString()
        if (apiID == "66bca8b95727c7563ad6e315") {
            if (type == "verify") {
                binding.lytOtp.visibility = View.VISIBLE
                binding.lytCreate.visibility = View.GONE
                binding.lytEdtOtp.visibility = View.GONE
            } else {
                binding.lytOtp.visibility = View.GONE
                binding.lytCreate.visibility = View.VISIBLE
                binding.lytEdtOtp.visibility = View.GONE
                binding.edtCustomerNumber.setText(customerMobile)
            }
        } else {
            binding.lytOtp.visibility = View.GONE
            binding.lytEdtOtp.visibility = View.VISIBLE
            binding.lytCreate.visibility = View.VISIBLE
            binding.edtCustomerNumber.setText(customerMobile)
        }


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
        binding.edtCustomerDob.setOnClickListener {
            CommonClass.showDatePickerDialog(myActivity, binding.edtCustomerDob)
        }
        binding.btnCreateCustomer.setOnClickListener {
            if (validateData()) {
                val token = userSession.getData(Constant.USER_TOKEN).toString()
                val requestData = hashMapOf<String, Any?>()
                requestData["country"] = "India"
                requestData["city"] = binding.edtCustomerCity.text.toString()
                requestData["state"] = binding.edtCustomerState.text.toString()
                requestData["pincode"] = binding.edtCustomerPincode.text.toString()
                requestData["district"] = binding.edtCustomerCity.text.toString()
                requestData["area"] = binding.edtCustomerArea.text.toString()
                requestData["user_id"] = token
                requestData["customer_mobile"] = binding.edtCustomerNumber.text.toString()
                requestData["name"] = binding.edtCustomerName.text.toString()
                requestData["dob"] = binding.edtCustomerDob.text.toString()
                requestData["api_id"] = apiID
                requestData["otp"] = binding.edtOtpPs.text.toString()
                createCustomer(requireContext(), requestData, object : MCallBackResponse {
                    override fun success(from: String, message: String) {
                        val response: CreateCustomerResponse = Gson().fromJson(message, CreateCustomerResponse::class.java)
                        if (!response.error) {
                            if (apiID == "66bca8b95727c7563ad6e315") {
                                Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                                binding.lytCreate.visibility = View.GONE
                                binding.lytOtp.visibility = View.VISIBLE
                            } else {
                                ShowDialog.bottomDialogSingleButton(
                                    myActivity,
                                    response.message,
                                    "You Can Process Transaction With this Customer Account",
                                    "success",
                                    object : MyClick {
                                        override fun onClick() {
                                            findNavController().popBackStack()
//                                            val bundle=Bundle()
//                                            bundle.putString("apiID",apiID)
//                                            bundle.putString("customerMobile",binding.edtCustomerNumber.text.toString())
//                                            bundle.putString("customerName",binding.edtCustomerName.text.toString())
//                                           findNavController().navigate(R.id.action_createCustomerFragment_to_addBeneficiaryFragment)
                                        }
                                    })

                            }

                        } else {
                            binding.lytOtp.visibility = View.GONE
                            binding.lytCreate.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun fail(from: String) {
                        binding.lytOtp.visibility = View.GONE
                        binding.lytCreate.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Request Failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }

        binding.tvResendOtp.setOnClickListener {
            if (customerMobile.isEmpty()) {
                Toast.makeText(requireContext(), "Provide a Valid Mobile Number", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                resendOtp()
            }
        }

        binding.btnVerifyCustomer.setOnClickListener {
            if (binding.edtOtpLogin.text.toString().length < 3) {
                Toast.makeText(requireContext(), "Enter a Valid OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyCustomer()

            }

        }

        binding.edtCustomerPincode.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtCustomerPincode.text.toString().length==6){
                    if (ActivityExtensions.isValidPinCode(binding.edtCustomerPincode.text.toString())){
                        checkPinCode()
                    }else{
                        Toast.makeText(requireContext(),"InValid PinCode",Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    private fun checkPinCode() {
        UtilMethods.getPinCodeData(requireContext(),binding.edtCustomerPincode.text.toString(),object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:PinCodeResponse=Gson().fromJson(message,PinCodeResponse::class.java)
                if (!response.error){
                    binding.edtCustomerCity.setText(response.data.district_name.lowercase())
                    binding.edtCustomerCity.error=null
                    binding.edtCustomerState.setText(response.data.state_name.lowercase())
                    binding.edtCustomerState.error=null
                    binding.edtCustomerArea.setText(response.data.division_name.lowercase())
                    binding.edtCustomerArea.error=null
                }else{
                    Toast.makeText(requireContext(),"PinCode Data Not Found",Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(),"PinCode Data Not Found",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verifyCustomer() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["customer_mobile"] = customerMobile
        requestData["user_id"] = token
        requestData["otp"] = binding.edtOtpLogin.text.toString()
        UtilMethods.verifyCustomerOtp(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: VerifyCustomerResponse = Gson().fromJson(message, VerifyCustomerResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Congratulations!",
                        "Customer Account Verified Successfully.You can make transaction now",
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })
                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "ERROR !",
                        response.message,
                        "error",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "ERROR !",
                    "Request Failed",
                    "error",
                    object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }
        })
    }

    private fun resendOtp() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["customer_mobile"] = customerMobile
        UtilMethods.resendCustomerOtp(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CustomerOtpResponse = Gson().fromJson(message, CustomerOtpResponse::class.java)
                if (!response.error) {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Request Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validateData(): Boolean {
        if (binding.edtCustomerName.text.toString().isEmpty()) {
            binding.edtCustomerName.error = "Enter Customer Name"
            return false
        } else if (!ActivityExtensions.isValidMobile(binding.edtCustomerNumber.text.toString())) {
            binding.edtCustomerNumber.error = "Enter a Valid Mobile"
            return false
        } else if (binding.edtCustomerDob.text.toString().isEmpty()) {
            binding.edtCustomerDob.error = "Select Date of Birth"
            return false
        } else if (binding.edtCustomerArea.text.toString().isEmpty()) {
            binding.edtCustomerArea.error = "Enter Area"
            return false
        } else if (binding.edtCustomerCity.text.toString().isEmpty()) {
            binding.edtCustomerCity.error = "Enter City"
            return false
        } else if (binding.edtCustomerState.text.toString().isEmpty()) {
            binding.edtCustomerState.error = "Enter State Name"
            return false
        } else if (binding.edtCustomerPincode.text.toString().length < 6) {
            binding.edtCustomerPincode.error = "Enter a Valid PinCode"
            return false
        } else {
            if (apiID == "66bca8ca5727c7563ad6e316") {
                if (binding.edtOtpPs.text.toString().isEmpty()) {
                    binding.edtOtpPs.error = "Enter Otp Received On Your Mobile"
                    return false
                } else {
                    return true
                }
            } else {
                return true
            }

        }
    }

    override fun setData() {
        val dob = getDate18YearsOlder()
        binding.edtCustomerDob.setText(dob)
    }

    private fun getDate18YearsOlder(): String {

        val today = LocalDate.now()

        val eighteenYearsAgo = today.minusYears(18)

        return eighteenYearsAgo.toString()

    }

}