package app.pay.pandapro.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.databinding.FragmentCertificateBinding
import app.pay.pandapro.helperclasses.CommonClass
import app.pay.pandapro.helperclasses.MyGlide
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.certificate.CertificateResponse
import app.pay.pandapro.responsemodels.certificate.Data
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CertificateFragment : BaseFragment<FragmentCertificateBinding>(FragmentCertificateBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var cmslist:MutableList<Data>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        certificate()
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.tvDownload.setOnClickListener {
            val bitmap = captureRelativeLayout(binding.certificateD)
            val imageUri = saveBitmapToFile(requireContext(), bitmap)
            shareImageToWhatsApp(requireContext(), imageUri)
        }
    }

    override fun setData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun certificate(){
    val token = userSession.getData(Constant.USER_TOKEN).toString()
    UtilMethods.certificate(myActivity, token, object : MCallBackResponse {
        override fun success(from: String, message: String) {
            val response: CertificateResponse =
                Gson().fromJson(message, CertificateResponse::class.java)
            if (!response.error) {
                val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
               val cmslist=response.data
         binding.name.text=cmslist.name
         binding.referId.text="Retailer Code: "+cmslist.refer_id
         binding.address.text=cmslist.address
         binding.OnboardingD.text=cmslist.onBoardDate
         binding.Validtill.text=cmslist.validDate
         binding.printDate.text=todayDate.toString()
                MyGlide.with(
                    requireContext(),
                    Uri.parse(Constant.PIMAGE_URL + (cmslist.sign).toString()),
                    binding.sign
                )
            } else {
                Toast.makeText(myActivity, response.error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        override fun fail(from: String) {
            Toast.makeText(myActivity, from, Toast.LENGTH_SHORT)
                .show()
        }
    })
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

    private fun captureRelativeLayout(layout: LinearLayout): Bitmap {
        val bitmap = Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        layout.draw(canvas)
        return bitmap
    }
    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }
}