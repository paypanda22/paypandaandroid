package app.pay.panda.fragments.reports

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import app.pay.panda.BaseBottomFragment
import app.pay.panda.databinding.FragmentSingleUtilityTransactionBinding
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.singleutility.Data
import app.pay.panda.responsemodels.singleutility.SingleUtilityTransaction
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SingleUtilityTransaction : BaseBottomFragment<FragmentSingleUtilityTransactionBinding>(FragmentSingleUtilityTransactionBinding::inflate) {

    private lateinit var userSession: UserSession
    private var batchId = ""
    private var list = ArrayList<Data>()

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
