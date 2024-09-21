package app.pay.panda.fragments.onboarding

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentAdhaarVerificationBinding

import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.ShowDialog

import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.aadhaarOtp.AadhaarOtpResponse
import app.pay.panda.responsemodels.adhaarverify.AadhaarverifyResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class AdhaarVerificationFragment : BaseFragment<FragmentAdhaarVerificationBinding>(FragmentAdhaarVerificationBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private var refID=""
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
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
        binding.edtAdhaarNumber.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtAdhaarNumber.text.toString().length == 12) {
                    if (ActivityExtensions.isValidAadhaar(binding.edtAdhaarNumber.text.toString())) {
                        binding.edtAdhaarNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green, 0)
                    }
                }else{
                    binding.edtAdhaarNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.btnAadhaar.setOnClickListener {
            if (binding.edtAdhaarNumber.text.toString().length!=12){
                binding.edtAdhaarNumber.error="Enter a Valid Aadhaar Number"
            }else{
                if (!ActivityExtensions.isValidAadhaar(binding.edtAdhaarNumber.text.toString()))
                {
                    binding.edtAdhaarNumber.error="Enter a Valid Aadhaar Number"
                }else{
                    validateAadhaar()
                }

            }
        }

        binding.btnOtp.setOnClickListener {
            if (binding.edtOtpLogin.text.toString().length<6){
                Toast.makeText(requireContext(),"Enter a Valid Otp",Toast.LENGTH_SHORT).show()
            }else{
                validateAadhaarOtp()
            }
        }
    }

    override fun setData() {

    }

    private fun validateAadhaarOtp() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"]=token
        requestData["ref_id"]=refID
        requestData["otp"]=binding.edtOtpLogin.text.toString()
        UtilMethods.aadhaarVerifyOtp(requireContext(),requestData,object: MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AadhaarverifyResponse = Gson().fromJson(message,AadhaarverifyResponse::class.java)
                userSession.setBoolData(Constant.ADHAAR_VERIFIED,true)
                userSession.setBoolData(Constant.IDENTITY_VERIFIED,true)
                userSession.setIntData(Constant.LOGIN_STEPS,1)
                binding.llOtp.visibility= GONE
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "Aadhaar Verified Successfully",
                    "Identity Verification Completed Successfully. Proceed to Next Step","success",object: MyClick {
                        override fun onClick() {
                            findNavController().navigate(R.id.action_global_onboardingStatusFragment)
                        }
                    })


            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(),from,Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun validateAadhaar() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"]=token
        requestData["adhaarNumber"]=binding.edtAdhaarNumber.text.toString()
        UtilMethods.aadhaarSendOTP(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: AadhaarOtpResponse =Gson().fromJson(message,AadhaarOtpResponse::class.java)
                binding.btnAadhaar.visibility=View.GONE
                binding.llOtp.visibility=View.VISIBLE
                refID=response.data?.ref_id.toString()
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(),from,Toast.LENGTH_SHORT).show()
            }
        })

    }

}