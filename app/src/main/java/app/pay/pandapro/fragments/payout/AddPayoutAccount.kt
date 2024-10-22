package app.pay.pandapro.fragments.payout

import android.app.Activity
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.databinding.FragmentAddPayoutAccountBinding
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.activity.DashBoardActivity
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.DmtBankAdapter
import app.pay.pandapro.databinding.DialogCustomlotiFileBinding
import app.pay.pandapro.helperclasses.ActivityExtensions
import app.pay.pandapro.helperclasses.AesEncryptOld
import app.pay.pandapro.helperclasses.ShowDialog
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.interfaces.BankListClickListner
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.responsemodels.bankverification.BankVerifictionResponse
import app.pay.pandapro.responsemodels.dmtBankList.DMTBankListResponse
import app.pay.pandapro.responsemodels.dmtBankList.Data
import app.pay.pandapro.responsemodels.uploadImage.UploadImageResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import java.io.File

class AddPayoutAccount : BaseFragment<FragmentAddPayoutAccountBinding>(FragmentAddPayoutAccountBinding::inflate) {

    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var bankList = ArrayList<Data>()
    private var bankID = ""
    private var bankVerified = false
    private var tvValidateBeneficiaryName = false
    private lateinit var attachments: MutableList<String>
    var accountNumber = ""
    var confirmAccountNumber = ""
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        attachments = mutableListOf()
         //showCustomDialog()
        handleBackPress()
        val verificationCharge = userSession.getIntData(Constant.VERIFICATION_CHARGE).toInt()
        binding.tvPromtCharges.text = "You will be charged $verificationCharge/- for beneficiary validation"
    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
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
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        dialog.show()
    }

    fun handleBackPressCustom(): Boolean {
        startActivity(Intent(requireContext(), DashBoardActivity::class.java))
        requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        return true
    }

    override fun addListeners() {

        // Add TextWatcher for the account number
        binding.edtAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validateAccountNumbers()
            }
        })

// Add TextWatcher for the confirm account number
        binding.edtcfmAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                validateAccountNumbers()
            }
        })


        binding.btnChooseFile.setOnClickListener {
            takeImage()
        }
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        //showCustomDialog()
        /*  val showDialog = DialogOK(requireContext())
          showDialog.showForceDialog(
              requireContext(),
              "Coming Soon","",
              lottieResId = R.raw.celebration,
              lottieWidth = 200,
              lottieHeight = 200
          )
*/
        binding.tvValidateBeneficiaryName.setOnClickListener {
            accountNumber = binding.edtAccountNumber.text.toString()
            confirmAccountNumber = binding.edtcfmAccountNumber.text.toString()

            if (bankID.isBlank()) {
                binding.edtBankName.error = "Select Bank Name"
            } else if (binding.edtAccountNumber.text.toString().isEmpty()) {
                binding.edtAccountNumber.error = "Provide a Valid bank Account"
            } else if (!ActivityExtensions.isValidIfsc(binding.edtIfsc.text.toString())) {
                binding.edtIfsc.error = "Enter a Valid Ifsc Code"
            } else if (accountNumber != confirmAccountNumber) {
                binding.edtcfmAccountNumber.error =
                    "Account number and Confirm account number do not match"
            } else {
                verifyBankName()
            }
        }
        binding.btnAddBeneficiary.setOnClickListener {

            accountNumber = binding.edtAccountNumber.text.toString()
            confirmAccountNumber = binding.edtcfmAccountNumber.text.toString()
            if (binding.edtName.text.toString().isEmpty()) {
                binding.edtName.error = "Enter a Account Holder Name"
            } else if (binding.edtBankName.text.toString().isEmpty()) {
                showToast(requireContext(), "Select Bank Name")
            } else if (!ActivityExtensions.isValidIfsc(binding.edtIfsc.text.toString())) {
                binding.edtIfsc.error = "Enter a Valid IFSC"
            } else if (binding.edtAccountNumber.text.toString().isEmpty()) {
                binding.edtAccountNumber.error = "Enter Account Number"
            /*} else if (!bankVerified) {
                Toast.makeText(
                    requireContext(),
                    "Please Verify bank Account First",
                    Toast.LENGTH_SHORT
                ).show()*/
            } else if (accountNumber != confirmAccountNumber) {
                binding.edtcfmAccountNumber.error =
                    "Account number and Confirm account number do not match"
            } else {
                addBankDetails()
            }
        }


        binding.edtBankName.setOnClickListener {
            addbank()
        }

        binding.edtcfmAccountNumber.setOnLongClickListener {
            // Prevent long-click actions like paste
            true
        }

        binding.edtcfmAccountNumber.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {}
        })

    }

    private fun addbank() {
        UtilMethods.BankList(requireContext(), object : MCallBackResponse {
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

    }

    private fun verifyBankName() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any>()
        requestData["name"] = binding.edtName.text.toString()
        requestData["ifsc"] = binding.edtIfsc.text.toString()
        requestData["phone"] = binding.edtMobile.text.toString()
        requestData["remarks"] = "Payout Bank Validation"
        requestData["bankAccount"] = binding.edtAccountNumber.text.toString()
        requestData["user_id"] = token
        UtilMethods.bankVerification(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: BankVerifictionResponse =
                    Gson().fromJson(message, BankVerifictionResponse::class.java)
                if(!response.error){
                bankVerified = true
                binding.tvValidateBeneficiaryName.visibility = GONE
                binding.edtName.setText(response.data.bank_account_name)
                Toast.makeText(requireContext(), "Bank Verified Successfully", Toast.LENGTH_SHORT)
                    .show()
            }else{
                    Toast.makeText(requireContext(), response.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun fail(from: String) {
                bankVerified = false
                Toast.makeText(
                    requireContext(),
                    from.toString(),
                    Toast.LENGTH_SHORT
                ).show()
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
                binding.edtIfsc.setText(model[pos].ifsc_code)
                bankID = model[pos]._id
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
        requestData["user_id"] = token
        requestData["name"] = binding.edtName.text.toString()
        requestData["ifsc"] = binding.edtIfsc.text.toString()
        requestData["mobile_number"] = binding.edtMobile.text.toString()
        requestData["bankId"] = bankID
        requestData["account_number"] = binding.edtAccountNumber.text.toString()
        requestData["confirm_account_number"] = binding.edtcfmAccountNumber.text.toString()
        requestData["bank_proof"] = binding.txtFileName.text.toString()
        requestData["bank_name"] = binding.edtBankName.text.toString()
        requestData["isVerified"] = bankVerified


        val encodedData = AesEncryptOld.encodeObj(requestData)
      //  val encodedData1 = AesEncrypt.encodeaesObj(requestData)
        UtilMethods.savePayoutDetails(
            requireContext(),
            token,
            encodedData,
            object : MCallBackResponse {
                override fun success(from: String, message: String) {
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
                        from.toString(),
                        "error",
                        object : MyClick {
                            override fun onClick() {
                                findNavController().popBackStack()
                            }
                        })
                }
            })
    }

    override fun setData() {

    }

    private fun handleBackPress() {
        // Set the callback for back press
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // You can either pop the backstack or handle custom logic here
                    findNavController().popBackStack()  // Navigate back to the previous fragment
                }
            })
    }

    private fun validateAccountNumbers() {
        val accountNumber = binding.edtAccountNumber.text.toString()
        val confirmAccountNumber = binding.edtcfmAccountNumber.text.toString()

        if (accountNumber.isNotEmpty() && confirmAccountNumber.isNotEmpty()) {
            if (accountNumber != confirmAccountNumber) {
                binding.edtcfmAccountNumber.error =
                    "Account number and Confirm account number should be match"
            } else {
                binding.edtcfmAccountNumber.error = null // Clear the error if they match
            }
        } else {
            // Optionally clear the error if fields are empty
            binding.edtcfmAccountNumber.error = null
        }
    }

    private fun takeImage() {
        ImagePicker.with(this)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .crop(1f, 2f)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String
        val cursor = myActivity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(myActivity.contentResolver, fileUri)
               //     binding.attachment1.setImageBitmap(bitmap)
                    val path: String = getRealPathFromURI(fileUri)
                    val file: File = File(path)
                    uploadImage(file)
                }
            }
        }

    private fun uploadImage(file: File) {
        myActivity.let {
            myActivity
            UtilMethods.uploadImage(myActivity, file, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: UploadImageResponse = Gson().fromJson(message, UploadImageResponse::class.java)
                    Toast.makeText(myActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    attachments.add(response.data?.url.toString())
                    binding.txtFileName.text=response.data?.url.toString()
                }

                override fun fail(from: String) {
                    Toast.makeText(myActivity, "Unable to Upload Image", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}