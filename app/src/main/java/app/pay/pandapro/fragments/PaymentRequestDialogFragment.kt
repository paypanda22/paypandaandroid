package app.pay.pandapro

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import app.pay.pandapro.databinding.FragmentPaymentRequestDialogBinding
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.paymentrequesttouser.PaymentrequesttouserResponse

import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class PaymentRequestDialogFragment : DialogFragment() {
    private lateinit var userSession: UserSession
    private var amount: String = ""
    private var date: String? = ""
    private var status: String? = ""
    private var id: String? = ""
    private var mobile: String? = ""
    private var userType: String? = ""
    private var remark: String? = ""

    private var _binding: FragmentPaymentRequestDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentRequestDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        arguments?.let {
            amount = it.getString("amount").toString()
            date = it.getString("date")
            status = it.getString("status")
            id = it.getString("id")
            mobile = it.getString("mobile")
            userType = it.getString("userType")
            remark = it.getString("remark")
        }
        binding.remark.text = remark
        userSession = UserSession(requireContext())
        // Populate the spinner
        val statusArray = arrayOf("Pending", "Rejected", "Approved")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter
        val position = statusArray.indexOf(status)
        if (position >= 0) {
            binding.spinnerStatus.setSelection(position)
        }
        binding.txtdate.text = date
        binding.amount.text = amount.toString()
        //binding.remark.text= remark

        // Set up listeners for buttons
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        if (status.equals("Approved")) {

        } else {
            binding.btnUpdate.setOnClickListener {
                if (binding.edtTPin.text.isEmpty()) {
                    binding.edtTPin.error="Please Enter TPin"
                } else{
                    paymentRequestToUser()
            }
        }
        }
    }
    private fun paymentRequestToUser() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

            val requestData= hashMapOf<String,Any?>()
            requestData["Payment"]=id
            requestData["_id"]=id
            requestData["amount"]=amount
            requestData["status"]=binding.spinnerStatus.selectedItem.toString()
            requestData["createdAt"]=date
            requestData["pin"]=binding.edtTPin.text.toString()
            requestData["user_id"]=token
            requestData["user_mobile"]=mobile
            requestData["user_type"]=userType
            requestData["authority_remark"]=binding.authorityRemark.text.toString()

    UtilMethods.paymentRequestToUser(requireContext(),token, requestData, object : MCallBackResponse {
        @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                try {
                    val response: PaymentrequesttouserResponse = Gson().fromJson(message, PaymentrequesttouserResponse::class.java)
                    if (!response.error) {
                        Toast.makeText(requireContext(), "Success: ${response.message}", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JsonSyntaxException) {
                    Log.e("JSON Parsing Error", e.message ?: "Unknown error")
                    Toast.makeText(requireContext(), "Failed to parse response.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Request failed: $from", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
