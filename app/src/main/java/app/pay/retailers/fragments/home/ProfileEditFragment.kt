package app.pay.retailers.fragments.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.databinding.FragmentProfileEditBinding
import app.pay.retailers.helperclasses.MyGlide
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.pinSendOtp.PinSendOtpResponse
import app.pay.retailers.responsemodels.updateProfilePic.UpdateProfileImageResponse
import app.pay.retailers.responsemodels.uploadImage.UploadImageResponse
import app.pay.retailers.responsemodels.userid.UserIDResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import java.io.File


class ProfileEditFragment : BaseFragment<FragmentProfileEditBinding>(FragmentProfileEditBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var profile_Image = ""
    private val startForProfileImageResult =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    val fileUri = data?.data!!
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(myActivity.contentResolver, fileUri)
                    binding.ivUserProfileImage.setImageBitmap(bitmap)

                    val path: String = getRealPathFromURI(fileUri)
                    val file: File = File(path)
                    uploadImage(file)
                }

                ImagePicker.RESULT_ERROR -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(myActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(myActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

            }

        }

    private fun uploadImage(file: File) {
        myActivity.let {
            myActivity
            UtilMethods.uploadImage(myActivity, file, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: UploadImageResponse = Gson().fromJson(message, UploadImageResponse::class.java)
                    Toast.makeText(myActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    profile_Image = response.data?.url.toString()
                    addProfileImage()
                }

                override fun fail(from: String) {
                    Toast.makeText(myActivity, "Unable to Upload Image", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun addProfileImage() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["profile"] = profile_Image
        requestData["user_id"]=token
        UtilMethods.updateProfileImage(requireContext(), requestData,  object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response:UpdateProfileImageResponse=Gson().fromJson(message,UpdateProfileImageResponse::class.java)
                if (!response.error){
                    userSession.setData(Constant.PROFILE_PIC,profile_Image)
                    showToast(requireContext(),"Profile Image Saved Successfully")
                }else{
                    showToast(requireContext(),response.message)
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(),"Unable to Update Profile Image")
            }
        })
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

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getUserDetail()
    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener{
            getUserDetail()
                 }
        binding.ivEditImage.setOnClickListener {
            takeImage()
        }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.rlChangePassword.setOnClickListener {
            ShowDialog.changePassword(myActivity,userSession)
        }
        binding.rlChangePin.setOnClickListener {
            ShowDialog.changePin(myActivity,userSession)
        }
        binding.rlForgetPin.setOnClickListener {
            val token=userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.forgetPinSendOtp(requireContext(),token,object:MCallBackResponse{
                override fun success(from: String, message: String) {
                    val response:PinSendOtpResponse=Gson().fromJson(message,PinSendOtpResponse::class.java)
                    if (!response.error){
                        ShowDialog.forgetPin(myActivity,userSession)
                    }else{
                        showToast(requireContext(),response.message)
                    }

                }

                override fun fail(from: String) {
                    showToast(requireContext(),from)
                }
            })

        }
        binding.Certificate.setOnClickListener{
            findNavController().navigate(R.id.certificate)
        }

    }
    private fun getUserDetail() {
        binding.swipeRefreshLayout.isRefreshing = true
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getUserDetail(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: UserIDResponse =
                    Gson().fromJson(message, UserIDResponse::class.java)
                if (!response.error!!) {
                    binding.swipeRefreshLayout.isRefreshing = false
                    //userSession.setData(Constant.ID,response.data?._id.toString())
binding.Name.text=response.data.name
binding.Email.text=response.data.email
binding.MobileNumber.text=response.data.mobile
binding.LockedAmount.text=response.data.locking_amt
binding.PresentAddress.text=response.data.presentAddr
binding.State.text=response.data.state
binding.PinCode.text=response.data.pinCode
binding.referCode.text=response.data.refer_id

                } else {
                    binding.swipeRefreshLayout.isRefreshing = false

                }
            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false

            }
        })
    }

    override fun setData() {
        MyGlide.with(
            requireContext(),
            Uri.parse(Constant.PIMAGE_URL + userSession.getData(Constant.PROFILE_PIC).toString()),
            binding.ivUserProfileImage
        );
    }

}