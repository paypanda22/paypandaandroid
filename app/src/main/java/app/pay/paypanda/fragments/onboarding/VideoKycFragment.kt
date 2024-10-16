package app.pay.paypanda.fragments.onboarding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.databinding.FragmentVideoKycBinding
import app.pay.paypanda.helperclasses.ShowDialog
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.MyClick
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class VideoKycFragment : BaseFragment<FragmentVideoKycBinding>(FragmentVideoKycBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var videoCaptureLauncher: ActivityResultLauncher<Intent>
    @SuppressLint("Recycle")
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        videoCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { videoUri ->
                    val inputStream: InputStream? = requireContext().contentResolver.openInputStream(videoUri)
                    val videoFile = File(requireContext().cacheDir, "recorded_video.mp4")

                    try {
                        inputStream.use { input ->
                            FileOutputStream(videoFile).use { output ->
                                val buffer = ByteArray(1024)
                                var read: Int
                                if (input != null) {
                                    while (input.read(buffer).also { read = it } != -1) {
                                        output.write(buffer, 0, read)
                                    }
                                }
                                output.flush()
                            }
                        }
                        // Directly use videoFile for upload
                        uploadVideo(videoFile)
                    } catch (e: IOException) {
                        // Handle the error, maybe notify the user or log the issue
                    }
                }
            }
        }


    }

    private fun uploadVideo(file: File) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.uploadKycVideo(requireContext(),file,token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                userSession.setBoolData(Constant.ISVIDEO,true)
                val userType = userSession.getData(Constant.USER_TYPE_NAME)
                if(userType.equals("zsm")|| userType.equals("asm")){
                    userSession.setIntData(Constant.LOGIN_STEPS_ZSM,3)
                }else{
                    userSession.setIntData(Constant.LOGIN_STEPS,6)
                }
                ShowDialog.bottomDialogSingleButton(myActivity,
                    "Video Uploaded Successfully",
                    "Video Kyc is Completed, Now Proceed to Self Declaration","success",object:MyClick{
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
            }

            override fun fail(from: String) {
                userSession.setBoolData(Constant.ISVIDEO,true)
                val userType = userSession.getData(Constant.USER_TYPE_NAME)
                if(userType.equals("zsm")|| userType.equals("asm")){
                    userSession.setIntData(Constant.LOGIN_STEPS_ZSM,3)
                }else{
                    userSession.setIntData(Constant.LOGIN_STEPS,6)
                }
                /*ShowDialog.bottomDialogSingleButton(myActivity,
                    "Video Uploaded Successfully",
                    "Video Kyc is Completed, Now Proceed to Self Declaration","success",object:MyClick{
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })*/
                Toast.makeText(myActivity, from, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.btnSubmit.setOnClickListener{
            dispatchTakeVideoIntent()
        }

    }

    private fun dispatchTakeVideoIntent() {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30)
        videoCaptureLauncher.launch(takeVideoIntent)
    }

    override fun setData() {

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