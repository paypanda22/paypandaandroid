package app.pay.panda.fragments.login

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentLoginBinding
import app.pay.panda.databinding.LytLoginDialogBinding
import app.pay.panda.fragments.PrivacyPolicy
import app.pay.panda.fragments.TermsAndConditions
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.GetKeyHash
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.MyClick2
import app.pay.panda.responsemodels.chkmobile.CheckMobileResponse
import app.pay.panda.responsemodels.generateotp.GenerateOtpResponse
import app.pay.panda.responsemodels.newlogin.NewLoginResponse
import app.pay.panda.responsemodels.passwordcheck.PasswordCheckResponse
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    private var loginMethod = "phone"
    private var refID = ""
    private val progressBar = CustomProgressBar()

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(myActivity)
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
            val tncFragment = TermsAndConditions()
            tncFragment.show(myActivity.supportFragmentManager, "TAG")
        }
        binding.tv4.setOnClickListener {
            val policyfgmt=PrivacyPolicy()
            policyfgmt.show(myActivity.supportFragmentManager,"TAG")

        }



        binding.edtMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtMobile.text.toString().length == 10) {
                    if (ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())) {
                        checkMobile()
                    } else {
                        binding.edtMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, R.drawable.ic_cancel_red, 0)
                    }
                } else {
                    binding.edtMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, 0, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.tvForget.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
        binding.btnViaMobile.setOnClickListener {
            loginMethod = "phone"
            binding.lytMobile.visibility = View.VISIBLE
            binding.lytEmail.visibility = View.GONE
            binding.btnViaMobile.setTextColor(resources.getColor(R.color.white))
            binding.btnViaEmail.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            binding.btnViaMobile.background=ContextCompat.getDrawable(myActivity,R.drawable.submitt_btnsmall_dark)
            binding.btnViaEmail.background=ContextCompat.getDrawable(myActivity,R.drawable.submitt_btnsmall_white)
            binding.edtEmail.text?.clear()
            binding.edtPassword.text?.clear()
        }

        binding.btnViaEmail.setOnClickListener {
            loginMethod = "email"
            binding.lytMobile.visibility = View.GONE
            binding.lytEmail.visibility = View.VISIBLE
            binding.btnViaMobile.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            binding.btnViaEmail.setTextColor(resources.getColor(R.color.white))
            binding.btnViaMobile.background=ContextCompat.getDrawable(myActivity,R.drawable.submitt_btnsmall_white)
            binding.btnViaEmail.background=ContextCompat.getDrawable(myActivity,R.drawable.submitt_btnsmall_dark)
            binding.edtEmail.text?.clear()
            binding.edtPassword.text?.clear()
        }

        binding.btnSubmit.setOnClickListener {
            if (loginMethod == "phone") {
                if (!ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())) {
                    binding.edtMobile.error = "Enter a Valid Mobile"
                    Toast.makeText(requireContext(), "Enter a Valid Mobile", Toast.LENGTH_SHORT).show()
                } else if (binding.edtPassword.text.toString().isEmpty()) {
                    binding.edtPassword.error = "Enter Password"
                    binding.edtPassword.setPaddingRelative(0, 0, 48.dpToPx(), 0)
                }else if(binding.tv1.isChecked == false) {
                    Toast.makeText(requireContext(), "Please Apply Terms and Conditions", Toast.LENGTH_SHORT).show()
                }else {
                    checkPasswordMobile("phone", "+91" + binding.edtMobile.text.toString())
                }
            } else {
                if (!ActivityExtensions.isValidEmail(binding.edtEmail.text.toString())) {
                    binding.edtEmail.error = "Enter a Valid Email Address"
                    Toast.makeText(requireContext(), "Enter a Valid Email Address", Toast.LENGTH_SHORT).show()
                } else if (binding.edtPassword.text.toString().isEmpty()) {
                    binding.edtPassword.error = "Enter Password"
                    binding.edtPassword.setPaddingRelative(0, 0, 48.dpToPx(), 0)
                } else {
                    checkEmail(binding.edtEmail.text.toString())

                }
            }
        }


    }

    private fun checkPasswordMobile(loginType: String, entity: String) {
        val requestData = hashMapOf<String, Any?>()
        requestData["entity"] = entity
        requestData["password"] = binding.edtPassword.text.toString()
        requestData["deviceId"] = CommonClass.getDeviceId(myActivity)
        val getHash = GetKeyHash(myActivity)
        val finalData = getHash.getHash(requestData)
        UtilMethods.passwordCheck(requireContext(), finalData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PasswordCheckResponse = Gson().fromJson(message, PasswordCheckResponse::class.java)
                if (!response.error) {
                    openOtpDialog("phone", entity);

                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkPasswordEmail(loginType: String, entity: String) {
        val requestData = hashMapOf<String, Any?>()
        requestData["entity"] = entity
        requestData["password"] = binding.edtPassword.text.toString()
        requestData["deviceId"] = CommonClass.getDeviceId(myActivity)
        val getHash = GetKeyHash(myActivity)
        val finalData = getHash.getHash(requestData)
        UtilMethods.passwordCheck(requireContext(), finalData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PasswordCheckResponse = Gson().fromJson(message, PasswordCheckResponse::class.java)
                if (!response.error) {
                    openOtpDialog("email", entity)
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openOtpDialog(loginType: String, entity: String) {
        val requestData = hashMapOf<String, Any?>()
        requestData["entity"] = entity
        requestData["password"] = binding.edtPassword.text.toString()
        requestData["deviceId"] = CommonClass.getDeviceId(requireActivity())

        val getHash = GetKeyHash(myActivity)
        val finalData = getHash.getHash(requestData)
        UtilMethods.userLogin(requireContext(), finalData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: NewLoginResponse = Gson().fromJson(message, NewLoginResponse::class.java)
                if (response.error==false){
                    if (response.data?.user?.isNotEmpty()==true) {
                        refID = response.data.user.toString()
                        val bundle = Bundle()
                        bundle.putString("entity", binding.edtMobile.text.toString())
                        bundle.putString("entityType", loginType)
                        bundle.putString("type", "login")
                        bundle.putString("password", binding.edtPassword.text.toString())
                        bundle.putString("refID", refID)
                        findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment, bundle)
                    } else {
                        val bundle = Bundle()
                        findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment, bundle)
                        Toast.makeText(requireContext(), "Unable to Send Otp to your mobile", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    val bundle = Bundle()
                    findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment, bundle)

                    showToast(requireContext(),response.message)
                }

            }

            override fun fail(from: String) {
                val bundle = Bundle()
                findNavController().navigate(R.id.action_loginFragment_to_verifyOtpFragment, bundle)

                showToast(requireContext(),from)
            }
        })

    }

    private fun checkEmail(emailText: String) {
        progressBar.showProgress(requireContext())
        UtilMethods.chkEmail(requireContext(), emailText, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckMobileResponse = Gson().fromJson(message, CheckMobileResponse::class.java)
                if (response.data.isExist) {
                    checkPasswordEmail("email", emailText)
                } else {
                    progressBar.hideProgress()
                    ShowDialog.bottomDialogTwoButton(
                        requireActivity(),
                        "Email is not Registered with Us",
                        "Do you want to create a new account with us ?",
                        "error",
                        object : MyClick {
                            override fun onClick() {
                                binding.edtMobile.text?.clear()
                                binding.edtEmail.text?.clear()
                                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                            }
                        }, object : MyClick2 {
                            override fun onCancel() {

                            }
                        })
                }
            }

            override fun fail(from: String) {
                progressBar.hideProgress()
                Toast.makeText(requireContext(), "Unable to verify Email Address", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun checkMobile() {
        UtilMethods.chkMobile(requireContext(), "+91" + binding.edtMobile.text.toString(), object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckMobileResponse = Gson().fromJson(message, CheckMobileResponse::class.java)
                if (response.data.isExist) {
                    binding.edtMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_base, 0, R.drawable.ic_check_green, 0)
                } else {
                    ShowDialog.bottomDialogTwoButton(
                        requireActivity(),
                        "Mobile Not Registered with Us !",
                        "Do you want to create a new account with Us ?",
                        "error",
                        object : MyClick {
                            override fun onClick() {
                                val mobile = binding.edtMobile.text.toString()
                                binding.edtMobile.text?.clear()
                                binding.edtEmail.text?.clear()
                                val bundle = Bundle()
                                bundle.putString("mobile", mobile)
                                findNavController().navigate(R.id.action_loginFragment_to_registerFragment, bundle)
                            }
                        }, object : MyClick2 {
                            override fun onCancel() {

                            }
                        })
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun setData() {

    }

    fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
       binding.edtPassword.error=null

    }

    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
}