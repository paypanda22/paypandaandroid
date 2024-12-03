package app.pay.retailers.fragments.aepsFragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import app.pay.retailers.BaseBottomFragment
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.databinding.FragmentSingleAepsTransactionBinding
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.aepscashwithdrawinvoice.CashWithdrawInvoiceResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SingleAepsTransaction : BaseBottomFragment<FragmentSingleAepsTransactionBinding>(FragmentSingleAepsTransactionBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var id=""

    private lateinit var cashwithdrawinvoicelist: MutableList<app.pay.retailers.responsemodels.aepscashwithdrawinvoice.Data>
    @SuppressLint("SetTextI18n")
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        id = arguments?.getString("id").toString()
        aepstxn(id)
    }

    private fun aepstxn(id:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.aepstxn(requireContext(), id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: CashWithdrawInvoiceResponse = Gson().fromJson(message, CashWithdrawInvoiceResponse::class.java)
                if (!response.error) {
                    cashwithdrawinvoicelist = mutableListOf()
                    cashwithdrawinvoicelist.add(response.data)
                    binding.tvTxnId.text=response.data.txn_id
                    binding.tvDate.text=response.data.createdAt
                    binding.tvAadhaarNumber.text= response.data.adhaar_number
                    binding.tvBankName.text=response.data.bank_name
                    binding.tvBalanceAmount.text=response.data.amount.toString()
                    binding.tvTxnAmount.text=response.data.totalAmount.toString()
                    binding.transType.text=response.data.type.toString()
                    binding.tvCspName.text=response.data.shop_name
                } else {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun nullActivityCheck() {
        if (activity!=null) {
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {

        binding.ivShare.setOnClickListener {
            val bitmap = captureRelativeLayout(binding.nsvMain)
            val imageUri = saveBitmapToFile(requireContext(), bitmap)
            shareImageToWhatsApp(requireContext(), imageUri)
        }

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

    private fun captureRelativeLayout(layout: LinearLayout): Bitmap {
        val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }

    override fun setData() {

    }

}