package app.pay.panda.fragments.login

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.StateListAdapter
import app.pay.panda.databinding.FragmentRegisterBinding
import app.pay.panda.databinding.OtpDialogBinding
import app.pay.panda.dialog.DialogOK
import app.pay.panda.fragments.PrivacyPolicy
import app.pay.panda.fragments.TermsAndConditions
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.StateListClickListner
import app.pay.panda.responsemodels.CheckSponsorCode.CheckSponsorResponse
import app.pay.panda.responsemodels.chkmobile.CheckMobileResponse
import app.pay.panda.responsemodels.registerOtp.RegisterOtpResponse
import app.pay.panda.responsemodels.reqestreg.ReqRegistrationResponse
import app.pay.panda.responsemodels.statelist.StateListResponse
import app.pay.panda.responsemodels.usertype.Data
import app.pay.panda.responsemodels.usertype.UserTypeResponse
import app.pay.panda.retrofit.ApiMethods
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var bottomSheetDialog: Dialog
    private lateinit var dBinding: OtpDialogBinding
    private var refID = ""
    private var userId = ""
    private var selectedUserTypeId: String? = null

    private var mobileVerified = false
    private var sponsorverified = false
    private var mobile = ""
    private var isReferral = 1
    private var stateID = ""
    private lateinit var userTypelist: MutableList<Data>
    private val progressBar = CustomProgressBar()
    private var stateList = ArrayList<app.pay.panda.responsemodels.statelist.Data>()

    override fun init() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        nullActivityCheck()

        userSession = UserSession(requireContext())
        mobile = arguments?.getString("mobile") ?: ""
        if (mobile.isNotEmpty()) {
            binding.edtRegMobile.setText(mobile)
        }
        getUseType()

    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {

        binding.edtState.setOnClickListener {
            UtilMethods.getStateList(requireContext(), "101", object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: StateListResponse =
                        Gson().fromJson(message, StateListResponse::class.java)
                    if (stateList.isNotEmpty()) {
                        stateList.clear()
                    }
                    stateList.addAll(response.data)
                    openStateListDialog()

                }

                override fun fail(from: String) {
                    Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.chkNoreferal.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.chkNoreferal.isChecked) {
                if (!ActivityExtensions.isValidMobile(binding.edtRegMobile.text.toString())) {
                    Toast.makeText(
                        requireContext(),
                        "Provide a Valid Mobile",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.chkNoreferal.isChecked = false
                } else if (!ActivityExtensions.isValidEmail(binding.edtEmail.text.toString())) {
                    binding.edtEmail.error = "Invalid Email Address Provided"
                    binding.chkNoreferal.isChecked = false
                } else if (!ActivityExtensions.isValidName(binding.edtName.text.toString())) {
                    binding.edtName.error = "Enter Name"
                    binding.chkNoreferal.isChecked = false
                } else if (stateID.isEmpty()) {
                    showToast(requireContext(), "Select State First")
                    binding.chkNoreferal.isChecked = false
                } else {
                    onBoardingRequest()
                    binding.chkNoreferal.isChecked = false
                }
            }


            isReferral = 0
            binding.edtSponsorMobile.isEnabled = false
            binding.edtSponsorMobile.background =
                ContextCompat.getDrawable(myActivity, R.drawable.submitt_btn_dialog_light_grey)

        }
        /*}else{
                isReferral=1
                binding.edtSponsorMobile.isEnabled=true
                binding.edtSponsorMobile.background=null
            }*/
        binding.tv2.setOnClickListener {
            val tncFragment = TermsAndConditions()
            tncFragment.show(myActivity.supportFragmentManager, "TAG")
        }
        binding.tv4.setOnClickListener {
            val policy= PrivacyPolicy()
            policy.show(myActivity.supportFragmentManager,"TAG")
        }
        binding.edtRegMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtRegMobile.text.toString().length == 10) {
                    if (ActivityExtensions.isValidMobile(binding.edtRegMobile.text.toString())) {
                        checkMobile("+91" + binding.edtRegMobile.text.toString())
                    } else {
                        binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_phone_base,
                            0,
                            R.drawable.ic_cancel_red,
                            0
                        )
                    }
                } else {
                    binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_phone_base,
                        0,
                        0,
                        0
                    )
                    mobileVerified = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        /*   binding.edtSponsorMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtSponsorMobile.text.toString().length == 10) {
                    checkSponsorCode(binding.edtSponsorMobile.text.toString())
                } else {
                    sponsorverified = false
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
*/
        binding.rlSubmit.setOnClickListener {
            if (!ActivityExtensions.isValidMobile(binding.edtRegMobile.text.toString())) {
                Toast.makeText(requireContext(), "Provide a Valid Mobile", Toast.LENGTH_SHORT)
                    .show()
            } else if (!ActivityExtensions.isValidEmail(binding.edtEmail.text.toString())) {
                binding.edtEmail.error = "Invalid Email Address Provided"
            } else if (!ActivityExtensions.isValidName(binding.edtName.text.toString())) {
                binding.edtName.error = "Enter Name"
            } else if (stateID.isEmpty()) {
                showToast(requireContext(), "Select State First")
            } else {
                if (binding.chkNoreferal.isChecked) {
                    reqRegister()
                } else {
                    /*  if (!sponsorverified) {
                        binding.edtSponsorMobile.error = "Enter a Valid Sponsor Code"
                    } else {*/
                    sendOtp(binding.edtRegMobile.text.toString())
                    //  }
                }

            }

        }
        // Inside your onItemClickListener
        binding.rbLoginMethod.setOnItemClickListener { parent, view, position, id ->
            selectedUserTypeId = userTypelist[position]._id
            val selectedUserType = parent.getItemAtPosition(position) as String
            updateVisibilityBasedOnUserType(selectedUserType)
        }

    }
    private fun updateVisibilityBasedOnUserType(selectedUserType: String) {
        val isSponsorMobileVisible: Boolean
        val isReferalVisible: Boolean

        if (selectedUserType == "Super Distributor" || selectedUserType == "whiteLabel") {
            binding.edtSponsorMobile.visibility = View.GONE
            binding.edtSponsorMobileLayout.visibility = View.GONE
            binding.referal.visibility = View.GONE
            isSponsorMobileVisible = false
            isReferalVisible = false
        } else {
            binding.edtSponsorMobile.visibility = View.VISIBLE
            binding.edtSponsorMobileLayout.visibility = View.VISIBLE
            binding.referal.visibility = View.VISIBLE
            isSponsorMobileVisible = true
            isReferalVisible = true
        }

        // Save visibility states in SharedPreferences
        userSession.setBoolData(Constant.SPONSOR_MOBILE_VISIBLE, isSponsorMobileVisible)
        userSession.setBoolData(Constant.REFERAL_VISIBLE, isReferalVisible)
        userSession.setData(Constant.USERTYPE, selectedUserType)
    }



    // Call this function in onResume
    override fun onResume() {
        super.onResume()

        // Retrieve the last selected user type from SharedPreferences
        val lastUserType = userSession.getData(Constant.USERTYPE)
        updateVisibilityBasedOnUserType(lastUserType.toString())
    }



    private fun openStateListDialog() {
        val dialogView = layoutInflater.inflate(R.layout.lyt_rv_name_search_item_list, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
        }
        val edtName: TextInputEditText = dialogView.findViewById(R.id.edtName)
        val rvList: RecyclerView = dialogView.findViewById(R.id.rvList)
        edtName.setHint(R.string.enter_state_name_to_search)

        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        val clickListner = object : StateListClickListner {
            override fun onItemClicked(
                holder: RecyclerView.ViewHolder,
                model: List<app.pay.panda.responsemodels.statelist.Data>,
                pos: Int
            ) {
                binding.edtState.setText(model[pos].name)
                stateID = model[pos]._id
                alertDialog.dismiss()
            }

        }
        val stateListAdapter = StateListAdapter(myActivity, stateList, clickListner)
        rvList.adapter = stateListAdapter
        rvList.layoutManager = LinearLayoutManager(myActivity)
        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                stateListAdapter.filter.filter(edtName.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


    }

    private fun reqRegister() {
        val requestData = hashMapOf<String, Any?>()
        requestData["name"] = binding.edtName.text.toString()
        requestData["email"] = binding.edtEmail.text.toString()
        requestData["mobileNo"] = binding.edtRegMobile.text.toString()
        requestData["latitude"] = userSession.getData(Constant.M_LAT)
        requestData["longitude"] = userSession.getData(Constant.M_LONG)
        ApiMethods.registrationRequest(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: ReqRegistrationResponse =
                    Gson().fromJson(message, ReqRegistrationResponse::class.java)
                if (response.error == false) {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Account Created Successfully",
                        "You account has been created successfully.Press OK to proceed to login screen",
                        "success",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().navigate(R.id.action_verifyRegistrationOtp_to_loginFragment)
                            }
                        })
                } else {
                    response.message?.let {
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "Account Not Created",
                            it,
                            "error",
                            object : MyClick {
                                override fun onClick() {
                                    findNavController().navigate(R.id.action_verifyRegistrationOtp_to_loginFragment)
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
                            findNavController().navigate(R.id.action_verifyRegistrationOtp_to_loginFragment)
                        }
                    })
            }
        })
    }

    private fun checkSponsorCode(sCode: String) {
        UtilMethods.checkSponsorCode(requireContext(), sCode, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckSponsorResponse =
                    Gson().fromJson(message, CheckSponsorResponse::class.java)
                sponsorverified = true
                binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_phone_base,
                    0,
                    R.drawable.ic_check_green,
                    0
                )
                binding.tvSponsorName.text = response.data?.user?.name.toString()
                binding.tvSponsorName.visibility = View.VISIBLE
            }

            override fun fail(from: String) {
                sponsorverified = false
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun checkMobile(mobile: String) {
        UtilMethods.chkMobile(requireContext(), mobile, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CheckMobileResponse =
                    Gson().fromJson(message, CheckMobileResponse::class.java)
                if (response.data.isExist) {
                    ShowDialog.bottomDialogSingleButton(myActivity,
                        "Mobile is Already Registered",
                        "This mobile number is already registered with us. Kindly Login to your account",
                        "error", object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })
                } else {
                    binding.edtRegMobile.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_phone_base,
                        0,
                        R.drawable.ic_check_green,
                        0
                    )
                }
            }

            override fun fail(from: String) {
                Toast.makeText(
                    requireContext(),
                    "Unable to Check Mobile Number Status",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()

            }
        })


    }

    private fun sendOtp(mobile: String) {
        progressBar.showProgress(requireContext())
        val requestData = hashMapOf<String, Any?>()
        requestData["emailId"] = binding.edtEmail.text.toString()
        requestData["mobileNo"] = "+91$mobile"
        UtilMethods.getRegistrationOtp(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: RegisterOtpResponse =
                    Gson().fromJson(message, RegisterOtpResponse::class.java)
                if (!response.error) {
                    if (response.data.user?.isNotEmpty() == true) {
                        progressBar.hideProgress()
                        val token = response.data.user.toString()
                        val bundle = Bundle()
                        bundle.putString("phone", binding.edtRegMobile.text.toString())
                        bundle.putString("email", binding.edtEmail.text.toString())
                        bundle.putString("password", binding.edtRegMobile.text.toString())
                        bundle.putString("token", token)
                        bundle.putString("isReferral", isReferral.toString())
                        bundle.putString("userTypeId", selectedUserTypeId)
                        bundle.putString("name", binding.edtName.text.toString())
                        bundle.putString("refID", binding.edtSponsorMobile.text.toString())
                        findNavController().navigate(
                            R.id.action_registerFragment_to_verifyRegistrationOtp,
                            bundle
                        )
                    } else {
                        progressBar.hideProgress()
                        showToast(requireContext(), response.message)
                    }
                } else {
                    showToast(requireContext(), response.message)
                }

            }

            override fun fail(from: String) {
                progressBar.hideProgress()
                showToast(requireContext(), from)
            }
        })


    }


    override fun setData() {

    }

    fun backPressed() {
        Toast.makeText(requireContext(), "RegistrFragment", Toast.LENGTH_SHORT).show()
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun getUseType() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.userType(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: UserTypeResponse =
                    Gson().fromJson(message, UserTypeResponse::class.java)
                if (!response.error) {
                    // Extracting user_type from the response data
                    userTypelist=response.data.toMutableList()
                    userTypelist.addAll(response.data)
                    val userTypeList = response.data.mapNotNull {
                        it.user_type

                    }


                    // Set the custom adapter for the AutoCompleteTextView
                    val adapter = UserTypeAdapter(requireContext(), userTypeList)

                    val autoCompleteTextView: AutoCompleteTextView =
                       binding.rbLoginMethod
                    autoCompleteTextView.setAdapter(adapter)

                } else {
                    Toast.makeText(requireContext(), "Unable to Load Bank List", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Unable to Load Bank List", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }






    private fun convertJSONObjectToJsonObject(jsonObject: JSONObject): JsonObject {
        return JsonParser.parseString(jsonObject.toString()).asJsonObject
    }

    private fun onBoardingRequest() {
        val jsonObject = JSONObject()
        jsonObject.put("name", binding.edtName.text.toString())
        jsonObject.put("email", binding.edtEmail.text.toString())
        jsonObject.put("mobileNo",  binding.edtRegMobile.text.toString())
        jsonObject.put("latitude",  userSession.getData(Constant.M_LAT))
        jsonObject.put("longitude", userSession.getData(Constant.M_LONG))
        val gsonJsonObject = convertJSONObjectToJsonObject(jsonObject)
       // val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.noBoardingRequest(requireContext(), gsonJsonObject, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: ReqRegistrationResponse =
                    Gson().fromJson(message, ReqRegistrationResponse::class.java)
                if (response.error == false) {
                    val showDialog = DialogOK(myActivity)
                   showDialog.showErrorDialog(
                        requireContext(),
                        "Thank You!!",
                        "Thank You For Interest in PayPanda Payment Solutions.Our enterprise team will contact you shortly./n If you have any further questions, Feel Free To Contact us at support@paypanda.in or call us toll-free at 18008890178",
                        lottieWidth = 200,
                        lottieHeight = 200
                    )
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "fail", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    class UserTypeAdapter(context: Context, private val userTypeList: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, userTypeList) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            // Customize your view here if needed
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            // Customize your dropdown view here if needed
            return view
        }
    }
}