package app.pay.pandapro.fragments.onboarding

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.CountryListAdapter
import app.pay.pandapro.adapters.StateListAdapter
import app.pay.pandapro.databinding.FragmentPersonalDetailsBinding
import app.pay.pandapro.helperclasses.Utils.Companion.showToast
import app.pay.pandapro.helperclasses.ShowDialog
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.CountryListClickListner
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.interfaces.StateListClickListner
import app.pay.pandapro.responsemodels.aadhaarAddress.AadhaarAddressResponse
import app.pay.pandapro.responsemodels.countrylist.CountryListResponse
import app.pay.pandapro.responsemodels.countrylist.Data
import app.pay.pandapro.responsemodels.statelist.StateListResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

class PersonalDetailsFragment : BaseFragment<FragmentPersonalDetailsBinding>(FragmentPersonalDetailsBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var countryID = "101"
    private var stateID = "4007"
    private var countryList = ArrayList<Data>()
    private var stateList = ArrayList<app.pay.pandapro.responsemodels.statelist.Data>()
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getUserAadhaarAddress()
    }

    private fun getUserAadhaarAddress() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getUserAadhaarAddress(requireContext(),token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:AadhaarAddressResponse=Gson().fromJson(message,AadhaarAddressResponse::class.java)
                if (!response.error){
                    val address=response.data.addressLine1+", "+response.data.addressLine2+", "+response.data.city+", "+response.data.district
                    binding.edtPresetAddress.setText(address)
                    binding.atvState.setText(response.data.state)
                    binding.edtPinCode.setText(response.data.pin_code)

                }else{
                   showToast(requireContext(),"Unable to Fetch Aadhaar Address")
                }
            }

            override fun fail(from: String) {
                showToast(requireContext(),"Unable to Fetch Aadhaar Address")
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
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnSubmit.setOnClickListener {
            if (validate()) {
                val token = userSession.getData(Constant.USER_TOKEN).toString()
                val requestData = hashMapOf<String, Any?>()
                requestData["user_id"] = token
                requestData["state"] = stateID
                requestData["country"] = countryID
                requestData["presentAddr"] = binding.edtPresetAddress.text.toString()
                requestData["alternate_mobileNo"] = binding.edtAltMobile.text.toString()
                requestData["educationQualification"] = binding.edtQualification.text.toString()
                UtilMethods.updatePersonalDetails(requireContext(), requestData, object : MCallBackResponse {
                    override fun success(from: String, message: String) {
                        userSession.setBoolData(Constant.ISPERSONALDETAILS,true)
                        val userType = userSession.getData(Constant.USER_TYPE_NAME)
                        if(userType.equals("zsm")|| userType.equals("asm")){
                            userSession.setIntData(Constant.LOGIN_STEPS_ZSM,2)
                        }else{
                            userSession.setIntData(Constant.LOGIN_STEPS,2)
                        }


                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "Details Updated Successfully",
                            "Personal Details Updated Successfully. Proceed to next Step",
                            "success", object : MyClick {
                                override fun onClick() {
                                    userSession.setBoolData(Constant.ISPERSONALDETAILS,true)
                                    findNavController().popBackStack()
                                }
                            })
                    }

                    override fun fail(from: String) {
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "Error in Updating Details ",
                            from,
                            "error", object : MyClick {
                                override fun onClick() {
                                    findNavController().popBackStack()
                                }
                            })
                    }
                })
            }
        }
        binding.atvCountry.setOnClickListener {
            UtilMethods.getCountryList(requireContext(), object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: CountryListResponse = Gson().fromJson(message, CountryListResponse::class.java)
                    if (countryList.isNotEmpty()) {
                        countryList.clear()
                    }
                    countryList.addAll(response.data)
                    openCountryListDialog()
                }

                override fun fail(from: String) {
                    Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.atvState.setOnClickListener {
            if (countryID.isBlank()){
                Toast.makeText(requireContext(),"Select Country First",Toast.LENGTH_SHORT).show()
            }else{
                UtilMethods.getStateList(requireContext(),countryID, object : MCallBackResponse {
                    override fun success(from: String, message: String) {
                        val response: StateListResponse = Gson().fromJson(message, StateListResponse::class.java)
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

        }


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
            override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.pandapro.responsemodels.statelist.Data>, pos: Int) {
                binding.atvState.setText(model[pos].name)
                stateID = model[pos]._id
                alertDialog.dismiss()
            }

        }
        val stateListAdapter = StateListAdapter(myActivity, stateList, clickListner)
        rvList.adapter = stateListAdapter
        rvList.layoutManager = LinearLayoutManager(myActivity)
        edtName.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                stateListAdapter.filter.filter(edtName.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })




    }

    private fun openCountryListDialog() {
        val dialogView = layoutInflater.inflate(R.layout.lyt_rv_name_search_item_list, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
        }
        val edtName: TextInputEditText = dialogView.findViewById(R.id.edtName)
        val rvList: RecyclerView = dialogView.findViewById(R.id.rvList)
        edtName.setHint(R.string.enter_country_name_to_search)

        val alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

        val clickListner = object : CountryListClickListner {
            override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
                binding.atvCountry.setText(model[pos].name)
                countryID = model[pos].id
                alertDialog.dismiss()
            }
        }

        val countryListAdapter = CountryListAdapter(myActivity, countryList, clickListner)
        rvList.adapter = countryListAdapter
        rvList.layoutManager = LinearLayoutManager(myActivity)

        edtName.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtName.text.toString().length>5){
                    countryListAdapter.filter.filter(edtName.text.toString())
                }else{
                    countryListAdapter.filter.filter("")
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun validate(): Boolean {
  if ("+91" + binding.edtAltMobile.text.toString() == binding.edtMobile.text.toString()) {
            binding.edtAltMobile.error = "Alternate mobile should be different from registered mobile"
            return false
        } else if (binding.edtPresetAddress.text.toString().isEmpty()) {
            binding.edtPresetAddress.error = "Enter your present address"
            return false
        } else if (countryID.isEmpty()) {
            binding.atvCountry.error = "Select Country First"
            return false
        } else if (stateID.isEmpty()) {
            binding.atvState.error = "Select State First"
            return false
        } else {
            return true
        }

    }

    override fun setData() {
        binding.edtMobile.setText(userSession.getData(Constant.MOBILE).toString())
        binding.edtName.setText(userSession.getData(Constant.PAN_NAME).toString())
    }

}