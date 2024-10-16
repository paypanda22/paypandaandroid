package app.pay.paypanda.helperclasses

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.MyClick
import app.pay.paypanda.interfaces.OnClick
import app.pay.paypanda.responsemodels.checkFinger.CheckFingerPrintResponse
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson

class ScanFinger(
    private val activity: Activity,
    private val userSession: UserSession,
    private val launcher: ActivityResultLauncher<Intent>
) {
    fun yourDevicePackage(devicename: String) {
        val devicePackageName = devicename
        var pidOption = ""
        pidOption = if (devicename.equals("com.mantra.mis100v2.rdservice", ignoreCase = true)) {
            "<PidOptions ver=\"1.0\"><Opts fCount=\"0\" fType=\"1\" iCount=\"1\" iType=\"0\" pCount=\"0\" pType=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"\" posh=\"UNKNOWN\" env=\"PP\"/></PidOptions>"
        } else {
            "<PidOptions ver=\"1.0\"><Opts fCount=\"1\" fType=\"2\" iCount=\"0\" iType=\"0\" pCount=\"0\" pType=\"0\" format=\"0\" pidVer=\"2.0\" timeout=\"10000\" otp=\"\" wadh=\"\" posh=\"UNKNOWN\"/></PidOptions>"
        }

        // Toast.makeText(activity, "DevicePackageName is "+devicename, Toast.LENGTH_SHORT).show();
        getPid(pidOption, devicename)
    }

    private fun getPid(pidOption: String, devicename: String) {
        val i = Intent()
        i.setPackage(devicename)
        i.setAction("in.gov.uidai.rdservice.fp.CAPTURE")
        i.putExtra("PID_OPTIONS", pidOption)
        launcher.launch(i)
    }

    fun validateFingerPrint(fData: String,click:OnClick) {
        UtilMethods.checkFData(activity,fData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:CheckFingerPrintResponse=Gson().fromJson(message,CheckFingerPrintResponse::class.java)
                if (response.error){
                    ShowDialog.bottomDialogSingleButton(activity,"Finger Scan Failed",response.message,"finger",object :MyClick{
                        override fun onClick() {

                        }
                    })
                }else{
                   ShowDialog.scanResult(activity,response.data.qScore.toInt(),object:MyClick{
                       override fun onClick() {
                           click.onButtonClick()
                       }
                   })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(activity,"Finger Scan Failed",from,"finger",object :MyClick{
                    override fun onClick() {

                    }
                })
            }
        })
    }


}