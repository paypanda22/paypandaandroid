package app.pay.paypanda.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.pay.paypanda.BaseActivity
import app.pay.paypanda.R
import app.pay.paypanda.databinding.ActivitySacnQrActitivyBinding
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.ReaderException
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.io.IOException
import java.io.InputStream

class SacnQrActitivy : BaseActivity<ActivitySacnQrActitivyBinding>(), View.OnClickListener {

    private lateinit var scanBtn: Button
    private lateinit var selectImageBtn: Button
    private lateinit var ivBack: ImageView
    private val REQUEST_IMAGE_PICK = 1

    override fun getViewBinding(): ActivitySacnQrActitivyBinding {
        return ActivitySacnQrActitivyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sacn_qr_actitivy)

        // Initialize views
        scanBtn = findViewById(R.id.scanBtn)
        selectImageBtn = findViewById(R.id.selectImageBtn)
        ivBack = findViewById(R.id.ivBack)

        // Set onClick listeners
        scanBtn.setOnClickListener(this)
        ivBack.setOnClickListener { finish() }

        selectImageBtn.setOnClickListener {
            // Launch an intent to pick an image from the gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        // Initialize your logic here if needed
    }

    override fun addListeners() {
     binding.ivBack.setOnClickListener{
         startActivity(Intent(this@SacnQrActitivy, DashBoardActivity::class.java))
         overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)


     }
    }

    override fun setData() {
        // Set data if needed
    }

    override fun handleBackPressCustom(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        // Create and configure the IntentIntegrator for QR code scanning from the camera
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setPrompt("Scan a barcode or QR Code")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Handle image selection for QR code decoding
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let { uri ->
                try {
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val result = decodeQRCode(bitmap)
                    if (result != null) {
                        Toast.makeText(this, "QR Code: $result", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No QR code found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle QR code scanning from the camera
            val intentResult: IntentResult? =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (intentResult != null) {
                if (intentResult.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("QRCodeScan", "Scan result: ${intentResult.contents}")
                    Log.d("QRCodeScan", "Scan format: ${intentResult.formatName}")

                    Toast.makeText(this, "Scan result: ${intentResult.contents}", Toast.LENGTH_SHORT).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun decodeQRCode(bitmap: Bitmap): String? {
        // Convert Bitmap to int array
        val width = bitmap.width
        val height = bitmap.height
        val intArray = IntArray(width * height)
        bitmap.getPixels(intArray, 0, width, 0, 0, width, height)

        // Create RGBLuminanceSource from int array
        val source = RGBLuminanceSource(width, height, intArray)
        val binarizer = HybridBinarizer(source)
        val binaryBitmap = BinaryBitmap(binarizer)

        // Create a reader and decode
        val reader = MultiFormatReader()
        return try {
            val result = reader.decode(binaryBitmap)
            result.text
        } catch (e: ReaderException) {
            e.printStackTrace()  // Log exception for debugging
            null
        } catch (e: Exception) {
            e.printStackTrace()  // Log exception for debugging
            null
        }
    }
}
