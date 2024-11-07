package app.pay.pandapro.fragments.onboarding

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View.GONE
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.databinding.FragmentUploadDocsBinding
import app.pay.pandapro.helperclasses.ShowDialog
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.interfaces.MyClick
import app.pay.pandapro.responsemodels.submitdocs.SubmitDocsResponse
import app.pay.pandapro.responsemodels.uploadImage.UploadImageResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import java.io.File


class UploadDocsFragment : BaseFragment<FragmentUploadDocsBinding>(FragmentUploadDocsBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var fileType = ""
    private var adhaar_front_card = ""
    private var adhaar_back_card = ""
    private var pan_card = ""
    private var gst = ""
    private var bank_proof = ""
    private var shop_outside_photo = ""
    private var shop_internal_photo = ""
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(myActivity.contentResolver, fileUri)

                    when (fileType) {
                        "AadhaarFront" -> {
                            binding.ivFront.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "AadhaarBack" -> {
                            binding.ivAadhaarBack.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "pan" -> {
                            binding.ivPanCard.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "bankProof" -> {
                            binding.ivBankProof.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "gst" -> {
                            binding.ivGst.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "shopFront" -> {
                            binding.ivShopF.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "shopInside" -> {
                            binding.ivShopI.setImageBitmap(bitmap)

                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        else -> {
                            Toast.makeText(requireContext(), "Invalid Document Type Selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(myActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(myActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

            }

        }

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        if (!userSession.getBoolData(Constant.GST_AVAILABLE)) {
            binding.mcvGst.visibility = GONE
        }

    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rlBottom.setOnClickListener {
            if (adhaar_front_card.isBlank()) {
                Toast.makeText(requireContext(), "Upload Aadhaar Front", Toast.LENGTH_SHORT).show()
            } else if (adhaar_back_card.isBlank()) {
                Toast.makeText(requireContext(), "Upload Aadhaar Back", Toast.LENGTH_SHORT).show()
            } else if (pan_card.isBlank()) {
                Toast.makeText(requireContext(), "Upload Pan Card", Toast.LENGTH_SHORT).show()
            } else if (bank_proof.isBlank()) {
                Toast.makeText(requireContext(), "Upload Bank Proof", Toast.LENGTH_SHORT).show()
            } else if (shop_internal_photo.isBlank()){
                Toast.makeText(requireContext(), "Upload Shop's Front View", Toast.LENGTH_SHORT).show()
            }else if (shop_outside_photo.isBlank()){
                Toast.makeText(requireContext(), "Upload Shop's Internal View", Toast.LENGTH_SHORT).show()
            } else {
                if (userSession.getBoolData(Constant.GST_AVAILABLE)) {
                    if (gst.isBlank()) {
                        Toast.makeText(requireContext(), "Upload GST Certificate", Toast.LENGTH_SHORT).show()
                    } else {
                        submitDocs()
                    }
                } else {
                    submitDocs()
                }
            }

        }
        binding.ivUploadFront.setOnClickListener {
            fileType = "AadhaarFront"
            takeImage()
        }
        binding.ivUploadBack.setOnClickListener {
            fileType = "AadhaarBack"
            takeImage()
        }
        binding.ivUploadPan.setOnClickListener {
            fileType = "pan"
            takeImage()
        }
        binding.ivUploadBank.setOnClickListener {
            fileType = "bankProof"
            takeImage()
        }
        binding.ivUploadGst.setOnClickListener {
            fileType = "gst"
            takeImage()
        }
        binding.ivUploadShopFront.setOnClickListener {
            fileType = "shopFront"
            takeImage()
        }
        binding.ivUploadShopInside.setOnClickListener {
            fileType = "shopInside"
            takeImage()
        }


    }

    private fun submitDocs() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        val requestData = hashMapOf<String, Any>()
        requestData["adhaar_front_card"] = adhaar_front_card
        requestData["adhaar_back_card"] = adhaar_back_card
        requestData["pan_card"] = pan_card
        requestData["gst"] = gst
        requestData["bank_proof"] = bank_proof
        requestData["shop_internal_photo"] = shop_internal_photo
        requestData["shop_outside_photo"] = shop_outside_photo
        requestData["longitude"]=userSession.getData(Constant.M_LONG).toString()
        requestData["latitude"]=userSession.getData(Constant.M_LAT).toString()
        requestData["user_id"] = token
        UtilMethods.submitDocs(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: SubmitDocsResponse = Gson().fromJson(message, SubmitDocsResponse::class.java)
                userSession.setBoolData(Constant.ISDOCS, true)
                userSession.setIntData(Constant.LOGIN_STEPS, 5)
                ShowDialog.bottomDialogSingleButton(
                    myActivity,
                    "SUCCESS",
                    "Documents Submitted Successfully. Proceed to Next Step",
                    "success",
                    object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "Error in Submitting Documents", from, "error", object : MyClick {
                    override fun onClick() {
                        findNavController().popBackStack()
                    }
                })
            }
        })

    }

    override fun setData() {

    }

    private fun uploadImage(file: File) {
        myActivity.let {
            myActivity
            UtilMethods.uploadImage(myActivity, file, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: UploadImageResponse = Gson().fromJson(message, UploadImageResponse::class.java)
                    Toast.makeText(myActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    if (fileType == "AadhaarFront") {
                        adhaar_front_card = response.data?.url.toString()
                    } else if (fileType == "AadhaarBack") {
                        adhaar_back_card = response.data?.url.toString()
                    } else if (fileType == "pan") {
                        pan_card = response.data?.url.toString()
                    } else if (fileType == "bankProof") {
                        bank_proof = response.data?.url.toString()
                    } else if (fileType == "gst") {
                        gst = response.data?.url.toString()
                    } else if (fileType == "shopFront") {
                        shop_outside_photo = response.data?.url.toString()
                    } else if (fileType == "shopInside") {
                        shop_internal_photo = response.data?.url.toString()
                    } else {
                        Toast.makeText(requireContext(), "Invalid Document Type Selected", Toast.LENGTH_SHORT).show()
                    }
                    markActive()


                }

                override fun fail(from: String) {
                    Toast.makeText(myActivity, "Unable to Upload Image", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun markActive() {
        if (adhaar_front_card.isNotEmpty()) {
            binding.mcvAadhaarFront.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }
        if (adhaar_back_card.isNotEmpty()) {
            binding.mcvAadhaarBack.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }
        if (pan_card.isNotEmpty()) {
            binding.mcvPanCard.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }
        if (bank_proof.isNotEmpty()) {
            binding.mcvBankProof.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }
        if (gst.isNotEmpty()) {
            binding.mcvGst.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }

        if (shop_outside_photo.isNotEmpty()) {
            binding.mcvShopFront.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }
        if (shop_internal_photo.isNotEmpty()) {
            binding.mcvShopInternal.setCardBackgroundColor(ContextCompat.getColor(myActivity, R.color.colorPrimary))
        }

    }

    private fun takeImage() {
        ImagePicker.with(this)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .crop(1f, 1f)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String
        val cursor = myActivity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result

    }

}