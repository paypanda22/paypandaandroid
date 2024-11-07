package app.pay.pandapro.fragments.dmt

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.pandapro.BaseBottomFragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import app.pay.pandapro.adapters.DmtTxnReceiptAdapter
import app.pay.pandapro.databinding.FragmentSingleDmtTransactionBinding
import app.pay.pandapro.helperclasses.CustomProgressBar
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.dmtTxnByBath.AllTran
import app.pay.pandapro.responsemodels.dmtTxnByBath.BatchWiseTxnListResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SingleDmtTransaction : BaseBottomFragment<FragmentSingleDmtTransactionBinding>(FragmentSingleDmtTransactionBinding::inflate) {
    private lateinit var userSession: UserSession
    private var batchId=""
    private var list=ArrayList<AllTran>()
    @SuppressLint("SetTextI18n")
    override fun init() {
        userSession= UserSession(requireContext())

        batchId=arguments?.getString("batchId").toString()
        getBatchTxn()
    }

    private fun getBatchTxn() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        binding.imageView.visibility= VISIBLE
        binding.mcvMain.visibility= GONE
        UtilMethods.getTxnByBatchId(requireContext(), batchId, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: BatchWiseTxnListResponse = Gson().fromJson(message, BatchWiseTxnListResponse::class.java)
                if (response.data.invoice.isNotEmpty()){
                    binding.tvShopName.text=response.data.invoice[0].shop_name.toString()
                    binding.tvBankName.text=response.data.invoice[0].bank_name.toString()
                    binding.tvCustomerName.text=response.data.invoice[0].customer_name
                    binding.tvCustomerMobile.text=response.data.invoice[0].mobile_number
                    binding.tvBeneName.text=response.data.invoice[0].beneficiary_name
                    binding.tvAccountNumber.text=response.data.invoice[0].account_number
                    binding.tvAmount.text=response.data.invoice[0].totalAmount.toString()
                   // binding.tvServiceCharge.text=response.data.invoice[0].charge.toString()
                    binding.senderMobNo.text=response.data.invoice[0].customer_mobile.toString()
                    binding.tvTxnId.text="Txn Id: "+response.data.invoice[0].allTrans[0].tid.toString()
                    binding.reqTime.text=response.data.invoice[0].createdAt.toString()
                    if (list.isNotEmpty()){
                        list.clear()
                    }
                    list.addAll(response.data.invoice[0].allTrans)

                    val avAdapter= DmtTxnReceiptAdapter(requireContext(),list)
                    binding.rvTxnList.adapter=avAdapter
                    binding.rvTxnList.layoutManager=LinearLayoutManager(requireContext())

                    binding.imageView.visibility= GONE
                    binding.mcvMain.visibility= VISIBLE
                }else{
                    dismiss()
                    Toast.makeText(requireContext(),"Unable to fetch Txn Details",Toast.LENGTH_SHORT).show()
                }
            }

            override fun fail(from: String) {
                dismiss()
                Toast.makeText(requireContext(),"Unable to fetch Txn Details",Toast.LENGTH_SHORT).show()
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
        /*binding.tvDownload.setOnClickListener {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                progressBar.hideProgress()
            }
        }*/

    }

    override fun setData() {

    }
    private fun captureRelativeLayout(layout: RelativeLayout): Bitmap {
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