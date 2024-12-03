    package app.pay.retailers.fragments.dmt


    import RecipientListDeserializer
    import android.app.Dialog
    import android.content.Intent
    import android.graphics.Color
    import android.graphics.drawable.ColorDrawable
    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
    import android.view.Gravity
    import android.view.View.GONE
    import android.view.View.VISIBLE
    import android.view.ViewGroup
    import android.view.WindowManager
    import android.widget.Toast
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.FragmentActivity
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView

    import app.pay.retailers.BaseFragment
    import app.pay.retailers.R

    import app.pay.retailers.activity.DashBoardActivity
    import app.pay.retailers.activity.IntroActivity
    import app.pay.retailers.adapters.DeleteRecipient
    import app.pay.retailers.adapters.DmtApiTypeAdapter
    import app.pay.retailers.adapters.RecipientListAdapter
    import app.pay.retailers.databinding.FragmentDMTBinding
    import app.pay.retailers.databinding.LytDmtOtoBinding
    import app.pay.retailers.helperclasses.ActivityExtensions
    import app.pay.retailers.helperclasses.CommonClass
    import app.pay.retailers.helperclasses.ShowDialog

    import app.pay.retailers.helperclasses.UserSession
    import app.pay.retailers.helperclasses.Utils.Companion.showToast
    import app.pay.retailers.interfaces.MCallBackResponse
    import app.pay.retailers.interfaces.MyClick
    import app.pay.retailers.interfaces.MyClick2
    import app.pay.retailers.interfaces.RecipientListClickListner
    import app.pay.retailers.responsemodels.deleteBene.DeleteBeneResponse
    import app.pay.retailers.responsemodels.dmtBeneficiaryList.Data
    import app.pay.retailers.responsemodels.dmtBeneficiaryList.RecipientListResponse
    import app.pay.retailers.responsemodels.dmtSettings.DmtApiType
    import app.pay.retailers.responsemodels.dmtSettings.DmtSettingsResponse
    import app.pay.retailers.responsemodels.dmtcustomer.GetCustomerInfoResponse
    import app.pay.retailers.responsemodels.dmtotp.DMTOtpResponse
    import app.pay.retailers.responsemodels.verifycustomer.VerifyCustomerResponse
    import app.pay.retailers.retrofit.CommonApiCall
    import app.pay.retailers.retrofit.Constant
    import app.pay.retailers.retrofit.UtilMethods
    import com.google.gson.Gson
    import com.google.gson.GsonBuilder


    class DMTFragment : BaseFragment<FragmentDMTBinding>(FragmentDMTBinding::inflate),DmtApiTypeAdapter.DmtTypeClickListener , DeleteRecipient,RecipientListClickListner {
        private lateinit var myActivity: FragmentActivity
        private lateinit var userSession: UserSession
        private var customerName = ""
        private var customerMobile = ""
         private var isAuth:Boolean=true
        private var avlLimit = 0
        private var recipients = ArrayList<Data>()

        private lateinit var dmt: DMT
        private var apiID = ""
        override fun init() {
            nullActivityCheck()
            userSession = UserSession(requireContext())
            getDmtSettings()

        }

        private fun getDmtSettings() {
            UtilMethods.getDmtSettings(requireContext(), object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: DmtSettingsResponse = Gson().fromJson(message, DmtSettingsResponse::class.java)
                    if (!response.error) {
                        if (response.data.dmtApiType.isNotEmpty()){
                            apiID=response.data.defaultApi
                            val defaultPosition=response.data.dmtApiType.indexOfFirst { it._id == apiID }
                            val dmtAPIAdapter=DmtApiTypeAdapter(myActivity,response.data.dmtApiType,this@DMTFragment,defaultPosition)
                            binding.rvDmtApiType.adapter=dmtAPIAdapter
                            binding.rvDmtApiType.layoutManager = LinearLayoutManager(myActivity, LinearLayoutManager.HORIZONTAL, false)
                            dmtAPIAdapter.setDefaultSelectedPosition()


                            binding.llNoService.visibility = GONE
                            binding.rlMainContent.visibility = VISIBLE
                            binding.imageView.visibility = GONE
                        }else{
                            binding.llNoService.visibility = VISIBLE
                            binding.rlMainContent.visibility = GONE
                            binding.imageView.visibility = GONE
                        }


                    } else {
                        binding.llNoService.visibility = VISIBLE
                        binding.rlMainContent.visibility = GONE
                        binding.imageView.visibility = GONE

                    }
                }

                override fun fail(from: String) {
                    binding.llNoService.visibility = VISIBLE
                    binding.rlMainContent.visibility = GONE
                    binding.imageView.visibility = GONE
                }
            })
        }

        private fun nullActivityCheck() {
            if (activity == null) {
                startActivity(Intent(context, IntroActivity::class.java))
            } else {
                myActivity = activity as FragmentActivity
            }
        }

        override fun addListeners() {
            binding.ivBack.setOnClickListener {
                startActivity(Intent(myActivity, DashBoardActivity::class.java))
                myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
            binding.edtCustomerNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (ActivityExtensions.isValidMobile(s.toString())) {
                        CommonClass.hideKeyBoard(myActivity, binding.edtCustomerNumber)
                        if (isAuth == true) {
                            getCustomerInfo()
                        }else{
                            findNavController().navigate(R.id.FrgamentAadhaarAuth)
                            binding.edtCustomerNumber.text?.clear()
                        }
                    } else {
                        customerName = ""
                        customerMobile = ""
                        avlLimit = 0
                        binding.mcvCustomerDetails.visibility = GONE
                        binding.mcvBeneficiaryList.visibility = GONE
                        binding.btnGetBeneficiary.visibility = GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
            binding.btnGetBeneficiary.setOnClickListener {
                if (!ActivityExtensions.isValidMobile(customerMobile)) {
                    Toast.makeText(requireContext(), "Unable to Fetch Recipient List.Try Again", Toast.LENGTH_SHORT).show()
                    binding.mcvCustomerDetails.visibility = GONE
                    customerName = ""
                    customerMobile = ""
                    avlLimit = 0
                    binding.edtCustomerNumber.text?.clear()
                } else {
                    getBeneficiaryList()
                }

            }
            binding.ivMenu.setOnClickListener {
                findNavController().navigate(R.id.action_DMTFragment2_to_dmtTransactionFragment2)
            }
            binding.tvAddNewBeneficiary.setOnClickListener {
                dmt = DMT(this@DMTFragment, myActivity, binding, userSession, apiID)
                dmt.openAddBeneficiaryAccount(customerMobile)

            }
            binding.update.setOnClickListener{
                if(customerMobile==""){

                }else{
                    getBeneficiaryList()
                   // getUpdateBeneficiaryList()
                }

            }
        }


        private fun getCustomerInfo() {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.getDmtCustomerInfo(requireContext(), binding.edtCustomerNumber.text.toString(), token, apiID, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    CommonClass.hideKeyBoard(myActivity, binding.edtCustomerNumber)
                    val response: GetCustomerInfoResponse = Gson().fromJson(message, GetCustomerInfoResponse::class.java)
                    if (!response.error) {
                        if (response.statusCode == "400") {
                            ShowDialog.bottomDialogTwoButton(myActivity,
                                "Customer Not Registered !",
                                "Do you want to register new Customer ?",
                                "error",
                                object : MyClick {
                                    override fun onClick() {
                                        val bundle = Bundle()
                                        bundle.putString("mobile", binding.edtCustomerNumber.text.toString())
                                        bundle.putString("type", "create")
                                        bundle.putString("apiID", apiID)
                                        binding.edtCustomerNumber.text?.clear()
                                        findNavController().navigate(R.id.action_DMTFragment2_to_createCustomerFragment2, bundle)
                                    }
                                },
                                object : MyClick2 {
                                    override fun onCancel() {
                                        binding.edtCustomerNumber.text?.clear()
                                    }
                                })
                        } else if (response.statusCode == "401") {
                            ShowDialog.bottomDialogTwoButton(myActivity,
                                "Customer Not Registered !",
                                "Do you want to register new Customer ?",
                                "error",
                                object : MyClick {
                                    override fun onClick() {
                                        val bundle = Bundle()
                                        bundle.putString("mobile", binding.edtCustomerNumber.text.toString())
                                        bundle.putString("type", "create")
                                        bundle.putString("type", "createOtp")
                                        bundle.putString("apiID", apiID)
                                        binding.edtCustomerNumber.text?.clear()
                                        findNavController().navigate(R.id.action_DMTFragment2_to_createCustomerFragment2, bundle)
                                    }
                                },
                                object : MyClick2 {
                                    override fun onCancel() {
                                        binding.edtCustomerNumber.text?.clear()
                                    }
                                })
                        } else if (response.statusCode == "300") {
                            ShowDialog.bottomDialogTwoButton(myActivity,
                                "Customer OTP Verification Pending !",
                                "Do you want to verify customer mobile ?",
                                "error",
                                object : MyClick {
                                    override fun onClick() {
                                        val bundle = Bundle()
                                        bundle.putString("mobile", binding.edtCustomerNumber.text.toString())
                                        bundle.putString("type", "verify")
                                        bundle.putString("apiID", apiID)
                                        binding.edtCustomerNumber.text?.clear()
                                        findNavController().navigate(R.id.action_DMTFragment2_to_createCustomerFragment2, bundle)
                                    }
                                },
                                object : MyClick2 {
                                    override fun onCancel() {
                                        binding.edtCustomerNumber.text?.clear()
                                    }
                                })
                        } else if (response.statusCode == "007") {
                            openTransactionFilterDialog()
                        } else if (response.statusCode == "008") {
                            openTransactionFilterDialogKYC()
                        } else if(response.statusCode == "200") {
                            customerMobile = binding.edtCustomerNumber.text.toString()
                            customerName = response.data.response.name
                            avlLimit = response.data.response.available_limit.toInt()
                            setCustomerData(response)
                        }else{
                            Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }else{
                         if (response.statusCode == "002") {
                            val bundle = Bundle()
                             bundle.putString("apiID", apiID)
                            bundle.putString("mobile", binding.edtCustomerNumber.text.toString())
                             binding.edtCustomerNumber.text?.clear()
                            findNavController().navigate(R.id.FragmentAadhaarAuthBank2,bundle)

                        }else if(response.statusCode == "003"){
                             openTransactionFilterDialogPaysprint()
                        }else {

                             Toast.makeText(
                                 requireContext(),
                                 response.message.toString(),
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                    }
                }

                override fun fail(from: String) {
                    CommonClass.hideKeyBoard(myActivity, binding.edtCustomerNumber)
                    Toast.makeText(requireContext(), "Request Failed", Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun setCustomerData(response: GetCustomerInfoResponse) {
            binding.mcvCustomerDetails.visibility = VISIBLE
            binding.tvCustomerName.text = response.data.response.name
            binding.tvCustomerStatus.text = response.data.response.state_desc
            binding.tvAvailableLimit.text = response.data.response.available_limit.toString()
            binding.btnGetBeneficiary.visibility = GONE
            getBeneficiaryList()
        }
    /*fun getUpdateBeneficiaryList(){
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        ApiMethods.recipientsListUpdate(requireContext(), token,customerMobile,  apiID, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: RecepentUpdateListResponse = Gson().fromJson(message, RecepentUpdateListResponse::class.java)
                if (!response.error) {

                    if (recipientsupdate.isNotEmpty()) {
                        recipientsupdate.clear()
                    }
                    recipientsupdate.addAll(response.data)
                    val recipientListAdapter = RecipientListAdapter(myActivity, recipients, this@DMTFragment,this@DMTFragment)
                    binding.rvBeneficiaryList.adapter = recipientListAdapter
                    binding.rvBeneficiaryList.layoutManager = LinearLayoutManager(myActivity)

                    binding.mcvBeneficiaryList.visibility = VISIBLE
                    binding.rvBeneficiaryList.visibility = VISIBLE
                    binding.lytNoBeneficiary.visibility = GONE

                } else {



                    }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogTwoButton(myActivity,
                    "ERROR",
                    "Unable to fetch beneficiary list .Try With Different Mobile Number",
                    "error",
                    object : MyClick {
                        override fun onClick() {
                            binding.edtCustomerNumber.text?.clear()
                        }
                    },
                    object : MyClick2 {
                        override fun onCancel() {

                        }
                    })
            }
        })


    }*/
        fun getBeneficiaryList() {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.getBeneficiaryList(requireContext(), customerMobile, token, apiID, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val gson = GsonBuilder()
                        .registerTypeAdapter(RecipientListResponse::class.java, RecipientListDeserializer())
                        .create()
                    val response: RecipientListResponse = gson.fromJson(message, RecipientListResponse::class.java)
                    if (!response.error) {

                        if (recipients.isNotEmpty()) {
                            recipients.clear()
                        }
                        recipients.addAll(response.data)
                        val recipientListAdapter = RecipientListAdapter(myActivity, recipients, this@DMTFragment,this@DMTFragment)
                        binding.rvBeneficiaryList.adapter = recipientListAdapter
                        binding.rvBeneficiaryList.layoutManager = LinearLayoutManager(myActivity)

                        binding.mcvBeneficiaryList.visibility = VISIBLE
                        binding.rvBeneficiaryList.visibility = VISIBLE
                        binding.lytNoBeneficiary.visibility = GONE

                    } else {
                        if (response.data.isEmpty()) {
                            ShowDialog.bottomDialogTwoButton(myActivity,
                                "Message",
                                response.message,
                                "error",
                                object : MyClick {
                                    override fun onClick() {
                                        dmt = DMT(this@DMTFragment, myActivity, binding, userSession, apiID)
                                        dmt.openAddBeneficiaryAccount(customerMobile)
                                    }
                                },
                                object : MyClick2 {
                                    override fun onCancel() {

                                    }
                                })
                        } else {
                            ShowDialog.bottomDialogSingleButton(myActivity,
                                "Message",
                                response.message,
                                "error",
                                object : MyClick {
                                    override fun onClick() {
    //                                    findNavController().navigate(R.id.action_DMTFragment2_to_createCustomerFragment2)
    //                                    binding.edtCustomerNumber.text?.clear()
                                    }
                                })
                        }

                    }
                }

                override fun fail(from: String) {
                    ShowDialog.bottomDialogTwoButton(myActivity,
                        "ERROR",
                        from,
                        "error",
                        object : MyClick {
                            override fun onClick() {
                                binding.edtCustomerNumber.text?.clear()
                            }
                        },
                        object : MyClick2 {
                            override fun onCancel() {

                            }
                        })
                }
            })


        }

        override fun setData() {

        }

        override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<DmtApiType>, pos: Int) {
            apiID=model[pos]._id
            binding.edtCustomerNumber.text?.clear()
        }

        override fun onDeleteIconClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
            val requestData= hashMapOf<String,Any?>()
            requestData["customer_mobile"]=binding.edtCustomerNumber.text.toString()
            requestData["recipient_id"]=model[pos].recipient_id.toString()
            requestData["api_id"]=apiID
            requestData["user_id"]=userSession.getData(Constant.USER_TOKEN).toString()
            CommonApiCall.deleteDmtBeneficiary(requireContext(),"",requestData,null,object:MCallBackResponse{
                override fun success(from: String, message: String) {
                    val response:DeleteBeneResponse=Gson().fromJson(message,DeleteBeneResponse::class.java)
                    if (!response.error){
                        recipients.removeAt(pos)
                        val recipientListAdapter = RecipientListAdapter(myActivity, recipients, this@DMTFragment,this@DMTFragment)
                        binding.rvBeneficiaryList.adapter = recipientListAdapter
                        binding.rvBeneficiaryList.layoutManager = LinearLayoutManager(myActivity)
                    }else{
                        showToast(requireContext(),response.message)
                    }

                }

                override fun fail(from: String) {
                    showToast(requireContext(),from)
                }
            })
        }

        override fun onRecipientItemClick(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
            dmt = DMT(this@DMTFragment, myActivity, binding, userSession, apiID)
            dmt.openMakeTransactionDialog(
                model[pos], avlLimit, customerName, customerMobile
            )
        }

        private fun getOnboardingOtpValidate(otp:String) {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            val mobile = userSession.getData(Constant.MOBILE).toString()
            val requestData= hashMapOf<String,Any?>()
            requestData["mobile"]=mobile.substring(3)
            requestData["user_id"]=token
            requestData["otp"]=otp
            UtilMethods.getOnboardingOtpValidate(requireContext(), token,requestData ,object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: DMTOtpResponse =
                        Gson().fromJson(message, DMTOtpResponse::class.java)
                    if (!response.error) {
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "Congratulations!",
                            response.message,
                            "success",
                            object : MyClick {
                                override fun onClick() {
                                    findNavController().popBackStack()
                                }
                            })

                    }else{
                        showToast(requireContext(), response.message)
                    }

                }

                override fun fail(from: String) {
                    showToast(requireContext(), from)
                }
            })
        }

        private fun openTransactionFilterDialog() {
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
                    getOnboardingOtpValidate(dBinding.etOtp.text.toString())
                }

            }

            filterDialog.setCancelable(true)
            filterDialog.show()
        }
        private fun verifyCustomer(otp: String, filterDialog: Dialog) {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            val requestData = hashMapOf<String, Any?>()
            requestData["customer_mobile"] = binding.edtCustomerNumber.text.toString()
            requestData["user_id"] = token
            requestData["api_id"] = apiID
            requestData["otp"] = otp
            UtilMethods.verifyCustomerOtp(requireContext(), requestData, object : MCallBackResponse {
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
                                    val bundle = Bundle().apply {
                                        putString("customerMobile", binding.edtCustomerNumber.text.toString())
                                        putString("apiID", apiID)
                                    }
                                    if(apiID=="66bca8dd5727c7563ad6e317"){
                                        if(!response.data.bank3_kyc){
                                        findNavController().navigate(R.id.FrgamentAadhaarAuth,bundle)
                                        }else{

                                        }
                                    }else{
                                        findNavController().popBackStack()
                                    }
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
                                findNavController().popBackStack()
                            }
                        })
                }
            })
        }
        private fun openTransactionFilterDialogPaysprint() {
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
                    verifyCustomer(dBinding.etOtp.text.toString(),filterDialog)
                }

            }

            filterDialog.setCancelable(true)
            filterDialog.show()
        }



    }