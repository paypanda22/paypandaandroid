package app.pay.retailers.fragments.onboarding

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.databinding.FragmentBusinessDetailsBinding
import app.pay.retailers.helperclasses.ActivityExtensions
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.responsemodels.gstupdate.GstUpdateResponse
import app.pay.retailers.responsemodels.gstverify.GstVerificationResponse
import app.pay.retailers.responsemodels.pincode.PinCodeResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson

class BusinessDetailsFragment : BaseFragment<FragmentBusinessDetailsBinding>(FragmentBusinessDetailsBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        binding.edtAddress.isEnabled= false

    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.rgGst.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbYes) {
                binding.llGstDetails.visibility = View.VISIBLE
                binding.edtAddress.isEnabled= false
                binding.edtPincode.isEnabled=false
                binding.edtAddress.text?.clear()
                binding.edtPincode.text?.clear()
                binding.edtState.text?.clear()
                binding.edtCity.text?.clear()
                binding.edtArea.text?.clear()
            } else {
                binding.edtAddress.isEnabled= true
                binding.edtPincode.isEnabled=true
                binding.edtGstNumber.text?.clear()
                binding.edtBusinessName.text?.clear()
                binding.edtTypeOfRegistration.text?.clear()
                binding.edtAddress.text?.clear()
                binding.edtPincode.text?.clear()
                binding.edtState.text?.clear()
                binding.edtCity.text?.clear()
                binding.edtArea.text?.clear()
                binding.llGstDetails.visibility = View.GONE
            }
        }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.edtPincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtPincode.text.toString().length == 6) {
                    getPinCodeDetails(binding.edtPincode.text.toString())
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.edtGstNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtGstNumber.text.toString().length == 15) {
                    if (ActivityExtensions.isValidGst(binding.edtGstNumber.text.toString())) {
                        validateGst()
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.btnSubmit.setOnClickListener {
            if (validateData()) {
                val token = userSession.getData(Constant.USER_TOKEN).toString()
                val requestData = hashMapOf<String, Any>()
                requestData["isGstAvailable"] = binding.rbYes.isChecked
                requestData["GSTIN"] = if (binding.rbYes.isChecked) binding.edtGstNumber.text.toString() else ""
                requestData["businessName"] = if (binding.rbYes.isChecked) binding.edtBusinessName.text.toString() else ""
                requestData["business_Address"] = binding.edtAddress.text.toString()
                requestData["business_city"] = binding.edtCity.text.toString()
                requestData["business_pincode"] = binding.edtPincode.text.toString()
                requestData["pincode"] = binding.edtPincode.text.toString()
                requestData["user_id"] = token
                requestData["business_state"] = binding.edtState.text.toString()
                requestData["shop_name"] = binding.edtShopName.text.toString()
                requestData["business_Area"] = binding.edtArea.text.toString()
                UtilMethods.updateGstDetails(requireContext(), requestData, object : MCallBackResponse {
                    override fun success(from: String, message: String) {
                        val response: GstUpdateResponse = Gson().fromJson(message, GstUpdateResponse::class.java)
                        userSession.setBoolData(Constant.GST_AVAILABLE, binding.rbYes.isChecked)
                        userSession.setBoolData(Constant.IS_GST, true)
                        userSession.setIntData(Constant.LOGIN_STEPS, 3)
                        ShowDialog.bottomDialogSingleButton(
                            myActivity,
                            "Business Details Updated",
                            "Business Details has been Submitted. Proceed for Next Step",
                            "success",
                            object : MyClick {
                                override fun onClick() {
                                    findNavController().popBackStack()
                                }
                            })

                    }

                    override fun fail(from: String) {
                        ShowDialog.bottomDialogSingleButton(myActivity, "Error In Updating GST Details", from, "error", object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })
                    }
                })

            }

        }

    }

    private fun getPinCodeDetails(pincode: String) {
        UtilMethods.getPinCodeData(requireContext(), pincode, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PinCodeResponse = Gson().fromJson(message, PinCodeResponse::class.java)
                binding.edtCity.setText(response.data.city)
                binding.edtState.setText(response.data.state)
                binding.edtArea.setText(response.data.sub_distance)
            }

            override fun fail(from: String) {

            }
        })
    }

    private fun validateData(): Boolean {
        if (binding.edtAddress.text.toString().isEmpty()) {
            binding.edtAddress.error = "Enter Your Business Address"
            return false
        } else if (binding.edtCity.text.toString().isEmpty()) {
            binding.edtCity.error = "Enter City Name"
            return false
        } else if (binding.edtPincode.text.toString().isEmpty()) {
            binding.edtPincode.error = "Enter a valid Pincode"
            return false
        } else if (binding.edtShopName.text.toString().isEmpty()) {
            binding.edtShopName.error = "Enter Your Shop Name"
            return false
        } else {
            if (binding.rbYes.isChecked) {
                if (!ActivityExtensions.isValidGst(binding.edtGstNumber.text.toString())) {
                    binding.edtGstNumber.error = "Enter a Valid GST Number"
                    return false
                } else if (binding.edtBusinessName.text.toString().isEmpty()) {
                    binding.edtBusinessName.error = "Enter a Valid Business Name"
                    return false
                } else if (binding.edtTypeOfRegistration.text.toString().isEmpty()) {
                    binding.edtTypeOfRegistration.error = "Enter Your GST Registration Type"
                    return false
                } else {
                    return true
                }

            } else {
                return true
            }
        }


    }

    private fun validateGst() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any>()
        requestData["GSTIN"] = binding.edtGstNumber.text.toString()
        requestData["businessName"] = "New Business"
        requestData["user_id"] = token
        UtilMethods.verifyGst(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: GstVerificationResponse = Gson().fromJson(message, GstVerificationResponse::class.java)
                val address = response.data.principal_place_address
                val parts: Array<String> = address.split(", ").toTypedArray()
                val length = parts.size
                if (length > 0) {
                    binding.edtPincode.setText(parts[length - 1])
                }
                binding.edtBusinessName.setText(response.data.legal_name_of_business.toString())
                binding.edtTypeOfRegistration.setText(response.data.taxpayer_type.toString())
                binding.edtAddress.setText(response.data.principal_place_address.toString())
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun setData() {

    }


}