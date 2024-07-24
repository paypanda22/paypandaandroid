package app.pay.panda.fragments.aepsFragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.AepsAllActions
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.AepsBanksAdapter
import app.pay.panda.adapters.ScannerListAdapter
import app.pay.panda.databinding.DialogBankListBinding
import app.pay.panda.databinding.DialogScannerDevicesBinding
import app.pay.panda.databinding.FragmentAepsCwBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.FingerPrintScanner
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.ScanFinger
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.AepsBankClick
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.OnClick
import app.pay.panda.interfaces.ScannerListClick
import app.pay.panda.responsemodels.aepsBanklist.AepsBankListResponse
import app.pay.panda.responsemodels.aepsBanklist.Data
import app.pay.panda.responsemodels.aepsCw.AepsCwResponse
import app.pay.panda.responsemodels.cwAuth.CwAuthResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class AepsCw<Context> : BaseFragment<FragmentAepsCwBinding>(FragmentAepsCwBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    var mobile = ""

    private var selectedPackage = ""
    private var fData = ""
    private var bankIin = ""
    private var aadhaarNumber = ""
    private var maskedAadhaar = ""
    private var merchantAuthKey = ""
    private var bankName = ""
    private var deviceName = ""
    private var isMerchantAuthenticated = false
    private lateinit var scanFinger: ScanFinger
    private lateinit var bankList: MutableList<Data>
    private val startForScannerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK && data != null) {
            fData = data.getStringExtra("PID_DATA").toString()
            if (fData.isNotEmpty()) {
                scanFinger.validateFingerPrint(fData, object : OnClick {
                    override fun onButtonClick() {
                        if (merchantAuthKey.isNotEmpty() && isMerchantAuthenticated) {
                            cashWithdrawal(fData)
                        } else {
                            merchantAuth(fData)
                        }

                    }
                })

            } else {
                Toast.makeText(requireContext(), "No FingerPrint Data Found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Unable to Capture FingerPrint Data", Toast.LENGTH_SHORT).show()
        }

    }

    private fun merchantAuth(fData: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val data = CommonClass.base64urlEncode(fData)
        val mobile = userSession.getData(Constant.MOBILE).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["accessmodetype"] = "APP"
        requestData["latitude"] = userSession.getData(Constant.M_LAT).toString()
        requestData["longitude"] = userSession.getData(Constant.M_LONG).toString()
        requestData["mobilenumber"] = mobile.substring(3)
        requestData["data"] = data
        requestData["user_id"] = token
        UtilMethods.merchantCwAuth(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: CwAuthResponse = Gson().fromJson(message, CwAuthResponse::class.java)
                if (!response.error) {
                    if (response.data.MerAuthTxnId.isNotEmpty()) {
                        binding.rlMerchantAuth.visibility = GONE
                        binding.rlmAuthSuccess.visibility = VISIBLE

                        isMerchantAuthenticated = true
                        merchantAuthKey = response.data.MerAuthTxnId
                    } else {
                        ShowDialog.bottomDialogSingleButton(
                            myActivity,
                            "Merchant Authentication Failed",
                            response.message,
                            "error",
                            object : MyClick {
                                override fun onClick() {
                                   resetMerchantAuth()
                                }
                            })
                    }

                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity, "Merchant Authentication Failed", response.message, "error", object : MyClick {
                        override fun onClick() {
                            resetMerchantAuth()
                        }
                    })
                }

            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "Merchant Authentication Failed", from, "error", object : MyClick {
                    override fun onClick() {
                        isMerchantAuthenticated = false
                        merchantAuthKey = ""
                        binding.rlMerchantAuth.visibility = VISIBLE
                        binding.rlmAuthSuccess.visibility = GONE
                    }
                })
            }
        })

    }

    private fun cashWithdrawal(fData: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val data = CommonClass.base64urlEncode(fData)
        val requestData = hashMapOf<String, Any?>()
        requestData["accessmodetype"] = "APP"
        requestData["latitude"] = userSession.getData(Constant.M_LAT).toString()
        requestData["longitude"] = userSession.getData(Constant.M_LONG).toString()
        requestData["data"] = data
        requestData["nationalbankidentification"] = bankIin
        requestData["mobilenumber"] = binding.edtCustomerMobile.text.toString()
        requestData["adhaarnumber"] = aadhaarNumber
        requestData["requestremarks"] = "Aeps Cash Withdrawal"
        requestData["is_iris"] = "NO"
        requestData["merAuthTxnId"] = merchantAuthKey
        requestData["amount"] = binding.edtAmount.text.toString()
        requestData["user_id"] = token
        UtilMethods.aepsCashWithdrawal(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AepsCwResponse = Gson().fromJson(message, AepsCwResponse::class.java)
                if (!response.error) {
                    ShowDialog.aepsCashWithdrawal(myActivity, response.message, response.data, object : MyClick {
                        override fun onClick() {
                            resetFragment()
                        }
                    })
                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity, "Cash Withdrawal Failed", response.message, "error", object : MyClick {
                        override fun onClick() {
                            resetMerchantAuth()
                        }
                    })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "Cash Withdrawal Failed", from, "error", object : MyClick {
                    override fun onClick() {
                        resetMerchantAuth()
                    }
                })
            }
        })


    }

    private fun resetMerchantAuth() {
        isMerchantAuthenticated = false
        merchantAuthKey = ""

        binding.rlMerchantAuth.visibility = VISIBLE
        binding.rlmAuthSuccess.visibility = GONE
    }

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        scanFinger = ScanFinger(myActivity, userSession, startForScannerResult)

        processArguments()
    }

    private fun processArguments() {
        mobile = arguments?.getString("mobile") ?: ""
        binding.edtCustomerMobile.setText(mobile)
        bankIin = arguments?.getString("bankIin") ?: ""
        aadhaarNumber = arguments?.getString("aadhar") ?: ""
        maskedAadhaar = arguments?.getString("masked_aadhar") ?: ""
        binding.edtCustomerAadhaar.setText(maskedAadhaar)
        bankName = arguments?.getString("bankName") ?: ""
        binding.edtBankName.setText(bankName)
        deviceName = arguments?.getString("device_name") ?: ""

        val hostActivity = activity as? AepsAllActions
        hostActivity?.makeActive()
    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.tvConsent.setOnClickListener { ShowDialog.aadhaarConsent(myActivity) }
        binding.edtBankName.setOnClickListener { getBankList() }
        binding.rlScanner.setOnClickListener { openDeviceDialog() }
        binding.rlDeviceSelected.setOnClickListener { openDeviceDialog() }
        binding.lytBank.setOnClickListener { getBankList() }
        binding.edtCustomerAadhaar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtCustomerAadhaar.text.toString().length > 11) {
                    if (ActivityExtensions.isValidAadhaar(binding.edtCustomerAadhaar.text.toString())) {
                        aadhaarNumber = binding.edtCustomerAadhaar.text.toString()
                        val masked =
                            CommonClass.maskAadhaar(binding.edtCustomerAadhaar.text.toString())
                        binding.edtCustomerAadhaar.setText(masked)
                        binding.edtCustomerAadhaar.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.fingerprint_scan,
                            0,
                            R.drawable.ic_check_green,
                            0
                        )
                    } else {
                        binding.edtCustomerAadhaar.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.fingerprint_scan,
                            0,
                            R.drawable.ic_cancel_red,
                            0
                        )
                    }
                } else {
                    binding.edtCustomerAadhaar.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.fingerprint_scan,
                        0,
                        0,
                        0
                    )
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.edtCustomerMobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtCustomerMobile.text.toString().length == 10) {
                    if (ActivityExtensions.isValidMobile(binding.edtCustomerMobile.text.toString())) {
                        binding.edtCustomerMobile.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_smartphone_2_,
                            0,
                            R.drawable.ic_check_green,
                            0
                        )
                    } else {
                        binding.edtCustomerMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_2_, 0, 0, 0)
                    }
                } else {
                    binding.edtCustomerMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_2_, 0, 0, 0)
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.btnProceed.setOnClickListener {
            if (validate()) {
                if (merchantAuthKey.isNotEmpty() && isMerchantAuthenticated) {
                    scanFinger.yourDevicePackage(selectedPackage)
                } else {
                    showToast(requireContext(), "Complete Merchant Auth First")
                }

            }

        }

        binding.rlMerchantAuth.setOnClickListener {
            if (validate()) {
                if (merchantAuthKey.isNotEmpty() && isMerchantAuthenticated) {
                    showToast(requireContext(), "Merchant Auth Already Completed")
                } else {
                    scanFinger.yourDevicePackage(selectedPackage)
                }
            }

        }

    }

    private fun validate(): Boolean {
        if (binding.edtBankName.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Select Bank Name First", Toast.LENGTH_SHORT).show()
            return false
        }else if (!binding.chkConsent.isChecked){
            showToast(requireContext(),"Kindly Check Aadhaar Consent check box")
            return false
        } else if (bankIin.isEmpty()) {
            Toast.makeText(requireContext(), "Select Bank Name First", Toast.LENGTH_SHORT).show()
            return false
        } else if (selectedPackage.isEmpty()) {
            Toast.makeText(requireContext(), "Select FingerPrint Device Model", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (binding.edtCustomerAadhaar.text.toString().isEmpty() || aadhaarNumber.isEmpty()) {
            binding.edtCustomerAadhaar.error = "Enter a Valid Aadhaar Number"
            return false
        } else if (!ActivityExtensions.isValidMobile(binding.edtCustomerMobile.text.toString())) {
            binding.edtCustomerMobile.error = "Enter a Valid Mobile Number"
            return false
        } else if (binding.edtAmount.text.toString().isEmpty()) {
            binding.edtAmount.error = "Enter a Valid Amount"
            return false
        } else {
            val amt = binding.edtAmount.text.toString().toInt()
            return if (amt < 100 || amt > 1000) {
                binding.edtAmount.error = "Enter a Valid Amount"
                Toast.makeText(requireContext(), "Amount Should be between 100 and 10000", Toast.LENGTH_SHORT).show()
                false
            } else if (amt % 50 != 0) {
                binding.edtAmount.error = "Enter a Valid Amount"
                Toast.makeText(requireContext(), "Amount Should be in Multiple of 50", Toast.LENGTH_SHORT).show()
                false
            } else {
                true
            }


        }
    }

    private fun getBankList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.aepsBankList(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AepsBankListResponse =
                    Gson().fromJson(message, AepsBankListResponse::class.java)
                if (!response.error) {
                    bankList = mutableListOf()
                    bankList.addAll(response.data)
                    openBankListDialog(bankList)

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

    private fun openBankListDialog(bankList: MutableList<Data>) {
        val bankListDialog: Dialog = Dialog(myActivity)
        val dBinding = DialogBankListBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        bankListDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        bankListDialog.setContentView(dBinding.root)
        bankListDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bankListDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bankListDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        bankListDialog.window?.setGravity(Gravity.BOTTOM)
        val clickListener = object : AepsBankClick {
            override fun onItemClicked(
                holder: RecyclerView.ViewHolder,
                model: List<Data>,
                pos: Int
            ) {
                binding.edtBankName.setText(model[pos].bankName)
                bankIin = model[pos].iinno.toString()
                bankListDialog.dismiss()
            }
        }
        val adapter = AepsBanksAdapter(myActivity, bankList, clickListener)
        dBinding.rvBankList.adapter = adapter
        dBinding.rvBankList.layoutManager = LinearLayoutManager(myActivity)
        dBinding.edtBankList.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(dBinding.edtBankList.text.toString())
                val itemCount = dBinding.rvBankList.adapter?.itemCount ?: 0
                if (itemCount > 0) {
                    dBinding.llNoData.visibility = View.GONE
                    dBinding.rvBankList.visibility = View.VISIBLE
                } else {
                    dBinding.llNoData.visibility = View.VISIBLE
                    dBinding.rvBankList.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        bankListDialog.setCancelable(true)
        bankListDialog.show()

    }

    private fun openDeviceDialog() {
        val scannerDialog: Dialog = Dialog(myActivity)
        val dBinding = DialogScannerDevicesBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        scannerDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        scannerDialog.setContentView(dBinding.root)
        scannerDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scannerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        scannerDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        scannerDialog.window?.setGravity(Gravity.BOTTOM)
        val list = FingerPrintScanner.getScannerList()
        val clickListener = object : ScannerListClick {
            override fun onItemClicked(
                holder: RecyclerView.ViewHolder,
                model: List<FingerPrintScanner>,
                pos: Int
            ) {
                userSession.setData(Constant.DEVICE_NAME, model[pos].getDeviceName())
                userSession.setData(Constant.SCANNER_PACKAGE, model[pos].getPackageName())
                userSession.setData(Constant.SCANNER_IMAGE, model[pos].getImageURL())
                binding.tvScannerName.text = model[pos].getDeviceName()
                MyGlide.with(requireContext(), Uri.parse(Constant.Image_Base_URL+model[pos].getImageURL()),binding.ivScannerImage)
                binding.rlScanner.visibility= View.GONE
                binding.rlDeviceSelected.visibility= View.VISIBLE
                selectedPackage = model[pos].getPackageName()
                scannerDialog.dismiss()
            }
        }
        val adapter = ScannerListAdapter(myActivity, list, clickListener)
        dBinding.rvScanner.adapter = adapter
        dBinding.rvScanner.layoutManager = LinearLayoutManager(myActivity)

        scannerDialog.setCancelable(true)
        scannerDialog.show()

    }

    override fun setData() {
        if (userSession.getData(Constant.DEVICE_NAME) != null) {
            binding.tvScannerName.text = userSession.getData(Constant.DEVICE_NAME)
            MyGlide.with(requireContext(), Uri.parse(Constant.Image_Base_URL+userSession.getData(Constant.SCANNER_IMAGE).toString()),binding.ivScannerImage)
            binding.rlScanner.visibility= View.GONE
            binding.rlDeviceSelected.visibility= View.VISIBLE
        }
        if (userSession.getData(Constant.SCANNER_PACKAGE) != null) {
            selectedPackage = userSession.getData(Constant.SCANNER_PACKAGE).toString()
        }
    }

    private fun resetFragment() {
        isMerchantAuthenticated = false
        merchantAuthKey = ""
        aadhaarNumber = ""
        bankIin = ""
        binding.rlMerchantAuth.visibility = VISIBLE
        binding.rlmAuthSuccess.visibility = GONE

        binding.edtCustomerAadhaar.text?.clear()
        binding.edtCustomerMobile.text?.clear()
        binding.edtBankName.text?.clear()
        binding.edtAmount.text?.clear()

    }


}