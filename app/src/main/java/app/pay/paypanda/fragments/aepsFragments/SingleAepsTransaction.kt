package app.pay.paypanda.fragments.aepsFragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import app.pay.paypanda.BaseBottomFragment
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.databinding.FragmentSingleAepsTransactionBinding
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.retrofit.Constant
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SingleAepsTransaction : BaseBottomFragment<FragmentSingleAepsTransactionBinding>(FragmentSingleAepsTransactionBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var date=""
    private var bankRrn=""
    private var bankName=""
    private var balAmount=""
    private var aadharNumber=""
    private var amount=""
    private var txnId=""
    @SuppressLint("SetTextI18n")
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        date=arguments?.getString("date").toString()
        bankRrn=arguments?.getString("bankRrn").toString()
        bankName=arguments?.getString("bankName").toString()
        balAmount=arguments?.getString("balAmount").toString()
        aadharNumber=arguments?.getString("aadharNumber").toString()
        amount=arguments?.getString("amount").toString()
        txnId=arguments?.getString("txnId").toString()
        binding.tvDate.text=date
        binding.tvAadhaarNumber.text= "xxxx-xxxx-$aadharNumber"
        binding.tvBankName.text=bankName
        binding.tvBalanceAmount.text=balAmount
        binding.tvTxnAmount.text=amount
        binding.tvBankRrn.text=bankRrn
        binding.tvTxnId.text=txnId



    }

    private fun nullActivityCheck() {
        if (activity!=null) {
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.btnDone.setOnClickListener { dismiss() }
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

    private fun captureRelativeLayout(layout: NestedScrollView): Bitmap {
        val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }

    override fun setData() {
        binding.tvCspName.text=userSession.getData(Constant.NAME).toString()

    }

}