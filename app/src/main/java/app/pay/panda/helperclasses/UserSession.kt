package app.pay.panda.helperclasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import app.pay.panda.R
import app.pay.panda.activity.LoginRegistrationActivity
import app.pay.panda.responsemodels.login.Data
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.Constant.PREFER_NAME


class UserSession(private val context: Context) {
    private val pref: SharedPreferences =
        context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun isUserLoggedIn(): Boolean {
        return pref.getBoolean(Constant.IS_USER_LOGIN, false)
    }

    fun getData(id: String): String? {
        return pref.getString(id, null)
    }

    fun getIntData(id: String): Int {
        return pref.getInt(id, 0)
    }
    fun setIntData(id: String, value: Int): Boolean {
        return editor.putInt(id, value).commit()
    }

    fun getBoolData(id: String): Boolean {
        return pref.getBoolean(id, false)
    }

    fun getFloatData(id: String): Float {
        return pref.getFloat(id, 0.0F)
    }

    fun setData(id: String, value: String): Unit {
        editor.putString(id, value).commit()
    }

    fun setFloatData(id: String, value: Float): Unit {
        editor.putFloat(id, value).commit()
    }

    fun setBoolData(id: String, value: Boolean): Unit {
        editor.putBoolean(id, value).commit()
    }

    fun logoutUser(activity: Activity) {
        editor.clear().commit()
        val intent = Intent(activity, LoginRegistrationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Constant.FROM, "login")
        }
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    fun setLocationData(id: String, value: Double): Unit {
        editor.putString(id, value.toString()).apply()
    }

    fun getLocationData(id: String): Double {
        return pref.getString(id, "0.0")?.toDouble() ?: 0.0
    }

    fun createUserSession(data: Data):Boolean {
        val wasSaved = editor.apply {
            putBoolean(Constant.IS_USER_LOGIN, true)
            putString(Constant.NAME,data.name)
            putString(Constant.PAN_NAME,data.pan_name)
            putString(Constant.PAN_NUMBER,data.pan_number)
            putString(Constant.PROFILE_PIC,data.profile)
            putString(Constant.USER_TOKEN,data.user)
            putString(Constant.MAIN_WALET,data.main_wallet.toString())
            data.adhaarVerified?.let { putBoolean(Constant.ADHAAR_VERIFIED, it) }
            data.panVerified?.let { putBoolean(Constant.PAN_VERIFIED, it) }
            data.isIdentity_verified?.let { putBoolean(Constant.IDENTITY_VERIFIED, it) }
            data.is_gst?.let { putBoolean(Constant.IS_GST, it) }
            data.isGstAvailable?.let { putBoolean(Constant.GST_AVAILABLE, it) }
            data.is_bank?.let { putBoolean(Constant.ISBANK, it) }
            data.is_document?.let { putBoolean(Constant.ISDOCS, it) }
            data.is_approved?.let { putBoolean(Constant.ISAPPROVED, it) }
            data.is_kycVid?.let { putBoolean(Constant.ISVIDEO, it) }
            data.is_personalDetails?.let { putBoolean(Constant.ISPERSONALDETAILS, it) }
            data.is_self_declare?.let { putBoolean(Constant.ISSELFDECLARE, it) }
            putString(Constant.MOBILE,data.mobile)
            putString(Constant.EMAIL,data.email)
            putString(Constant.TPINSTATUS,data.Tpin_status)
            val loginStepList= listOf(
                data.isIdentity_verified,
                data.is_personalDetails,
                data.is_gst,
                data.is_bank,
                data.is_document,
                data.is_kycVid,
                data.is_self_declare
            )
            var loginSteps = 0
            for (step in loginStepList) {
                if (step == true) loginSteps++ else break
            }
            putInt(Constant.LOGIN_STEPS,loginSteps)
        }.commit()


        return wasSaved
    }
    fun setUserData(data: app.pay.panda.responsemodels.dashBoardData.Data):Boolean{
        val wasSaved = editor.apply {
            putBoolean(Constant.IS_USER_LOGIN, true)
            putString(Constant.NAME,data.name)
            putString(Constant.PAN_NAME,data.pan_name)
            putString(Constant.PAN_NUMBER,data.pan_number)
            putString(Constant.MOBILE,data.mobile)
            putString(Constant.EMAIL,data.email)
            putString(Constant.PROFILE_PIC,data.profile)
            putString(Constant.MAIN_WALET,data.main_wallet)
            putString(Constant.AEPS_WALLET,data.aeps_wallet)
            putString(Constant.COMMISSION_WALLET,data.commision_wallet)
            data.adhaarVerified?.let { putBoolean(Constant.ADHAAR_VERIFIED, it) }
            data.panVerified?.let { putBoolean(Constant.PAN_VERIFIED, it) }
            data.isIdentity_verified?.let { putBoolean(Constant.IDENTITY_VERIFIED, it) }
            data.is_gst?.let { putBoolean(Constant.IS_GST, it) }
            data.isGstAvailable?.let { putBoolean(Constant.GST_AVAILABLE, it) }
            data.is_bank?.let { putBoolean(Constant.ISBANK, it) }
            data.is_document?.let { putBoolean(Constant.ISDOCS, it) }
            data.is_approved?.let { putBoolean(Constant.ISAPPROVED, it) }
            data.is_kycVid?.let { putBoolean(Constant.ISVIDEO, it) }
            data.is_personalDetails?.let { putBoolean(Constant.ISPERSONALDETAILS, it) }
            data.is_self_declare?.let { putBoolean(Constant.ISSELFDECLARE, it) }
            data.notification?.let { putInt(Constant.NOTIFICATION_COUNT, it) }
            putString(Constant.TPINSTATUS,data.Tpin_status)
            putString(Constant.BUSINESS_NAME,data.business_name.toString())
            putString(Constant.USER_TYPE,data.user_type_id?.user_type.toString())
            putString(Constant.USER_TYPE_ID,data.user_type_id?._id.toString())

            val loginStepList= listOf(
                data.isIdentity_verified,
                data.is_personalDetails,
                data.is_gst,
                data.is_bank,
                data.is_document,
                data.is_kycVid,
                data.is_self_declare
            )
            var loginSteps = 0
            for (step in loginStepList) {
                if (step == true) loginSteps++ else break
            }
            putInt(Constant.LOGIN_STEPS,loginSteps)
        }.commit()
        return wasSaved
    }

}