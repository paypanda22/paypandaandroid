package app.pay.paypanda.fragments.reports

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import app.pay.paypanda.BaseBottomFragment
import app.pay.paypanda.databinding.FragmentSingleAepsPayoutInvoiceBinding
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.responsemodels.aepspayoutinvoice.AepsPayoutInvoiceResponse
import app.pay.paypanda.responsemodels.aepspayoutinvoice.Data

import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SingleAepsPayoutInvoice : BaseBottomFragment<FragmentSingleAepsPayoutInvoiceBinding>(FragmentSingleAepsPayoutInvoiceBinding::inflate) {
    private lateinit var userSession: UserSession
    private var batchId = ""
    private lateinit var operatorList: MutableList<Data>
    override fun init() {
        aepsPayoutInvoice()
    }

    override fun addListeners() {
        binding.btnDone.setOnClickListener { dismiss() }
        binding.ivShare.setOnClickListener {
            val bitmap = captureRelativeLayout(binding.nsvMain)
            val imageUri = saveBitmapToFile(requireContext(), bitmap)
            shareImageToWhatsApp(requireContext(), imageUri)
        }

    }

    override fun setData() {

    }

    private fun aepsPayoutInvoice() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.aepsPayoutInvoice(requireContext(), batchId, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: AepsPayoutInvoiceResponse = Gson().fromJson(message, AepsPayoutInvoiceResponse::class.java)
                if (!response.error) {
                    operatorList= mutableListOf()
                    if (operatorList.isNotEmpty()) {
                        operatorList.clear()
                    }


                    binding.tvStatus.text=response.data.status
                    binding.bankAccountNumber.text=response.data.bank_account_number

                    binding.tvCspName.text=response.data.shop_name
                    binding.mobileNumber.text=response.data.mobile_number
                    binding.tvBankName.text=response.data.bank_name
                    binding.customerName.text=response.data.customer_name
                    binding.createdAt.text=response.data.createdAt
                    binding.txnId.text=response.data.txn_id
                    binding.tvTxnAmount.text=response.data.amount.toString()
                    binding.tvUtr.text=response.data.utr.toString()
                } else {
                    Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(requireContext())
        batchId = arguments?.getString("id").toString()
    }
    private fun shareImageToWhatsApp(context: Context, imageUri: Uri?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            setPackage("com.whatsapp") // Targeting WhatsApp specifically
        }

        // Start the share intent
        try {
            context.startActivity(Intent.createChooser(intent, "Share Image"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBitmapToFile(context: Context,bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "screenshot.png")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    private fun captureRelativeLayout(layout: NestedScrollView): Bitmap {
        val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }
}