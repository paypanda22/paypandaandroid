package app.pay.retailers.fragments.onboarding

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.DmtBankAdapter
import app.pay.retailers.databinding.FragmentOnboardBankDetailsBinding
import app.pay.retailers.helperclasses.ActivityExtensions
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.BankListClickListner
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.responsemodels.dmtBankList.DMTBankListResponse
import app.pay.retailers.responsemodels.dmtBankList.Data
import app.pay.retailers.responsemodels.verifyBankName.VerifyBankResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson


class OnboardBankDetailsFragment : BaseFragment<FragmentOnboardBankDetailsBinding>(FragmentOnboardBankDetailsBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private var bankList=ArrayList<Data>()
    private var bankID=""
    var accountType: String = "Saving"
    private var bankVerified=false
    override fun init() {
        nullActivityCheck()
        userSession= UserSession(requireContext())

        binding.edtName.addTextChangedListener(textWatcher)
        binding.edtIfsc.addTextChangedListener(textWatcher)
        binding.atvBankName.addTextChangedListener(textWatcher)
        binding.edtAcNumber.addTextChangedListener(textWatcher)
    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener{findNavController().popBackStack()}


        binding.atvBankName.setOnClickListener {
            UtilMethods.BankList(requireContext(),object:MCallBackResponse{
                override fun success(from: String, message: String) {
                    val response:DMTBankListResponse=Gson().fromJson(message,DMTBankListResponse::class.java)
                    if (response.data.isEmpty()){
                        ShowDialog.showDialog(myActivity,"Unable to Fetch bank List",from,"error",object:MyClick{
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })
                    }else{
                        if (bankList.isNotEmpty()){
                            bankList.clear()
                        }
                        bankList.addAll(response.data)
                        openBankListDialog()
                    }

                }

                override fun fail(from: String) {
                    ShowDialog.showDialog(myActivity,"Unable to Fetch bank List",from,"error",object:MyClick{
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
                }
            })
        }


        binding.tvVerifyBank.setOnClickListener {
           if (bankID.isBlank()){
               binding.atvBankName.error="Select Bank Name"
           }else if (binding.edtAcNumber.text.toString().isEmpty()){
               binding.edtAcNumber.error="Provide a Valid bank Account"
           }else if (!ActivityExtensions.isValidIfsc(binding.edtIfsc.text.toString())){
               binding.edtIfsc.error="Enter a Valid Ifsc Code"
           }else{
               verifyBankName()
           }
        }


        binding.btnSubmit.setOnClickListener {
            if (binding.edtName.text.toString().isEmpty()){
                binding.edtName.error="Enter a Account Holder Name"
            }else if (binding.atvBankName.text.toString().isEmpty()){
                showToast(requireContext(),"Select Bank Name")
            }else if (!ActivityExtensions.isValidIfsc(binding.edtIfsc.text.toString())){
                binding.edtIfsc.error="Enter a Valid IFSC"
            }else if (binding.edtAcNumber.text.toString().isEmpty()){
                binding.edtAcNumber.error="Enter Account Number"
            }else if (!bankVerified){
                Toast.makeText(requireContext(),"Please Verify bank Account First",Toast.LENGTH_SHORT).show()
            } else{
                addBankDetails()
            }
        }


    }

    private fun addBankDetails() {
        val requestData= hashMapOf<String,Any>()
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val phone=userSession.getData(Constant.MOBILE).toString()

        requestData["name"]=binding.edtName.text.toString()
        requestData["ifsc"]=binding.edtIfsc.text.toString()
        requestData["phone"]=phone
        requestData["bank_id"]=bankID
        requestData["bank_name"]=binding.atvBankName.text.toString()
        requestData["bank_account_number"]=binding.edtAcNumber.text.toString()
        requestData["user_id"]=token
        requestData["account_number"]=binding.edtAcNumber.text.toString()

        UtilMethods.saveBankDetails(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                userSession.setBoolData(Constant.ISBANK,true)
                userSession.setIntData(Constant.LOGIN_STEPS,4)

                ShowDialog.bottomDialogSingleButton(myActivity,
                    "Bank Added Successfully",
                    "Bank Account verified and added, Proceed To next Step","success",object:MyClick{
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "ERROR",
                    "Unable to Add Your Bank Details. Kindly try after some time","error",object:MyClick{
                        override fun onClick() {

                        }
                    })
            }
        })

    }


    private fun openBankListDialog() {
        val dialogView = layoutInflater.inflate(R.layout.lyt_rv_name_search_item_list, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
        }
        val edtName: TextInputEditText = dialogView.findViewById(R.id.edtName)
        val rvList: RecyclerView = dialogView.findViewById(R.id.rvList)
        edtName.setHint("Enter Bank Name to Search")

        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        val clickListener=object:BankListClickListner{
            override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
                binding.atvBankName.setText(model[pos].bank_name)
                bankID=model[pos].bankID
                alertDialog.dismiss()
            }
        }

        val bankListAdapter=DmtBankAdapter(myActivity,bankList,clickListener)
        rvList.adapter=bankListAdapter
        rvList.layoutManager=LinearLayoutManager(myActivity)

        edtName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bankListAdapter.filter.filter(edtName.text.toString())

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.rgAcType.setOnCheckedChangeListener { group, checkedId ->
            accountType = when (checkedId) {
                R.id.saving -> {
                    "Saving"  // Set value for 'Saving' account
                }

                R.id.current -> {
                    "Current" // Set value for 'Current' account
                }

                else -> {
                    "" // Default or fallback value
                }
            }
        }
    }

    private fun verifyBankName() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val phone=userSession.getData(Constant.MOBILE).toString()
        val requestData= hashMapOf<String,Any>()
        requestData["name"]=binding.edtName.text.toString()
        requestData["ifsc"]=binding.edtIfsc.text.toString()
        requestData["phone"]=phone
        requestData["remarks"]=""
        requestData["bankAccount"]=binding.edtAcNumber.text.toString()
        requestData["user_id"]=token
        requestData["account_type"]=accountType
        UtilMethods.verifyBankAccount(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: VerifyBankResponse =Gson().fromJson(message,VerifyBankResponse::class.java)
                bankVerified=true

                binding.edtName.setText(response.data.data.nameAtBank)
                Toast.makeText(requireContext(),"Bank Verified Successfully",Toast.LENGTH_SHORT).show()
                binding.tvVerifyBank.isClickable = false
                binding.tvVerifyBank.isFocusable = false
                binding.tvVerifyBank.setTextColor(ContextCompat.getColor(myActivity, R.color.bggrey))
                binding.tvVerifyBank.setBackgroundColor(ContextCompat.getColor(myActivity, R.color.white))
            }

            override fun fail(from: String) {
                bankVerified=false
                Toast.makeText(requireContext(),from,Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun enableVerifyBank() {

        binding.tvVerifyBank.isClickable = true
        binding.tvVerifyBank.isFocusable = true
        binding.tvVerifyBank.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
        binding.tvVerifyBank.setBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimaryDark))
    }
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            enableVerifyBank() // Re-enable the Verify button
        }

        override fun afterTextChanged(s: Editable?) {}
    }


    // Listener for RadioGroup (Account Type Change)
    private fun setupAccountTypeChangeListener() {
        binding.rgAcType.setOnCheckedChangeListener { group, checkedId ->
            // Call the enableVerifyBank function when account type changes
            enableVerifyBank()
        }
    }

    // Call this function to set both listeners
    private fun setupListeners() {
        // Assuming you have an EditText field(s) that you want to watch for changes
        binding.edtName.addTextChangedListener(textWatcher)
        binding.edtAcNumber.addTextChangedListener(textWatcher)

        // Setup listener for account type change (RadioGroup)
        setupAccountTypeChangeListener()
    }
    override fun setData() {

    }

}