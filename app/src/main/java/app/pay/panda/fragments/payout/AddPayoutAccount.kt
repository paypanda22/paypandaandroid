package app.pay.panda.fragments.payout

import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.databinding.FragmentAddPayoutAccountBinding
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.PayoutActivity
import app.pay.panda.adapters.DmtBankAdapter
import app.pay.panda.databinding.DialogCustomlotiFileBinding
import app.pay.panda.dialog.DialogOK
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.BankListClickListner
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.dmtBankList.DMTBankListResponse
import app.pay.panda.responsemodels.dmtBankList.Data
import app.pay.panda.responsemodels.verifyBankName.VerifyBankResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

class AddPayoutAccount : BaseFragment<FragmentAddPayoutAccountBinding>(FragmentAddPayoutAccountBinding::inflate) {

    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var bankList = ArrayList<Data>()
    private var bankID = ""
    private var bankVerified=false
    private var tvValidateBeneficiaryName = false
    override fun init() {
        //   nullActivityCheck()
        userSession = UserSession(requireContext())

    }
    private fun showCustomDialog() {
        val dialog = Dialog(requireContext(), R.style.FullScreenDialogStyle)
        val dbinding = DialogCustomlotiFileBinding.inflate(layoutInflater)
        dialog.setContentView(dbinding.root)

        // Set the dialog properties
        dialog.setCancelable(false) // User cannot dismiss the dialog
        dbinding.notifyButton.setOnClickListener {

            Toast.makeText(requireContext(), "You will be notified", Toast.LENGTH_SHORT).show()
            handleBackPressCustom()
        }
        // Optionally, dismiss the dialog
        dialog.dismiss()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        dialog.show()
    }
     fun handleBackPressCustom(): Boolean {
        startActivity(Intent(requireContext(), DashBoardActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        return true
    }
    override fun addListeners() {
        showCustomDialog()
      /*  val showDialog = DialogOK(requireContext())
        showDialog.showForceDialog(
            requireContext(),
            "Coming Soon","",
            lottieResId = R.raw.celebration,
            lottieWidth = 200,
            lottieHeight = 200
        )*/

       /* binding.tvValidateBeneficiaryName.setOnClickListener {
            if (bankID.isBlank()){
                binding.edtBankName.error="Select Bank Name"
            }else if (binding.edtAccountNumber.text.toString().isEmpty()){
                binding.edtAccountNumber.error="Provide a Valid bank Account"
            }else if (!ActivityExtensions.isValidIfsc(binding.edtIfsc.text.toString())){
                binding.edtIfsc.error="Enter a Valid Ifsc Code"
            }else{
                verifyBankName()
            }
        }
        binding.btnAddBeneficiary.setOnClickListener {
            if (binding.edtName.text.toString().isEmpty()){
                binding.edtName.error="Enter a Account Holder Name"
            }else if (binding.edtBankName.text.toString().isEmpty()){
                showToast(requireContext(),"Select Bank Name")
            }else if (!ActivityExtensions.isValidIfsc(binding.edtIfsc.text.toString())){
                binding.edtIfsc.error="Enter a Valid IFSC"
            }else if (binding.edtAccountNumber.text.toString().isEmpty()){
                binding.edtAccountNumber.error="Enter Account Number"
            }else if (!bankVerified){
                Toast.makeText(requireContext(),"Please Verify bank Account First",Toast.LENGTH_SHORT).show()
            } else{
                addBankDetails()
            }
        }
    binding.ivCancel.setOnClickListener {
        findNavController().popBackStack()
    }

        binding.edtBankName.setOnClickListener {
            addbank()
        }
    }
private fun addbank(){
    UtilMethods.dmtBankList(requireContext(), object : MCallBackResponse {
        override fun success(from: String, message: String) {
            val response: DMTBankListResponse =
                Gson().fromJson(message, DMTBankListResponse::class.java)
            if (response.data.isEmpty()) {
                ShowDialog.showDialog(
                    requireActivity(),
                    "Unable to Fetch bank List",
                    from,
                    "error",
                    object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            } else {
                if (bankList.isNotEmpty()) {
                    bankList.clear()
                }
                bankList.addAll(response.data)
                openBankListDialog()
            }
        }

        override fun fail(from: String) {
            ShowDialog.showDialog(
                requireActivity(),
                "Unable to Fetch bank List",
                from,
                "error",
                object : MyClick {
                    override fun onClick() {
                        findNavController().popBackStack()
                    }
                })
        }
    })
*/
}
   /* private fun verifyBankName() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()

        val requestData= hashMapOf<String,Any>()
        requestData["name"]=binding.edtName.text.toString()
        requestData["ifsc"]=binding.edtIfsc.text.toString()
        requestData["phone"]=binding.edtAccountNumber.text.toString()
        requestData["remarks"]=""
        requestData["bankAccount"]=binding.edtAccountNumber.text.toString()
        requestData["user_id"]=token
        UtilMethods.verifyBankAccount(requireContext(),requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: VerifyBankResponse =Gson().fromJson(message, VerifyBankResponse::class.java)
                bankVerified=true
                binding.tvValidateBeneficiaryName.visibility= GONE
               // binding.edtName.setText(response.data.data.nameAtBank)
                Toast.makeText(requireContext(),"Bank Verified Successfully",Toast.LENGTH_SHORT).show()
            }

            override fun fail(from: String) {
                bankVerified=false
                Toast.makeText(requireContext(),"Unable to Verify Bank Details",Toast.LENGTH_SHORT).show()
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

        val clickListener = object : BankListClickListner {
            override fun onItemClicked(
                holder: RecyclerView.ViewHolder,
                model: List<Data>,
                pos: Int
            ) {
                binding.edtBankName.setText(model[pos].bank_name)
                bankID = model[pos].bankID
                alertDialog.dismiss()
            }
        }

        val bankListAdapter = DmtBankAdapter(requireActivity(), bankList, clickListener)
        rvList.adapter = bankListAdapter
        rvList.layoutManager = LinearLayoutManager(requireActivity())

        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bankListAdapter.filter.filter(edtName.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun addBankDetails() {
        val requestData = hashMapOf<String, Any>()
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        //   val phone=userSession.getData(Constant.MOBILE).toString()

        requestData["name"] = binding.edtName.text.toString()
        requestData["ifsc"] = binding.edtIfsc.text.toString()
        requestData["phone"] = binding.edtMobile.text.toString()
     *//*   requestData["bank_id"] = bankID*//*
        requestData["bankAccount"] = binding.edtAccountNumber.text.toString()
        requestData["user_id"] = token
        val progressBar = CustomProgressBar()
        UtilMethods.savePayoutDetails(requireContext(), requestData, object : MCallBackResponse {

            override fun success(from: String, message: String) {
                progressBar.hideProgress()
                userSession.setBoolData(Constant.ISBANK, true)
                userSession.setIntData(Constant.LOGIN_STEPS, 4)
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "Bank Added Successfully",
                    "Bank Account verified and added, Proceed To next Step",
                    "success",
                    object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "ERROR",
                    "Unable to Add Your Bank Details. Kindly try after some time",
                    "error",
                    object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }
        })*/
    //}
    override fun setData() {
        // Set data to views
    }

}