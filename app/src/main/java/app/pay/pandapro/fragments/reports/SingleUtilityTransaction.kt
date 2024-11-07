package app.pay.pandapro.fragments.reports

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import app.pay.pandapro.BaseBottomFragment
import app.pay.pandapro.databinding.FragmentSingleUtilityTransactionBinding
import app.pay.pandapro.helperclasses.CustomProgressBar
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.singleutility.Data
import app.pay.pandapro.responsemodels.singleutility.SingleUtilityTransaction
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SingleUtilityTransaction : BaseBottomFragment<FragmentSingleUtilityTransactionBinding>(FragmentSingleUtilityTransactionBinding::inflate) {

    private lateinit var userSession: UserSession
    private var batchId = ""
    private var list = ArrayList<Data>()
    val data = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSession = UserSession(requireContext())
        batchId = arguments?.getString("id").toString()
    }

    override fun init() {
        getTxnByUtilityId()

    }

    private fun getTxnByUtilityId() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.getTxnByUtilityId(requireContext(), batchId, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: SingleUtilityTransaction = Gson().fromJson(message, SingleUtilityTransaction::class.java)
                if (!response.error) {
                    if (list.isNotEmpty()) {
                        list.clear()
                    }
                    val serviceLabel = getServiceLabel(response.data.operator_name)

// Set the label to the TextView or other UI element
                    binding.consumnumber1.text = serviceLabel
                   // getServiceLabel(response.data.operator_name)
                    binding.billerName.text=response.data.operator_name
                    binding.number.text=response.data.user_id.mobile
                    binding.Name.text=response.data.user_id.name
                    binding.ShopName.text=response.data.user_id.shop_name
                    binding.TxnIdnew.text=response.data.txn_id
                    binding.operatorRefId.text=response.data.txnReferenceId
                    binding.amount.text=response.data.amount
                    binding.amount.text=response.data.amount
                    binding.txnId.text=response.data.txnReferenceId
                    binding.serviceName.text=response.data.category_id.service_name
                    binding.ConsumerNo.text=response.data.ca_num
                    binding.name1.text=response.data.user_id.name
                    binding.shopnam.text=response.data.user_id.shop_name
                    binding.reqTime.text=response.data.createdAt

                   /* val zonedDateTime = ZonedDateTime.parse(response.data.createdAt)

                    // Format it to a simpler form, like dd-MM-yyyy
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    val formattedDate = zonedDateTime.format(formatter)
                    binding.reqTime.text=formattedDate.toString()*/
                } else {
                    Toast.makeText(requireContext(), "Unable to fetch Txn Details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
               // Toast.makeText(requireContext(), "Unable to fetch Txn", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun getServiceLabel(serviceName: String): String {
        val serviceLabels = mapOf(
            "Credit Card" to "Credit Card Number",
            "Aeps Bank Withdraw" to "Aadhaar Number",
                                        "Aeps Cash Deposit" to "Aadhaar Number",
            "B2B" to "Reference Number",
            "Broadband Postpaid" to "Customer ID",
            "CMS" to "Customer ID",
            "Cable TV" to "Subscriber ID",
            "Clubs and Associations" to "Member ID",
            "DMT" to "Beneficiary Account",
            "DTH" to "Biller Number",
            "Donation" to "Donor ID",
            "Education Fees" to "Student ID",
            "Electricity" to "Consumer Number",
            "Fastag" to "Vehicle Number",
            "Gas" to "Consumer Number",
            "Health Insurance" to "Policy Number",
            "Water" to "Consumer Number",
            "Subscription" to "Subscription ID",
            "Rental" to "Tenant ID",
            "Recurring Deposit" to "Account Number",
            "Recharge" to "Mobile Number",
            "Quick Dhan" to "Transaction ID",
            "Payout" to "Beneficiary ID",
            "NCMC Recharge" to "Card Number",
            "Municipal Taxes" to "Property ID",
            "Municipal Services" to "Service ID",
            "Mobile Prepaid" to "Mobile Number",
            "Mobile Postpaid" to "Mobile Number",
            "Metro Recharge" to "Card Number",
            "Loan Repayment" to "Loan Account Number",
            "Life Insurance" to "Policy Number",
            "Landline Postpaid" to "Landline Number",
            "LPG Gas" to "LPG ID",
            "Insurance" to "Policy Number",
            "Housing Society" to "Member ID",
            "Hospital and Pathology" to "Patient ID",
            "SBI Card" to "SBI Card"
        )

        return serviceLabels[serviceName] ?: "Reference Number"
    }

    override fun addListeners() {
        binding.tvShare.setOnClickListener {
            val bitmap = captureRelativeLayout(binding.mainContent)
            val imageUri = saveBitmapToFile(requireContext(), bitmap)
            imageUri?.let { uri ->
                shareImageToWhatsApp(requireContext(), uri)
            }
        }
        binding.tvDownload.setOnClickListener {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                progressBar.hideProgress()
            }
        }
    }

    override fun setData() {

    }

    private fun captureRelativeLayout(layout: MaterialCardView): Bitmap {
        val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }

    private fun shareImageToWhatsApp(context: Context, imageUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            setPackage("com.whatsapp") // Targeting WhatsApp specifically
        }

        // Start the share intent
        try {
            context.startActivity(Intent.createChooser(intent, "Share Image"))
           binding.btn.visibility=View.GONE
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "WhatsApp not installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
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

}
