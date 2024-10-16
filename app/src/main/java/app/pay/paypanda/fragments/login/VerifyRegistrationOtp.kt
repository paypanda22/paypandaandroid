package app.pay.paypanda.fragments.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.databinding.FragmentVerifyRegistrationOtpBinding
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.helperclasses.Utils.Companion.showToast
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.responsemodels.registerOtp.RegisterOtpResponse
import app.pay.paypanda.responsemodels.resendtpin.TransactionPinResponse
import app.pay.paypanda.responsemodels.verifyOtp.VerifyOtpResponse
import app.pay.paypanda.responsemodels.verifyRegisterOtp.VerifyRegisterOtpResponse
import app.pay.paypanda.retrofit.ApiMethods
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson


class VerifyRegistrationOtp : BaseFragment<FragmentVerifyRegistrationOtpBinding>(FragmentVerifyRegistrationOtpBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private var email=""
    private var phone=""
    private var token=""
    private var emailToken=""
    private var mobileToken=""
    private var userTypeId=""
    private var name=""
    private var state=""
    private var password=""
    private var refID=""
    private var isReferral=""
    private var mobileVerified = "0"
    private lateinit var countDownTimer: CountDownTimer
    var timeLeftInMillis: Long = 60000L
    var isTimerRunning: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        email=arguments?.getString("email") ?:""
        phone=arguments?.getString("phone") ?:""
        token=arguments?.getString("token") ?:""
        userTypeId=arguments?.getString("userTypeId") ?:""
        password=arguments?.getString("password") ?:""
        name=arguments?.getString("name") ?:""
        refID=arguments?.getString("refID") ?:""
        isReferral=arguments?.getString("isReferral") ?:""
        state=arguments?.getString("state") ?:""
        mobileVerified=arguments?.getString("mobileVerified") ?:""

        binding.tvNumber.text="Sent to +91- $phone"
        binding.tvEmail.text="Sent to - $email"
        setTimer()
        val result1=Bundle()
        result1.putString("mobileVerified", "0")       // This sets the mobileVerified status to 1
        parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)

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
    private fun nullActivityCheck() {
        if (activity !=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.tvResendOtp.setOnClickListener {
            if (isTimerRunning) {
               showToast(requireContext(),"You can Resend OTP After One Minute")
            } else {
               // timeLeftInMillis = 60000
                   // resendOtp()
                    resendSignupOTP()


            }
        }

        binding.btnContinue.setOnClickListener{
            if (binding.edtOtpMobile.length()<6){
                showToast(requireContext(),"Enter OTP Received On Mobile")
            }/*else if (binding.edtOtpEmail.length()<=6){
                showToast(requireContext(),"Enter OTP Received On Email")
            }*/else{
             //   verifyOtp()
            validateMobileOtp()
         //  }
        }

    }
        }

    private fun verifyOtp() {
        val requestData= hashMapOf<String,Any?>()
        requestData["user_id"]=token
      requestData["email_otp"]=binding.edtOtpEmail.text.toString()
        requestData["mobile_otp"]=binding.edtOtpMobile.text.toString()

        UtilMethods.verifyRegisterOtp(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
               val response:VerifyRegisterOtpResponse=Gson().fromJson(message,VerifyRegisterOtpResponse::class.java)
                if (response.error==false){
                    emailToken=response.data?.email.toString()
                    mobileToken=response.data?.mobile.toString()
                    cancelTimer()
                   // register()
                }else{
                    showToast(requireContext(),response.message)
                }
            }

            override fun fail(from: String) {
                showToast(requireContext(),from)
            }
        })
    }

    private fun validateMobileOtp() {
        val requestData = hashMapOf<String, Any?>()
        requestData["otp"] = binding.edtOtpMobile.text.toString()
       requestData["user_id"] = token

        UtilMethods.verifyOtp(myActivity, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: VerifyOtpResponse = Gson().fromJson(message, VerifyOtpResponse::class.java)
                if (response.data.user.isNotEmpty()) {
//                    emailToken=response.data?.toString()
                  // mobileToken=response.data?.user.toString()
                      mobileToken=response.data.user.toString()
                    mobileVerified="1"

                  //  register()
                    cancelTimer()
                    val result = Bundle()
                    result.putString("mobileToken", mobileToken) // Make sure you're setting the right key

                    val result1=Bundle()
                    result1.putString("mobileVerified", "1")       // This sets the mobileVerified status to 1

                    parentFragmentManager.setFragmentResult("mobileTokenKey", result)
                    parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
                    findNavController().popBackStack()
                } else {
                    mobileVerified="0"
                    val result1=Bundle()
                    result1.putString("mobileVerified", "0")       // This sets the mobileVerified status to 1
                    parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
                    Toast.makeText(activity, "Unable to verify OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                val result1=Bundle()
                result1.putString("mobileVerified", "0")       // This sets the mobileVerified status to 1
                parentFragmentManager.setFragmentResult("mobileVerifiedKey", result1)
                Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
            }
        })

    }


    private fun resendOtp() {
        val requestData= hashMapOf<String,Any?>()
        requestData["emailId"]=email
        requestData["mobileNo"]=phone
      UtilMethods.getRegistrationOtp(requireContext(),requestData,object:MCallBackResponse{
          override fun success(from: String, message: String) {
              val response:RegisterOtpResponse=Gson().fromJson(message,RegisterOtpResponse::class.java)
              if(!response.error){
                  token= response.data.user.toString()
                  showToast(requireContext(),response.message)
                  setTimer()
              }else{
                  showToast(requireContext(),response.message)
              }
          }

          override fun fail(from: String) {
              showToast(requireContext(),from)
          }
      })
    }

    private fun resendSignupOTP() {
     //   val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        ApiMethods.resendSignupOTP(myActivity, token,requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: TransactionPinResponse = Gson().fromJson(
                    message,
                    TransactionPinResponse::class.java
                )
                if (response.error == false) {
                    showToast(myActivity, "Otp Sand Successfully ")
                } else {
                    showToast(myActivity, response.message)
                }
            }

            override fun fail(from: String) {
                // showToast(myActivity,from)
            }
        })
    }

    override fun setData() {

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

  /*  private fun register() {
        val requestData = hashMapOf<String, Any?>()
        requestData["email"] = email
        requestData["mobile"] = mobileToken
        requestData["password"] = password
        requestData["user_type_id"] = userTypeId
        requestData["name"] = name
        requestData["refer_id"] = refID
        requestData["state"] = state
        val getHash = GetKeyHash(myActivity)
        val finalData = getHash.getHash(requestData)
        UtilMethods.register(myActivity, finalData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: RegisterResponse = Gson().fromJson(message, RegisterResponse::class.java)
                if (response.error==false){
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Account Created Successfully",
                        "You account has been created successfully.Press OK to proceed to login screen",
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().navigate(R.id.action_verifyRegistrationOtp_to_loginFragment)
                            }
                        })
                }else{
                    response.message?.let {
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "Account Not Created",
                            it,
                            "error",
                            object : MyClick {
                                override fun onClick() {
                                    findNavController().popBackStack()
                                }
                            })
                    }
                }

            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "Account Not Created",
                    from,
                    "error",
                    object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }
        })
    }*/

}