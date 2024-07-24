package app.pay.panda.fragments.dmt

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.RecipientListAdapter
import app.pay.panda.databinding.FragmentDMTBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.ShowDialog

import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.MyClick2
import app.pay.panda.interfaces.RecipientListClickListner
import app.pay.panda.responsemodels.dmtBeneficiaryList.Data
import app.pay.panda.responsemodels.dmtBeneficiaryList.RecipientListResponse
import app.pay.panda.responsemodels.dmtSettings.DmtSettingsResponse
import app.pay.panda.responsemodels.dmtcustomer.GetCustomerInfoResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class DMTFragment : BaseFragment<FragmentDMTBinding>(FragmentDMTBinding::inflate) {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    private var customerName = ""
    private var customerMobile = ""
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
                    userSession.setIntData(Constant.VERIFICATION_CHARGE, response.data.bankVerificationCharge)
                    apiID = response.data.defaultApi
                    if (response.data.dmtApiType == "eko") {
                        binding.rbBank2.visibility = GONE
                        apiID = "eko"
                        binding.llNoService.visibility = GONE
                        binding.rlMainContent.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    } else if (response.data.dmtApiType == "Paysprint") {
                        binding.rbBank1.visibility = GONE
                        apiID = "Paysprint"
                        binding.rbBank2.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                        binding.rbBank2.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                        binding.rbBank2.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.white)
                        binding.rbBank2.isChecked = true
                        binding.llNoService.visibility = GONE
                        binding.rlMainContent.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    } else if (response.data.dmtApiType == "both") {
                        binding.rgBank.visibility = VISIBLE
                        if (response.data.defaultApi == "eko") {
                            binding.rbBank1.isChecked = true
                            apiID = "eko"
                            binding.rbBank1.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                            binding.rbBank1.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                            binding.rbBank1.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.white)
                            binding.rbBank2.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound)
                            binding.rbBank2.setTextColor(ContextCompat.getColor(myActivity, R.color.black))
                            binding.rbBank2.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.black)
                        } else {
                            binding.rbBank2.isChecked = true
                            apiID = "Paysprint"
                            binding.rbBank2.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                            binding.rbBank2.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                            binding.rbBank2.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.white)
                            binding.rbBank1.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound)
                            binding.rbBank1.setTextColor(ContextCompat.getColor(myActivity, R.color.black))
                            binding.rbBank1.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.black)

                        }
                        dmt = DMT(this@DMTFragment, myActivity, binding, userSession, apiID)
                        binding.llNoService.visibility = GONE
                        binding.rlMainContent.visibility = VISIBLE
                        binding.imageView.visibility = GONE

                    } else {
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
                    getCustomerInfo()
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

        binding.rgBank.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rbBank1) {
                apiID = "eko"
                binding.rbBank1.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                binding.rbBank1.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.rbBank1.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.white)
                binding.rbBank2.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound)
                binding.rbBank2.setTextColor(ContextCompat.getColor(myActivity, R.color.black))
                binding.rbBank2.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.black)
                binding.edtCustomerNumber.text?.clear()
            } else {
                apiID = "Paysprint"
                binding.rbBank2.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound_primary)
                binding.rbBank2.setTextColor(ContextCompat.getColor(myActivity, R.color.white))
                binding.rbBank2.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.white)
                binding.rbBank1.background = ContextCompat.getDrawable(myActivity, R.drawable.rectanle_backgound)
                binding.rbBank1.setTextColor(ContextCompat.getColor(myActivity, R.color.black))
                binding.rbBank1.buttonTintList = ContextCompat.getColorStateList(myActivity, R.color.black)
                binding.edtCustomerNumber.text?.clear()
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
                    customerMobile = binding.edtCustomerNumber.text.toString()
                    customerName = response.data.response.name
                    avlLimit = response.data.response.available_limit.toInt()
                    setCustomerData(response)
                } else {
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
                    } else {
                        Toast.makeText(requireContext(), "Unable to fetch customer details.", Toast.LENGTH_SHORT).show()
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

    fun getBeneficiaryList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getBeneficiaryList(requireContext(), customerMobile, token, apiID, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: RecipientListResponse = Gson().fromJson(message, RecipientListResponse::class.java)
                if (!response.error) {

                    if (recipients.isNotEmpty()) {
                        recipients.clear()
                    }
                    recipients.addAll(response.data)
                    val beneficiaryListClick = object : RecipientListClickListner {
                        override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
                            dmt = DMT(this@DMTFragment, myActivity, binding, userSession, apiID)
                            dmt.openMakeTransactionDialog(
                                model[pos], avlLimit, customerName, customerMobile
                            )
                        }
                    }
                    val recipientListAdapter = RecipientListAdapter(myActivity, recipients, beneficiaryListClick)
                    binding.rvBeneficiaryList.adapter = recipientListAdapter
                    binding.rvBeneficiaryList.layoutManager = LinearLayoutManager(myActivity)

                    binding.mcvBeneficiaryList.visibility = VISIBLE
                    binding.rvBeneficiaryList.visibility = VISIBLE
                    binding.lytNoBeneficiary.visibility = GONE

                } else {
                    if (response.data.isEmpty()) {
                        ShowDialog.bottomDialogTwoButton(myActivity,
                            "No Beneficiary Found",
                            "No Beneficiary Registered.Do you want to Register a new Beneficiary ?",
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
                            "Customer Not Registered With Us",
                            "Try With Different Mobile Number",
                            "error",
                            object : MyClick {
                                override fun onClick() {
                                    binding.edtCustomerNumber.text?.clear()
                                }
                            })
                    }

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


    }

    override fun setData() {

    }

}