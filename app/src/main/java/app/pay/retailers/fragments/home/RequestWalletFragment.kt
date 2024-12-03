package app.pay.retailers.fragments.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.TransferToAdapter
import app.pay.retailers.databinding.FragmentRequestWalletBinding
import app.pay.retailers.helperclasses.CommonClass
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.responsemodels.companyBanks.CompanyBanksResponse
import app.pay.retailers.responsemodels.transferTo.Data
import app.pay.retailers.responsemodels.transferTo.TransferToResponse
import app.pay.retailers.responsemodels.uploadImage.UploadImageResponse
import app.pay.retailers.responsemodels.walletRequest.WalletRequestResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import java.io.File
import java.time.LocalDate


class RequestWalletFragment : BaseFragment<FragmentRequestWalletBinding>(FragmentRequestWalletBinding::inflate) {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    private var transferTo = ""
    private lateinit var toList: MutableList<Data>
    private lateinit var bankList: MutableList<app.pay.retailers.responsemodels.companyBanks.Data>
    private var paymentDate = ""
    private var bankRef = ""
    private var bank = ""
    private var method = "IMPS"
    private var uploaded_path = ""
    private var account_number = ""
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(myActivity.contentResolver, fileUri)

                    binding.ivReceipt.setImageBitmap(bitmap)

                    val path: String = getRealPathFromURI(fileUri)
                    val file: File = File(path)
                    uploadImage(file)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(myActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(myActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

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

    private fun uploadImage(file: File) {
        Log.e("TAG", "uploadImage: Start")
        myActivity.let {
            myActivity
            UtilMethods.uploadImage(myActivity, file, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: UploadImageResponse = Gson().fromJson(message, UploadImageResponse::class.java)
                    Toast.makeText(myActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    uploaded_path = response.data?.url.toString()
                }

                override fun fail(from: String) {
                    Toast.makeText(myActivity, "Unable to Upload Image", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        CommonClass.getDashBoardData(requireActivity(), userSession)
        binding.tvCurrBalance.text = userSession.getData(Constant.MAIN_WALET).toString()
        getTransferToList()
        val currentDate = LocalDate.now().toString()  // Format will be "yyyy-MM-dd"
        binding.edtPaymentDate.setText(currentDate)
        paymentDate= binding.edtPaymentDate.setText(currentDate).toString()
    //   paymentDate= CommonClass.showDatePickerDialog(myActivity, binding.edtPaymentDate).toString()
    }

    private fun getTransferToList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getTransferTo(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: TransferToResponse = Gson().fromJson(message, TransferToResponse::class.java)
                if (!response.error) {
                    toList = mutableListOf()
                    toList.addAll(response.data)

                    val click = object : TransferToAdapter.TransferToClickListener {
                        override fun onItemClick(position: Int) {
                            transferTo = toList[position].value
                            if (toList[position].id.toInt() == 1) {
                                getAdminBankDetails()
                            } else {
                                binding.llAdminBanks.visibility = GONE
                                binding.lytRemark.visibility = VISIBLE
                            }
                        }
                    }

                    val transferToAdapter = TransferToAdapter(myActivity, toList, click)
                    binding.rvTransferTo.adapter = transferToAdapter
                    binding.rvTransferTo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    binding.rlMainContent.visibility = VISIBLE
                    binding.llNoService.visibility = GONE
                    binding.imageView.visibility = GONE
                } else {
                    binding.rlMainContent.visibility = GONE
                    binding.llNoService.visibility = VISIBLE
                    binding.imageView.visibility = GONE

                }

            }

            override fun fail(from: String) {
                binding.rlMainContent.visibility = GONE
                binding.llNoService.visibility = VISIBLE
                binding.imageView.visibility = GONE
            }
        })
    }

    private fun getAdminBankDetails() {
        UtilMethods.getCompanyBankList(requireContext(), object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CompanyBanksResponse = Gson().fromJson(message, CompanyBanksResponse::class.java)
                if (!response.error) {
                    bankList = mutableListOf()
                    bankList.addAll(response.data)
                    // Create ArrayAdapter for bank account numbers spinner
                    val bankAccountAdapter =
                        ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, bankList.map { it.bank_account_number }
                        )
                    binding.spinnerBankAccount.adapter = bankAccountAdapter

                    // Create ArrayAdapter for bank names spinner
                    val bankNameAdapter =
                        ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, bankList.map { it.bank_name }
                        )
                    binding.spinnerBankName.adapter = bankNameAdapter

                    binding.spinnerBankName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectedData = parent?.getItemAtPosition(position) as String
                            val accountNumber = bankList[position].bank_account_number
                            binding.atvBankAccountNumber.setText(accountNumber)
                            binding.atvBankName.setText(selectedData)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //Do Nothing
                        }
                    }


                    binding.llAdminBanks.visibility = VISIBLE
                    binding.lytRemark.visibility = VISIBLE
                } else {
                    Toast.makeText(requireContext(), "Unable to Fetch Company Bank List", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Unable to Fetch Company Bank List", Toast.LENGTH_SHORT).show()
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

    @SuppressLint("SetTextI18n")
    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.amt500.setOnClickListener { binding.edtAmount.setText("500") }
        binding.amt1000.setOnClickListener { binding.edtAmount.setText("1000") }
        binding.amt2000.setOnClickListener { binding.edtAmount.setText("2000") }
        binding.amt5000.setOnClickListener { binding.edtAmount.setText("5000") }
        binding.atvBankName.setOnClickListener { binding.spinnerBankName.performClick() }
        binding.lytBankName.setEndIconOnClickListener {
            binding.spinnerBankName.performClick()
        }
        binding.edtPaymentDate.setOnClickListener {
            CommonClass.showDatePickerDialog(myActivity, binding.edtPaymentDate)
        }
        val userType = userSession.getData(Constant.USER_TYPE_NAME)


        if (userType == "zsm" || userType == "asm") {
            binding.overdraft.visibility = View.VISIBLE


        } else {
            binding.overdraft.visibility = View.GONE
        }
        binding.rgMethod.setOnCheckedChangeListener { group, checkedId ->
            method = when (checkedId) {
                R.id.rbImps -> {
                    binding.lytBankName.visibility = View.VISIBLE
                    binding.lytBankAc.visibility = View.VISIBLE
                    binding.date.visibility = View.VISIBLE
                    binding.ltyBankRef.visibility = View.VISIBLE
                    binding.rlImageUpload.visibility = View.VISIBLE
                    "IMPS"}
                R.id.rbNeft -> {
                    binding.lytBankName.visibility = View.VISIBLE
                    binding.lytBankAc.visibility = View.VISIBLE
                    binding.date.visibility = View.VISIBLE
                    binding.ltyBankRef.visibility = View.VISIBLE
                    binding.rlImageUpload.visibility = View.VISIBLE
                    "NEFT"}
                R.id.rbCash ->{
                    binding.lytBankName.visibility = View.VISIBLE
                    binding.lytBankAc.visibility = View.VISIBLE
                    binding.date.visibility = View.VISIBLE
                    binding.ltyBankRef.visibility = View.VISIBLE
                    binding.rlImageUpload.visibility = View.VISIBLE
                    "CASH"}
                R.id.rbUpi -> {
                    binding.lytBankName.visibility = View.VISIBLE
                    binding.lytBankAc.visibility = View.VISIBLE
                    binding.date.visibility = View.VISIBLE
                    binding.ltyBankRef.visibility = View.VISIBLE
                    binding.rlImageUpload.visibility = View.VISIBLE
                    "UPI"}
                R.id.overdraft ->{
                    binding.lytBankName.visibility = View.GONE
                    binding.lytBankAc.visibility = View.GONE
                    binding.date.visibility = View.GONE
                    binding.ltyBankRef.visibility = View.GONE
                    binding.rlImageUpload.visibility = View.GONE
                    "overdraft"

                }
                else -> ""
            }
        }
        binding.btnTopup.setOnClickListener {
            val amountText = binding.edtAmount.text.toString()

            // Check if the amount field is empty
            if (amountText.isEmpty()) {
                binding.edtAmount.error = "Enter Amount"
            } else {
                val amount = amountText.toIntOrNull() // Safely convert the amount to an integer

                // Check if the amount is valid and at least 100
                if (amount == null || amount < 100) {
                    binding.edtAmount.error = "Minimum amount is 100"
                    Toast.makeText(myActivity, "Please enter at least 100", Toast.LENGTH_SHORT).show()
                } else {
                    // If the amount is valid and meets the minimum requirement
                    if (transferTo == "Admin") {
                        if (validateInput()) {
                            processRequest()
                        }
                    } else {
                        if (binding.edtRemark.text.toString().isEmpty()) {
                            binding.edtRemark.error = "Enter Remark"
                        } else {
                            processRequest()
                        }
                    }
                }
            }
        }


        binding.ivMenu.setOnClickListener { findNavController().navigate(R.id.action_requestWalletFragment_to_walletRequestListFragment2) }

        binding.rlImageUpload.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .crop(1f, 1f)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }


    }

    private fun validateInput(): Boolean {
        if (binding.atvBankName.text.toString().isEmpty()) {
            binding.atvBankName.error = "Select Bank Name"
            return false
        } else if (binding.atvBankAccountNumber.text.toString().isEmpty()) {
            binding.atvBankAccountNumber.error = "Select Bank Account"
            return false
        } else if (binding.edtPaymentDate.text.toString().isEmpty()) {
            binding.edtPaymentDate.error = "Select Payment Date"
            return false
        } else if (binding.edtBankRef.text.toString().isEmpty()) {
            binding.edtBankRef.error = "Enter Bank Utr"
            return false
        } else if (binding.edtRemark.text.toString().isEmpty()) {
            binding.edtRemark.error = "Enter Remark"
            return false
        } else if (uploaded_path.isEmpty()) {
            Toast.makeText(requireContext(), "Upload Transaction Receipt", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }

    }

    private fun processRequest() {
        if (binding.edtBankRef.text.toString().isNotEmpty()) {
            bankRef = binding.edtBankRef.text.toString()
        }
        if (binding.edtPaymentDate.text.toString().isNotEmpty()) {
            paymentDate = binding.edtPaymentDate.text.toString()
        }
        if (binding.atvBankName.text.toString().isNotEmpty()) {
            bank = binding.atvBankName.text.toString()
        }
        if (binding.atvBankAccountNumber.text.toString().isNotEmpty()) {
            account_number = binding.atvBankAccountNumber.text.toString()
        }

        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["user_id"] = token
        requestData["paymentDate"] = paymentDate
        requestData["bankRef"] = bankRef
        requestData["amount"] = binding.edtAmount.text.toString()
        requestData["bank"] = bank
        requestData["method"] = method
        requestData["account_number"] = account_number
        requestData["remark"] = binding.edtRemark.text.toString()
        requestData["transferTo"] = transferTo
        requestData["receipt_img"]=uploaded_path
        UtilMethods.addPaymentRequest(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: WalletRequestResponse = Gson().fromJson(message, WalletRequestResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity, "SUCCESS", "Wallet Request Successfully Sent", "success", object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })

                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity, "ERROR", response.message, "error", object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
                }

            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "ERROR", "Request Failed", "error", object : MyClick {
                    override fun onClick() {
                        findNavController().popBackStack()
                    }
                })
            }
        })

    }

    override fun setData() {


    }

}