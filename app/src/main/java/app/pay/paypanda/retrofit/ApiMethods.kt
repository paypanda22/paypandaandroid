package app.pay.paypanda.retrofit

import android.content.Context
import android.widget.Toast
import app.pay.paypanda.R
import app.pay.paypanda.helperclasses.CustomProgressBar
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.retrofit.UtilMethods.isNetworkAvailable
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ApiMethods {

    fun newForgetPasswordMobile(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .newForgetPasswordMobile(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {

            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun newForgetPasswordEmail(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .newForgetPasswordEmail(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun forgetPasswordOtpEmail(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .forgetPasswordOtpEmail(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun forgetPasswordOtpMobile(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .forgetPasswordOtpMobile(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }

    }

    fun registrationRequest(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .registrationRequest(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun generateOtpForTPin(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .generateOtpForTPin().execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun verifyTPinOtp(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyTPinOtp(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun createNewTPin(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .createNewTPin(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun transferAepsToMain(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .transferAepsToMain(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun resendSignupOTP(context: Context, token: String,obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .resendSignupOTP(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun mAtmInitiate(context: Context, token:String,obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .mAtmInitiate(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }
//tpin

    fun addTpin(context: Context, token:String,obj: Any, rsaendata:String,callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .addTpin(obj,rsaendata).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }
    fun changeTpin(
        context: Context,
        token: String,
        obj: Any,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .changeTpin(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun checkTpin(
        context: Context,
        token: String,
        userId: String,
        tpin: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .checkTpin(userId,tpin).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun otpfortpin(
        context: Context,
        token: String,
        userId: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .otpfortpin(userId).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }
    fun varifyOtpForTpin(
        context: Context,
        token: String,
        userId: String,
        otp: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .varifyOtpForTpin(userId,otp).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun resendotpfortpin(
        context: Context,
        token: String,
        userId: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .resendotpfortpin(userId).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun isTpin(
        context: Context,
        token: String,
        userId: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .isTpin(userId).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun recipientsListUpdate(
        context: Context,
        token: String,
        mobileNo: String,
        api_id: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .recipientsListUpdate(mobileNo,api_id).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

}