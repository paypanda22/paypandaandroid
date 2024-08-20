package app.pay.panda.fragments.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentForgetPasswordBinding
import app.pay.panda.fragments.PrivacyPolicy
import app.pay.panda.fragments.TermsAndConditions
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.chkmobile.CheckMobileResponse
import app.pay.panda.responsemodels.generateotp.GenerateOtpResponse
import app.pay.panda.responsemodels.newForgetPassword.ForgetPasswordNewResponse
import app.pay.panda.retrofit.ApiMethods
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>(FragmentForgetPasswordBinding::inflate) {
    private lateinit var myActivity:FragmentActivity
    private var loginType="phone"
    private var refID=""
    private val progressBar = CustomProgressBar()

    override fun init() {
        nullActivityCheck()
    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.tv2.setOnClickListener {
            val tncFragment= TermsAndConditions()
            tncFragment.show(myActivity.supportFragmentManager,"TAG")
        }
        binding.tv4.setOnClickListener {
            val policy= PrivacyPolicy()
            policy.show(myActivity.supportFragmentManager,"TAG")
        }

        binding.rbLoginMethod.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId== R.id.rbMobile){
                loginType="phone"
                binding.lytMobile.visibility= View.VISIBLE
                binding.lytEmail.visibility= View.GONE
                binding.rbMobile.background= ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                binding.rbMobile.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.rbMobile.buttonTintList= ContextCompat.getColorStateList(myActivity, R.color.white)
                binding.rbEmail.background= ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound)
                binding.rbEmail.setTextColor(ContextCompat.getColor(myActivity, R.color.black))
                binding.rbEmail.buttonTintList= ContextCompat.getColorStateList(myActivity, R.color.black)
                binding.edtRegEmail.text?.clear()
            }else{
                loginType="email"
                binding.lytMobile.visibility= View.GONE
                binding.lytEmail.visibility= View.VISIBLE
                binding.rbEmail.background= ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                binding.rbEmail.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.rbEmail.buttonTintList= ContextCompat.getColorStateList(myActivity, R.color.white)
                binding.rbMobile.background= ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound)
                binding.rbMobile.setTextColor(ContextCompat.getColor(myActivity, R.color.black))
                binding.rbMobile.buttonTintList= ContextCompat.getColorStateList(myActivity, R.color.black)
                binding.edtRegMobile.text?.clear()
                binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, 0, 0)
            }
        }

        binding.edtRegMobile.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtRegMobile.text.toString().length == 10) {
                    if (ActivityExtensions.isValidMobile(binding.edtRegMobile.text.toString())) {
                        checkMobile()
                    } else {
                        binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, R.drawable.ic_cancel_red, 0)
                    }
                } else {
                    binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, 0, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.rlSubmit.setOnClickListener{
            if (binding.edtPassword.text.toString().isEmpty()){
                showToast(requireContext(),"Enter New Password First")
            } else if (!ActivityExtensions.isValidPassword(binding.edtPassword.text.toString())){
                showToast(requireContext(),"Invalid Password Provided")
            } else if (binding.edtConfirmPassword.text.toString().isEmpty()){
                showToast(requireContext(),"Enter Confirm Password")
            }else if (binding.edtPassword.text.toString() !=binding.edtConfirmPassword.text.toString() ){
                showToast(requireContext(),"Password Not Match")
            } else if (!ActivityExtensions.isValidPan(binding.edtPanNumber.text.toString())){
                binding.edtPanNumber.error="Enter a Valid pan Number"
                showToast(requireContext(),"Enter a Valid pan Number")
            } else{
                if (loginType=="phone"){
                    if (!ActivityExtensions.isValidMobile(binding.edtRegMobile.text.toString())){
                        binding.edtRegMobile.error="Enter a Valid Mobile"
                    }else{
                        progressBar.showProgress(requireContext())
                        openOtpDialog("phone","+91"+binding.edtRegMobile.text.toString())
                    }

                }else{
                    if (!ActivityExtensions.isValidEmail(binding.edtRegEmail.text.toString())){
                        binding.edtRegEmail.error="Enter a valid Email"
                    }else{
                        checkEmail()
                    }

                }
            }

        }
    }

    private fun checkEmail() {
        UtilMethods.chkEmail(requireContext(), binding.edtRegEmail.text.toString(), object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckMobileResponse = Gson().fromJson(message, CheckMobileResponse::class.java)
                if (response.data.isExist) {
                    openOtpDialog("email", binding.edtRegEmail.text.toString())
                } else {
                   showToast(requireContext(),response.message)
                }
            }

            override fun fail(from: String) {
                showToast(requireContext(),from)
            }
        })
    }

    private fun checkMobile() {
        UtilMethods.chkMobile(requireContext(), "+91" + binding.edtRegMobile.text.toString(), object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckMobileResponse = Gson().fromJson(message, CheckMobileResponse::class.java)
                if (response.data.isExist) {
                    binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, R.drawable.ic_check_green, 0)
                } else {
                    binding.edtRegMobile.text?.clear()
                    Toast.makeText(requireContext(),"Invalid Mobile Number Provided",Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })


    }
    private fun openOtpDialog(loginType: String, entity: String) {
        if (loginType == "phone") {
            val requestData = hashMapOf<String, Any?>()
            requestData["mobileNo"] = entity
            requestData["pan"] = binding.edtPanNumber.text.toString()
            ApiMethods.newForgetPasswordMobile(requireContext(), requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: ForgetPasswordNewResponse = Gson().fromJson(message, ForgetPasswordNewResponse::class.java)
                    if(!response.error){
                        if (response.data.user.isNotEmpty()) {
                            refID = response.data.user
                            val bundle = Bundle()
                            bundle.putString("entity", binding.edtRegMobile.text.toString())
                            bundle.putString("entityType", loginType)
                            bundle.putString("type", "forget_password")
                            bundle.putString("password", binding.edtPassword.text.toString())
                            bundle.putString("refID", refID)
                            findNavController().navigate(R.id.action_forgetPasswordFragment_to_verifyOtpFragment, bundle)
                        } else {
                            showToast(requireContext(), "Unable to Send Otp to your mobile")
                        }
                    }else{
                        showToast(requireContext(), "Unable to Send Otp to your mobile")
                    }

                }

                override fun fail(from: String) {
                    Toast.makeText(requireContext(), "Error in sending Otp to your mobile", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val requestData = hashMapOf<String, Any?>()
            requestData["emailId"] = entity
            requestData["pan"] = binding.edtPanNumber.text.toString()
            ApiMethods.newForgetPasswordEmail(requireContext(), requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: ForgetPasswordNewResponse = Gson().fromJson(message, ForgetPasswordNewResponse::class.java)
                    if (!response.error){
                        if (response.data.user.isNotEmpty()) {
                            refID = response.data.user
                            val bundle = Bundle()
                            bundle.putString("entity", binding.edtRegEmail.text.toString())
                            bundle.putString("entityType", loginType)
                            bundle.putString("type", "forget_password")
                            bundle.putString("password", binding.edtPassword.text.toString())
                            bundle.putString("refID", refID)
                            findNavController().navigate(R.id.action_forgetPasswordFragment_to_verifyOtpFragment, bundle)
                        } else {
                            Toast.makeText(requireContext(), "Unable to Send Otp to your email", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(), "Unable to Send Otp to your email", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun fail(from: String) {
                    Toast.makeText(requireContext(), "Error in sending Otp to your email", Toast.LENGTH_SHORT).show()
                }
            })

        }

    }

    override fun setData() {

    }
    fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}