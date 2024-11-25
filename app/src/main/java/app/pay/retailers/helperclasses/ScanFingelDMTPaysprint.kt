package app.pay.retailers.helperclasses

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

class ScanFingelDMTPaysprint (
    private val activity: Activity,
    private val userSession: UserSession,
    private val launcher: ActivityResultLauncher<Intent>
) {
    fun yourDevicePackage(devicename: String) {
        val devicePackageName = devicename
        var pidOption = ""
        pidOption = if (devicename.equals("com.mantra.mis100v2.rdservice", ignoreCase = true)) {
            "<PidOptions ver=\"1.0\"><Opts fCount=\"0\" fType=\"1\" iCount=\"1\" iType=\"0\" pCount=\"0\" pType=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"18f4CEiXeXcfGXvgWA/blxD+w2pw7hfQPY45JMytkPw=\" posh=\"UNKNOWN\" env=\"PP\"/></PidOptions>"
        } else {
            "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"2\" iCount=\"0\" iType=\"0\" pCount=\"0\" pType=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"18f4CEiXeXcfGXvgWA/blxD+w2pw7hfQPY45JMytkPw=\" posh=\"UNKNOWN\"/></PidOptions>"
        }

        getPid(pidOption, devicename)
    }

    private fun getPid(pidOption: String, devicename: String) {
        val i = Intent()
        i.setPackage(devicename)
        i.setAction("in.gov.uidai.rdservice.fp.CAPTURE")
        i.putExtra("PID_OPTIONS", pidOption)
        launcher.launch(i)
    }
}