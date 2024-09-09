package app.pay.panda.fragments.reports

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.pay.panda.BaseBottomFragment
import app.pay.panda.R
import app.pay.panda.databinding.FragmentCMSTransactionReportBinding
import app.pay.panda.databinding.FragmentSingleCMSTransactionBinding
import app.pay.panda.databinding.FragmentSingleUtilityTransactionBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.cmsinvoice.CMSInvoiceResponse
import app.pay.panda.responsemodels.cmsinvoice.Data

import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
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