package app.pay.paypanda.fragments.recharge

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.DashBoardActivity
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.adapters.RechargeOperatorAdapter
import app.pay.paypanda.adapters.TelecomCircleAdapter
import app.pay.paypanda.databinding.DialogDisputeMasterBinding
import app.pay.paypanda.databinding.FragmentMobileRechargeBinding
import app.pay.paypanda.helperclasses.ActivityExtensions
import app.pay.paypanda.helperclasses.TelecomCircle
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.helperclasses.Utils.Companion.showToast
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.RechargeOperatorClick
import app.pay.paypanda.interfaces.TelecomCircleClick
import app.pay.paypanda.responsemodels.getNumberDetails.NumberDetailsResponse
import app.pay.paypanda.responsemodels.rechargeOperator.Operator
import app.pay.paypanda.responsemodels.rechargeOperator.RechargeOperatorResponse
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson


class MobileRechargeFragment : BaseFragment<FragmentMobileRechargeBinding>(FragmentMobileRechargeBinding::inflate),TelecomCircleClick,RechargeOperatorClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private  var circleList= mutableListOf<TelecomCircle>()
    private var operatorList= mutableListOf<Operator>()
    private lateinit var alertDialog: AlertDialog
    private var circleID=""
    private var fetchNumberDetails=true
    private var operatorID="66617b5f3c35f52923782cbe"
    private lateinit var rHelper:RechargeHelper

    private val pickContactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { contactUri ->
                val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID)
                val cursor = requireActivity().contentResolver.query(contactUri, projection, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                        val contactName = it.getString(nameIndex)
                        val contactId = it.getString(idIndex)

                        // Query for the phone number using the contact ID
                        val phoneProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val phoneCursor = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            phoneProjection,
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(contactId),
                            null
                        )

                        phoneCursor?.use { phoneCursor ->
                            if (phoneCursor.moveToFirst()) {
                                val numberIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                val rawPhoneNumber = phoneCursor.getString(numberIndex)
                                val phoneNumber=extractPhoneNumber(rawPhoneNumber)
                                // Do something with the contact name and phone number
                                binding.edtMobile.setText(phoneNumber)
                                //Toast.makeText(requireContext(), "Picked contact: $contactName, Phone: $phoneNumber", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun extractPhoneNumber(rawPhoneNumber: String?): String {
        val digitsOnly = rawPhoneNumber?.replace(Regex("[^\\d]"), "")
        return if (digitsOnly != null) {
            if (digitsOnly.length > 10) {
                digitsOnly.takeLast(10).toString()
            } else {
                digitsOnly.toString()
            }
        }else{
            ""
        }
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickContact()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }



    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        rHelper= RechargeHelper(myActivity,binding,userSession,findNavController())
        parentFragmentManager.setFragmentResultListener("requestKey",this@MobileRechargeFragment as LifecycleOwner){key, bundle->
            val amt=bundle.getString("amount").toString()
            binding.edtAmount.setText(amt)
        }


    }

    private fun nullActivityCheck() {
        if (activity !=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.btnSubmit.setOnClickListener {
            rHelper.processRecharge(operatorID)
        }
        binding.ivBack.setOnClickListener {
            startActivity(Intent(myActivity, DashBoardActivity::class.java))
            myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        binding.ivPickContact.setOnClickListener { requestContactPermission() }
        binding.edtCircleName.setOnClickListener {
            getCircleList()
        }
        binding.ivChangeCircle.setOnClickListener {
            getCircleList()
        }
        binding.llBrowsePlans.setOnClickListener {
            if (!ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())){
                binding.edtMobile.error="Enter a Valid Mobile"
            }else if(circleID.isEmpty()){
                binding.edtCircleName.error="Select a Telecom Circle"
            }else{
                val bundle=Bundle()
                bundle.apply {
                    putString("mobile",binding.edtMobile.text.toString())
                    putString("circle",circleID)
                    putString("circleName",binding.edtCircleName.text.toString())
                    findNavController().navigate(R.id.action_mobileRechargeFragment_to_fetchPlansFragment)
                }
            }

        }
        binding.edtMobile.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.edtMobile.text.toString().length==10){
                    if (ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())){
                        if (fetchNumberDetails){
                            checkOperatorDetails()

                        }

                    }else{
                        showToast(requireContext(),"Invalid Mobile")
                    }
                }else{
                    fetchNumberDetails=true
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.edtOperatorName.setOnClickListener{ getOperatorList() }
        binding.ivChangeOperator.setOnClickListener { getOperatorList() }
        binding.llBrowsePlans.setOnClickListener {
            if (!ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())){
                binding.edtMobile.error="Enter a Valid Mobile"
            }else{
                val bundle=Bundle()
                bundle.putString("mobile",binding.edtMobile.text.toString())
                findNavController().navigate(R.id.action_mobileRechargeFragment_to_fetchPlansFragment,bundle)
            }

        }

    }

    private fun getOperatorList() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getRechargeOperators(requireContext(),"6683af280b801cb1adc7407a",token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:RechargeOperatorResponse=Gson().fromJson(message,RechargeOperatorResponse::class.java)
                if (!response.error){
                    if (response.data.operators.isNotEmpty()){
                        if (operatorList.isNotEmpty()){
                            operatorList.clear()
                        }
                        operatorList.addAll(response.data.operators)
                        openOperatorListDialog()
                        val cleanedOperatorNames = response.data.operators.map { cleanOperatorName(it.name) }


                    }else{
                        showToast(requireContext(),"Unable to Fetch Operator List")
                    }
                }else{
                    showToast(requireContext(),"Unable to Fetch Operator List")
                }


            }

            override fun fail(from: String) {
                showToast(requireContext(),"Unable to Fetch Operator List")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun openOperatorListDialog() {
        val dBinding = DialogDisputeMasterBinding.inflate(myActivity.layoutInflater)
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dBinding.root)
        }
        dBinding.tvTitle.text="Select Mobile Operator"

        val operatorAdapter=RechargeOperatorAdapter(myActivity,operatorList,this@MobileRechargeFragment)

        dBinding.rvDisputeMaster.adapter = operatorAdapter
        dBinding.rvDisputeMaster.layoutManager = LinearLayoutManager(myActivity)


        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    private fun checkOperatorDetails() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNumberDetails(requireContext(),binding.edtMobile.text.toString(),token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:NumberDetailsResponse=Gson().fromJson(message,NumberDetailsResponse::class.java)
                if (!response.error){
                    fetchNumberDetails=false
                    binding.edtCircleName.setText(response.data.Circle)
                    val cleanedOperatorName = cleanOperatorName(response.data.Operator)
                    binding.edtOperatorName.setText(cleanedOperatorName)
                    circleID=response.data.CircleCode
                }else{

                }

            }

            override fun fail(from: String) {

            }
        })
    }

    private fun cleanOperatorName(operatorName: String): String {
        return when {
            operatorName.contains("Reliance Jio", ignoreCase = true) -> "Reliance Jio"
            operatorName.contains("Airtel", ignoreCase = true) -> "Airtel"
            operatorName.contains("BSNL", ignoreCase = true) -> "BSNL"
            operatorName.contains("Vodafone", ignoreCase = true) -> "Vodafone"
            else -> operatorName // Return as is if no cleaning is required
        }
    }
    @SuppressLint("SetTextI18n")
    private fun getCircleList() {
        circleList=TelecomCircle.getTelecomCircles()
        val dBinding = DialogDisputeMasterBinding.inflate(myActivity.layoutInflater)
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dBinding.root)
        }
        dBinding.tvTitle.text="Select Telecom Circle"

        val circleAdapter=TelecomCircleAdapter(myActivity,circleList,this@MobileRechargeFragment)

        dBinding.rvDisputeMaster.adapter = circleAdapter
        dBinding.rvDisputeMaster.layoutManager = LinearLayoutManager(myActivity)


        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()

    }

    private fun requestContactPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED -> {
                pickContact()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                // Explain why the permission is needed and then request it
                Toast.makeText(requireContext(), "Contacts permission is needed to pick a contact", Toast.LENGTH_LONG).show()
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
            else -> {
                // Directly request for permission
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun pickContact() {
        val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        pickContactLauncher.launch(contactPickerIntent)
    }

    override fun setData() {

    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<TelecomCircle>, pos: Int) {
       binding.edtCircleName.setText(model[pos].name)
        circleID=model[pos].circle_code.toString()
        alertDialog.dismiss()
    }

    override fun onSelectOperator(holder: RecyclerView.ViewHolder, model: List<Operator>, pos: Int) {
       binding.edtOperatorName.setText(model[pos].name)
        operatorID=model[pos]._id
      //  setOperatorLogo()
        alertDialog.dismiss()
    }

    private fun setOperatorLogo() {
        when(operatorID){
            "6683b0150546958c65bf4bb4"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.bsnl))
            }
            "6683b0150546958c65bf4bb6"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.vi))
            }
            "6683b0150546958c65bf4bb4"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.bsnl))
            }
            "6683b0150546958c65bf4bb3"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.airtel))
            }
            "6683b0150546958c65bf4bb5"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.jio))
            }
            "6683b2970546958c65bf4bb8"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.airtel_tv))
            }
            "6683b2970546958c65bf4bb9"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.dish_tv))
            }
            "6683b2970546958c65bf4bba"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.sun_direct))
            }
            "6683b2970546958c65bf4bbb"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.tata_sky))
            }
            "6683b2970546958c65bf4bbc"->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.v_d2h))
            }
            else->{
                binding.ivOperatorImage.setImageDrawable(ContextCompat.getDrawable(myActivity,R.drawable.bsnl))
            }
        }
    }

}