package app.pay.panda

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import app.pay.panda.databinding.FragmentPaymentRequestDialogBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.paymentrequesttouser.PaymentrequesttouserResponse

import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
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
        arguments?.let {
            amount = it.getString("amount").toString()
            date = it.getString("date")
            status = it.getString("status")
            id = it.getString("id")
            mobile = it.getString("mobile")
            userType = it.getString("userType")
        }
        userSession= UserSession(requireContext())
        // Populate the spinner
        val statusArray = arrayOf("Pending", "Rejected", "Approved")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statusArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStatus.adapter = adapter
        val position = statusArray.indexOf(status)
        if (position >= 0) {
            binding.spinnerStatus.setSelection(position)
        }
        binding.txtdate.text=date
        binding.amount.text= amount.toString()

        // Set up listeners for buttons
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnUpdate.setOnClickListener {
            paymentRequestToUser()
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
