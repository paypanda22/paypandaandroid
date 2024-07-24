package app.pay.panda.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.databinding.ActivityQrCollectionBinding
import app.pay.panda.databinding.DialogCustomlotiFileBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.quickdhansendotp.QuickDhanSendOtpResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class QrCollectionActivity : BaseActivity<ActivityQrCollectionBinding>() {
    private lateinit var userSession: UserSession
    private val textToEncode = "upi://pay?pa=UPIpos@icici&pn=ABDUL+QUADIR&tr=EZV2024061709182387858915&am=100&cu=INR&mc=6012"
    private var otpRef=""
    private var otpTxnID=""

    override fun getViewBinding(): ActivityQrCollectionBinding =ActivityQrCollectionBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession= UserSession(this@QrCollectionActivity)
        val qrCodeBitmap = generateQRCode(textToEncode)
       // binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)
    }


    private fun showCustomDialog() {
        val dialog = Dialog(this, R.style.FullScreenDialogStyle)
        val dbinding = DialogCustomlotiFileBinding.inflate(layoutInflater)
        dialog.setContentView(dbinding.root)

        // Set the dialog properties
        dialog.setCancelable(false) // User cannot dismiss the dialog
        dialog.setCanceledOnTouchOutside(false)
        dbinding.notifyButton.setOnClickListener {
            Toast.makeText(this, "You will be notified", Toast.LENGTH_SHORT).show()
            // Handle button click here
            handleBackPressCustom()
        }
            // Optionally, dismiss the dialog
            dialog.dismiss()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        dialog.show()

    }

    override fun addListeners() {
      showCustomDialog()
        binding.tv100.setOnClickListener { binding.edtAmount.setText("100") }
        binding.tv500.setOnClickListener { binding.edtAmount.setText("500") }
        binding.tv1000.setOnClickListener { binding.edtAmount.setText("1000") }
        binding.tv200.setOnClickListener { binding.edtAmount.setText("200") }
        binding.btnSubmit.setOnClickListener {
            if (validate()){
                sendOtp()
            }
        }
binding.ivBack.setOnClickListener{
    startActivity(Intent(this@QrCollectionActivity, DashBoardActivity::class.java))
    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

}
    }

    private fun sendOtp() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["mobile"]=binding.edtMobile.text.toString()
        requestData["customer_name"]=binding.edtName.text.toString()
        requestData["amount"]=binding.edtAmount.text.toString()
        requestData["address"]=binding.edtAddress.text.toString()
        requestData["pincode"]=binding.edtPinCode.text.toString()
        requestData["user_id"]=token

        UtilMethods.quickDhanSendOtp(this@QrCollectionActivity,requestData,object:MCallBackResponse{
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response:QuickDhanSendOtpResponse=Gson().fromJson(message,QuickDhanSendOtpResponse::class.java)
                if (response.error==false){
                    binding.lytOtp.visibility=View.VISIBLE
                    binding.btnSubmit.text="Verify Otp"
                }else{
                    binding.lytOtp.visibility=View.VISIBLE
                    binding.btnSubmit.text="Verify Otp"
                    showToast(this@QrCollectionActivity,response.message)
                }

            }

            override fun fail(from: String) {
                showToast(this@QrCollectionActivity,from)
            }
        })
    }

    private fun validate(): Boolean {
        if (!ActivityExtensions.isValidMobile(binding.edtMobile.text.toString())){
            binding.edtMobile.error="Enter Valid Mobile Number"
            return false
        }else if (binding.edtName.text.toString().isEmpty()){
            binding.edtName.error="Enter Customer Name"
            return false
        }else if (binding.edtAddress.text.toString().isEmpty()){
            binding.edtName.error="Enter Customer Address"
            return false
        }else if (!ActivityExtensions.isValidPinCode(binding.edtPinCode.text.toString())){
            binding.edtName.error="Enter Customer PinCode"
            return false
        } else if (binding.edtPurpose.text.toString().isEmpty()){
            binding.edtPurpose.error="Enter Withdrawal Purpose"
            return false
        }else if (binding.edtAmount.text.toString().isEmpty()){
            binding.edtAmount.error="Enter Withdrawal Amount"
            return false
        }else{
            val amt=binding.edtAmount.text.toString().toInt()
            return if (amt<100){
                showToast(this@QrCollectionActivity,"Amount Should Be Greater than 100")
                false
            }else if (amt>1000){
                showToast(this@QrCollectionActivity,"Amount Should Be Less than equals to 1000")
                false
            }else{
                true
            }
        }

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        startActivity(Intent(this@QrCollectionActivity, DashBoardActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        return true
    }

    override fun onClick(v: View?) {

    }

    private fun generateQRCode(text: String): Bitmap? {
        val size = 1024 // Size of the QR code
        val qrCodeWriter = QRCodeWriter()
        return try {
            val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) -0x1000000 else -0x1)
                }
            }
            bmp
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

}