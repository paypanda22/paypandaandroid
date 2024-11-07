package app.pay.pandapro

import android.content.Intent
import android.os.Bundle

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import app.pay.pandapro.activity.ActivityCheckPermissions
import app.pay.pandapro.helperclasses.ActivityExtensions
import app.pay.pandapro.helperclasses.AllPermissions
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.dashBoardData.DashBoardData
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), View.OnClickListener {


    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // This method is for subclasses to provide their view binding
    abstract fun getViewBinding(): VB

    // This method can be used if a subclass prefers to use a layout resource ID
    protected open fun setLayout(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Apply fullscreen To All Activities
        ActivityExtensions.fullscreen(this)

        // Checking permissions
        try {
            val permissions = AllPermissions(this, UserSession(this@BaseActivity))
            val plist = permissions.checkPermissions()
            if (plist.isNotEmpty()) {
                val firstPermission = plist[0]
                startActivity(Intent(this, ActivityCheckPermissions::class.java).apply {
                    putExtra("permission", firstPermission.name)
                    putExtra("type", firstPermission.type)
                })
            } else {
                _binding = getViewBinding()
                val layoutResId = setLayout()

                if (_binding != null) {
                    setContentView(_binding!!.root)
                } else if (layoutResId != null) {
                    setContentView(layoutResId)
                } else {
                    throw IllegalStateException("Must provide a layout resource ID or a view binding")
                }
//                val developerMode = Settings.Global.getInt(
//                    contentResolver,
//                    Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
//                )
//                if (developerMode == 1) {
//                    ShowDialog.bottomDialogSingleButton(this@BaseActivity,
//                        "Un-Authorised Phone State",
//                        "Please turn off the DEBUG Mode & use the latest APP from Play Store", "error", object : MyClick {
//                            override fun onClick() {
//                                finishAffinity()
//                            }
//                        })
//                } else {
                init(savedInstanceState)
                addListeners()
                setData()
                getDashBoardData()
              //  }


                //Handle BackPress
                // Handle back press
                val onBackPressedCallback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (!handleBackPressCustom()) {
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                }
                onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any unexpected exceptions here
        }
    }

    private fun getDashBoardData() {
        val userSession=UserSession(this@BaseActivity)
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.dashBoardData(this@BaseActivity, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DashBoardData = Gson().fromJson(message, DashBoardData::class.java)
                response.data?.let { userSession.setUserData(it) }
            }

            override fun fail(from: String) {
            }
        })
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    protected abstract fun addListeners()

    protected abstract fun setData()

//    @Deprecated("Deprecated app Java")
//    override fun onBackPressed() {
//        if (!handleBackPress()) {
//            super.onBackPressed()
//        }
//    }

    abstract fun handleBackPressCustom(): Boolean

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // To avoid memory leaks
    }


}



