package app.pay.retailers.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

import app.pay.retailers.BaseActivity2
import app.pay.retailers.R
import app.pay.retailers.databinding.ActivityCheckPermissionsBinding
import app.pay.retailers.helperclasses.UserSession


class ActivityCheckPermissions : BaseActivity2<ActivityCheckPermissionsBinding>() {
    private lateinit var reqPermission:String
    private lateinit var reqType:String
    private var reqCount:Int=0
    private lateinit var userSession: UserSession
    private val checkPermissionCode:Int =9718

    override fun getViewBinding(): ActivityCheckPermissionsBinding =ActivityCheckPermissionsBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession= UserSession(this@ActivityCheckPermissions)
        reqPermission= intent.getStringExtra("permission").toString()
        reqType= intent.getStringExtra("type").toString()

    }

    override fun addListeners() {
        binding.bottomBar.setOnClickListener{
            requestPermissions(arrayOf(reqPermission), checkPermissionCode)
        }

    }

    override fun setData() {
        when (reqType) {
            "camera" -> {
                binding.ivServiceIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@ActivityCheckPermissions,
                        R.drawable.camera_color
                    )
                )
                binding.tvPermissionName.setText(R.string.camera_permission)
                binding.tvPermissionDesc.setText(R.string.camera_permissions_text)
            }
            "location" -> {
                binding.ivServiceIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@ActivityCheckPermissions,
                        R.drawable.location_color
                    )
                )
                binding.tvPermissionName.setText(R.string.location_permission)
                binding.tvPermissionDesc.setText(R.string.location_permissions_text)
            }
            "Notification" -> {
                binding.ivServiceIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@ActivityCheckPermissions,
                        R.drawable.notification
                    )
                )
                binding.tvPermissionName.setText(R.string.phone_number_permission)
                binding.tvPermissionDesc.setText(R.string.phone_number_permissions_text)
            }
            "backgroundLocation"->{
                binding.ivServiceIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@ActivityCheckPermissions,
                        R.drawable.background_location
                    )
                )
                binding.tvPermissionName.setText(R.string.access_background_location)
                binding.tvPermissionDesc.setText(R.string.access_background_location_text)
            }
        }
    }

    override fun handleBackPress(): Boolean {
       return true
    }

    override fun onClick(v: View?) {

    }

    private fun sendtoapp(){
        startActivity(Intent(this@ActivityCheckPermissions,LoginRegistrationActivity::class.java))
    }

//    private fun navigateToActivity(activityClass: Class<*>) {
//        if (userSession.isUserLoggedIn()){
//            var role=userSession.getData(Constant.USER_TYPE)
//            val intent = Intent(this@ActivityCheckPermissions, if (role == "Owner") AdminActivity::class.java else EmployeeActivity::class.java)
//                .putExtra("from", "login")
//            startActivity(intent)
//            finish()
//
//        }else{
//            startActivity(Intent(this, activityClass)
//                .putExtra("from", "login")
//                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//            finish()
//        }
//
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            checkPermissionCode -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendtoapp()
                } else {
                    Toast.makeText(this, "Background location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            // Handle other permission results if necessary
        }
    }

}