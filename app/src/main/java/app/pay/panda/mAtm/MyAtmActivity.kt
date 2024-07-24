package app.pay.panda.mAtm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import app.pay.panda.BaseActivity
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.databinding.ActivityMatmBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import com.google.gson.Gson
import com.sdk.matmsdk.ui.MatmActivity
import com.sdk.matmsdk.utils.MATMOnFinishListener
import com.sdk.matmsdk.utils.MatmSdkConstants
import org.json.JSONObject


class MyAtmActivity : BaseActivity<ActivityMatmBinding>(), MATMOnFinishListener {
    private lateinit var userSession: UserSession

    override fun getViewBinding(): ActivityMatmBinding = ActivityMatmBinding.inflate(layoutInflater)
//    private val startForScannerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        val resultCode = result.resultCode
//        val data = result.data
//
//        showToast(this@MAtmActivity, "SAS")
//
//
//    }

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@MyAtmActivity)
        MatmSdkConstants.MATMOnFinishListener = this

    }

    override fun addListeners() {
        binding.btnProceed.setOnClickListener {
            val themeColor = ThemeColor(buttonBgColor = "#8bc43f", buttonTextColor = "#FFFFFF")
            val dataModel = DataModel(
                transactionAmount = 0,
                transactionType = "0",
                shop_name = "Saurabh Mobile",
                brand_name = "PayPanda",
                applicationType = "android",
                paramB = "",
                paramC = "",
                device_type = "morefun",
                device_name = "morefun",
                apiUserName = "upitestret",
                user_mobile_number = "9412591166",
                userName = "Saurabh",
                clientRefID = "PAYP98745698SWER343455SSQSA",
                clientID = "42Zuw71Ok7e2TGAgHPKttM7PFGMspJLLy3ewq15dhgjtGM9l",
                clientSecret = "MDB9krmA8OqYdgjTKflkXXU7BTNAJgVDEWBmhWjQ8YBvAPNKNPLbxnJGSKcKiEV9",
                loginID = "upitestret",
                skipReceipt = true,
                themeColor = themeColor
            )
            val response = Gson().toJson(dataModel)

            val intent = Intent(this@MyAtmActivity, MatmActivity::class.java)
            intent.putExtra("data", response)
            startActivity(intent)
        }

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        startActivity(Intent(this@MyAtmActivity, DashBoardActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        return true
    }

    override fun onClick(v: View?) {

    }

    override fun onMatmSDKFinish(statusTxt: String?, clientRefID: String?, remainingAmount: String?, statusDesc: String?, jsonObject: JSONObject?) {
        showToast(this@MyAtmActivity, "Finish")
        showToast(this@MyAtmActivity, statusTxt + clientRefID + statusDesc + jsonObject)
        Log.e( "transactionData", jsonObject.toString() )
    }


}