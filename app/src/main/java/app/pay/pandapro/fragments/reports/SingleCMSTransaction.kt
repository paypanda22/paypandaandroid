package app.pay.pandapro.fragments.reports

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import app.pay.pandapro.BaseBottomFragment
import app.pay.pandapro.databinding.FragmentSingleCMSTransactionBinding
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.cmsinvoice.CMSInvoiceResponse
import app.pay.pandapro.responsemodels.cmsinvoice.Data

import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson

class SingleCMSTransaction : BaseBottomFragment<FragmentSingleCMSTransactionBinding>(FragmentSingleCMSTransactionBinding::inflate) {
    private lateinit var userSession: UserSession
    private var batchId = ""
    private lateinit var operatorList: MutableList<Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(requireContext())
        batchId = arguments?.getString("id").toString()
    }

    override fun init() {
        cmsInvoice()
    }
    private fun cmsInvoice() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.cmsInvoice(requireContext(), batchId, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: CMSInvoiceResponse = Gson().fromJson(message, CMSInvoiceResponse::class.java)
                if (!response.error) {
                    if (operatorList.isNotEmpty()) {
                        operatorList.clear()
                    }


                    binding.Name.text=response.data.shop_name
                    binding.number.text=response.data.mobile_number

                    binding.txnId.text=response.data.txn_id
                    binding.Amount.text=response.data.totalAmount.toString()
                    binding.Status.text=response.data.status.toString()
                } else {
                    Toast.makeText(requireContext(), "Unable to fetch Txn Details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), "Unable to fetch Txn", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun addListeners() {

    }

    override fun setData() {

    }

}