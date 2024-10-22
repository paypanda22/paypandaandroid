package app.pay.pandapro.fragments.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.DashBoardActivity
import app.pay.pandapro.activity.OnBoardingActivity
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.databinding.FragmentVerifyOtpBinding
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.GetKeyHash
import app.pay.pandapro.helperclasses.ShowDialog
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.interfaces.OnBackPressedListner
import app.pay.pandapro.responsemodels.forgetpassword.ForgetPasswordResponse
import app.pay.pandapro.responsemodels.login.LoginResponse
import app.pay.pandapro.responsemodels.newForgetPasswordVerify.ForgetPasswordVerifyResponse
import app.pay.pandapro.responsemodels.newlogin.NewLoginResponse
import app.pay.pandapro.retrofit.ApiMethods
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson

class VerifyOtpFragment : BaseFragment<FragmentVerifyOtpBinding>(FragmentVerifyOtpBinding::inflate), OnBackPressedListner {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    private lateinit var entityType: String
    private lateinit var type: String
    private lateinit var password: String
    private lateinit var Cpassword: String
    private lateinit var countDownTimer: CountDownTimer
    var timeLeftInMillis: Long = 60000L
    var isTimerRunning: Boolean = false
    private var entity = ""
    private var refID = ""
    private var regEmail = ""
    private var user_type_id = ""
    private var name = ""
    private var refer_id = ""
    private var userId = ""
    private var mobileToken=""
    private var mobileVerified = "0"
    @SuppressLint("SetTextI18n")
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())

        entity = arguments?.getString("entity").toString()
        entityType = arguments?.getString("entityType").toString()
        type = arguments?.getString("type").toString()
        password = arguments?.getString("password").toString()
        Cpassword = arguments?.getString("Cpassword").toString()
        refID = arguments?.getString("refID").toString()
        regEmail = arguments?.getString("regEmail") ?: ""
        user_type_id = arguments?.getString("user_type_id") ?: ""
        name = arguments?.getString("name") ?: ""
        refer_id = arguments?.getString("refer_id") ?: ""
        mobileVerified=arguments?.getString("mobileVerified") ?:""
        binding.tvNumber.text = if (entityType == "phone") "Sent to +91- $entity" else "Sent to - $entity"
        setTimer()


        val result1=Bundle()
        result1.putString("mobileVerified", "0")       // This sets the mobileVerified status to 1
        parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
    }


    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    fun setTimer() {
        binding.tvTimer.visibility = View.VISIBLE
        binding.tvResendOtp.setTextColor(
            ContextCompat.getColor(
                requireActivity(), R.color.bggrey_dark
            )
        )
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                binding.tvResendOtp.isEnabled = true
                binding.tvTimer.visibility = View.GONE
                binding.tvResendOtp.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.colorPrimaryDark
                    )
                )
                isTimerRunning = false
            }
        }
        countDownTimer.start()
        isTimerRunning = true
    }

    private fun cancelTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel()
        }

    }

    private fun updateTimer() {
        val minutes = timeLeftInMillis / 1000 / 60
        val seconds = timeLeftInMillis / 1000 % 60

        @SuppressLint("DefaultLocale") val timeLeftFormatted =
            String.format("%02d:%02d", minutes, seconds)
        binding.tvTimer.text = timeLeftFormatted
    }

    override fun addListeners() {
        binding.edtOtpLogin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtOtpLogin.text!!.length == 6) {
                    val imm: InputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.edtOtpLogin.windowToken, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.tvResendOtp.setOnClickListener {
            if (isTimerRunning) {
                Toast.makeText(activity, "You can Resend OTP After One Minute", Toast.LENGTH_SHORT)
                    .show()
            } else {
                timeLeftInMillis = 60000
                setTimer()
                if(type=="login"){
                    resendOTP("phone",entity)
                }else{
                    resendforgetOTP()
                }

            }
        }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnContinue.setOnClickListener {
            if (binding.edtOtpLogin.text.toString().length != 6) {
                Toast.makeText(requireContext(), "Enter a Valid OTP", Toast.LENGTH_SHORT).show()
            } else {
                cancelTimer()
                if (type=="login"){
                    login()
                }else{
                    if (entityType=="phone"){
                        verifyMobileOtp()
                    }else{
                       // verifyEmailOtp()
                    }
                }

            }

        }

    }

    private fun verifyEmailOtp() {
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = refID
        requestData["otp"] = binding.edtOtpLogin.text.toString()
        ApiMethods.forgetPasswordOtpEmail(myActivity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: ForgetPasswordVerifyResponse = Gson().fromJson(message, ForgetPasswordVerifyResponse::class.java)
                if (!response.error){
                    if (response.data.user.isNotEmpty()) {
                       // forgetPassword()
                    } else {
                        showToast(activity, response.message)
                    }
                }else{
                    showToast(activity, response.message)
                }
            }

            override fun fail(from: String) {
                showToast(activity, from)
            }
        })
    }


    private fun verifyMobileOtp() {
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = refID
        requestData["otp"] = binding.edtOtpLogin.text.toString()
        ApiMethods.forgetPasswordOtpMobile(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: ForgetPasswordVerifyResponse = Gson().fromJson(message, ForgetPasswordVerifyResponse::class.java)
                if (!response.error){
                    if (response.data.user.isNotEmpty()) {
                        mobileToken=response.data.user
                        mobileVerified="1"

                        cancelTimer()
                        val result = Bundle()
                        result.putString("mobileToken", mobileToken) // Make sure you're setting the right key

                        val result1= Bundle()
                        result1.putString("mobileVerified", "1")       // This sets the mobileVerified status to 1

                        parentFragmentManager.setFragmentResult("mobileTokenKey", result)
                        parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
                        findNavController().popBackStack()
                     // forgetPassword()
                    } else {
                        mobileVerified="0"
                        val result1=Bundle()
                        result1.putString("mobileVerified", "0")       // This sets the mobileVerified status to 1
                        parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
                        Toast.makeText(activity, "Unable to verify OTP", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    mobileVerified="0"
                    val result1=Bundle()
                    result1.putString("mobileVerified", "0")       // This sets the mobileVerified status to 1
                    parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
                    showToast(activity, response.message)
                }

            }

            override fun fail(from: String) {
                showToast(activity, from)
            }
        })
    }

    /*private fun forgetPassword() {
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = refID
        requestData["password"] = password
        requestData["repassword"] = Cpassword
        UtilMethods.forgetPassword(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: ForgetPasswordResponse = Gson().fromJson(message, ForgetPasswordResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(
                        myActivity,
                        "Password Changed Successfully",
                        "Proceed to Login With New Password",
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().navigate(R.id.action_verifyOtpFragment_to_loginFragment)
                            }
                        })

                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }*/

    private fun login() {
        if (entityType == "phone") entity = "+91$entity"
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = refID
        requestData["otp"] = binding.edtOtpLogin.text.toString()
        requestData["deviceId"] = CommonClass.getDeviceId(myActivity)
        val getHash = GetKeyHash(myActivity)
        val finalData = getHash.getHash(requestData)
        UtilMethods.newLoginOtp(requireContext(), finalData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: LoginResponse = Gson().fromJson(message, LoginResponse::class.java)
                if (response.error==false){
                    val createSession = response.data?.let { userSession.createUserSession(it) }
                    if (createSession == true) {
                        userSession.setData(Constant.USER_TYPE_NAME,response.data.user_type_name.toString())
                        userSession.setData(Constant.TPINSTATUS,response.data.Tpin_status.toString())
                        sendToMainScreen();
                    } else {
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    showToast(requireContext(),response.message)
                }

            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun sendToMainScreen() {
        if (userSession.getBoolData(Constant.ISAPPROVED)) {
            startActivity(Intent(myActivity, DashBoardActivity::class.java))
        } else {
            startActivity(Intent(myActivity, OnBoardingActivity::class.java))
        }
    }

    override fun setData() {

    }


    override fun backPressed() {
        if (isTimerRunning) {
            Toast.makeText(requireContext(), "Running", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Not Running", Toast.LENGTH_SHORT).show()
        }
    }


    private fun resendOTP(loginType: String, entity: String) {

        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = refID
        requestData["entity"] = entity
        requestData["password"] = password
        requestData["deviceId"] = CommonClass.getDeviceId(myActivity)

        val getHash = GetKeyHash(myActivity)
        val finalData = getHash.getHash(requestData)
        UtilMethods.resendOTP(requireContext(), finalData, refID,object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: NewLoginResponse =
                    Gson().fromJson(message, NewLoginResponse::class.java)
                if (response.error == false) {
                    Toast.makeText(
                        requireContext(),
                        "Otp send successfully to your mobile number",
                        Toast.LENGTH_SHORT
                    ).show()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Unable to Send Otp to your mobile",
                            Toast.LENGTH_SHORT
                        ).show()
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
            }
        })
    }

    private fun resendforgetOTP() {

        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = refID


        UtilMethods.resendforgetOTP(requireContext(), requestData, refID,object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: NewLoginResponse =
                    Gson().fromJson(message, NewLoginResponse::class.java)
                if (response.error == false) {
                    Toast.makeText(
                        requireContext(),
                        "Otp send successfully to your mobile number",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable to Send Otp to your mobile",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
            }
        })
    }

        override fun onPause() {
        super.onPause()
        if (isTimerRunning) {
            cancelTimer()
        }
    }

    override fun onResume() {
        super.onResume()
        countDownTimer.start()
    }


}