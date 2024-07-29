package app.pay.panda.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import app.pay.panda.R
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.interfaces.MCallBackResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File


object UtilMethods {
     fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun chkMobile(context: Context, mobileNo: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .chkMobile(mobileNo).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun chkEmail(context: Context, email: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .chkEmail(email).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun userLogin(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .userLogin(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun sendOtpMobile(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .sendOtpMobile(obj).execute()
                    withContext(Dispatchers.Main) {

                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun verifyOtp(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyOtp(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun sendOtpEmail(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .sendOtpEmail(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun verifyOtpEmail(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyOtpEmail(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun checkSponsorCode(context: Context, referID: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .checkSponsorCode(referID).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun register(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .register(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun dashBoardData(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .dashBoardData().execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun dashBoardData2(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .dashBoardData().execute()
                    withContext(Dispatchers.Main) {
                        // progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

                        } else {
                            // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    // progressBar.hideProgress()
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

    fun verifyPan(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyPan(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun aadhaarSendOTP(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aadhaarSendOTP(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun aadhaarVerifyOtp(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aadhaarVerifyOtp(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun getStateList(context: Context, countryID: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getStateList(countryID).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun getCountryList(context: Context, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getCountryList().execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun updatePersonalDetails(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .updatePersonalDetails(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun verifyGst(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyGst(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun updateGstDetails(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .updateGstDetails(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun verifyBankAccount(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyBankAccount(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun uploadImage(context: Context, file: File, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                    val imageRequest = MultipartBody.Part.createFormData("image", file.name, requestBody)

                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .uploadImage(imageRequest).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            Log.e("TAG", "uploadImage: " + response.body().toString())
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

                        } else {
                            progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        callBackResponse.fail(e.message ?: "Unknown Error")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun submitDocs(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .submitDocs(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun dmtBankList(context: Context, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .dmtBankList().execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun saveBankDetails(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .saveBankDetails(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun uploadKycVideo(context: Context, file: File, token: String, callBackResponse: MCallBackResponse) {
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
            return
        }

        val progressBar = CustomProgressBar().apply { showProgress(context) }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val requestBody = RequestBody.create("video/mp4".toMediaTypeOrNull(), file)
                val videoRequest = MultipartBody.Part.createFormData("video", file.name, requestBody)

                val tokenP = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    token
                )

                val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                    .uploadKycVideo(videoRequest, tokenP).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body() as JsonObject
                        if (responseBody.has("error")) {
                            val status = responseBody.get("error").asBoolean
                            if (status) {
                                val message = responseBody.get("message").asString
                                callBackResponse.fail(message)
                            } else {
                                callBackResponse.success("strresponse", responseBody.toString())
                            }
                        } else {
                            val message = responseBody.get("message").asString ?: "Unknown Error"
                            callBackResponse.fail(message)
                        }
                    } else {
                        // Handle failure
                        callBackResponse.fail("Request failed: ${response.message()}")
                    }
                    progressBar.hideProgress()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callBackResponse.fail(e.message ?: "Unknown error")
                    progressBar.hideProgress()
                }
            }
        }
    }

    fun selfDeclaration(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .selfDeclaration(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun getDmtCustomerInfo(context: Context, mobile: String, token: String, apiId: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getDmtCustomerInfo(mobile, apiId).execute()
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

    fun getBeneficiaryList(context: Context, mobile: String, token: String, apiId: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getBeneficiaryList(mobile, apiId).execute()
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

    fun dmtMakeTransaction(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .dmtMakeTransaction(obj).execute()
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

    fun dmtTransactionList(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .dmtTransactionList(obj).execute()
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

    fun createCustomer(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .createCustomer(obj).execute()
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

    fun resendCustomerOtp(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .resendCustomerOtp(obj).execute()
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

    fun verifyCustomerOtp(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyCustomerOtp(obj).execute()
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

    fun addEkoRecipient(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .addEkoRecipient(obj).execute()
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

    fun getOperator(context: Context, categoryID: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getOperator(categoryID).execute()
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

    fun dmtTxnEnquiry(context: Context, txID: String, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .dmtTxnEnquiry(txID).execute()
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

    fun dmtInitiateRefund(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .dmtInitiateRefund(obj).execute()
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

    fun getTxnByBatchId(context: Context, batchId: String, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getTxnByBatchId(batchId).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {

                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {

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

    fun passwordCheck(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .passwordCheck(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun forgetPassword(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .forgetPassword(obj).execute()
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

    fun getCompanyBankList(context: Context, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getCompanyBankList().execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun getDmtSettings(context: Context, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getDmtSettings().execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun getPinCodeData(context: Context, pincode: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitYooPayInstance().create(GetData::class.java)
                        .getPinCodeData(pincode).execute()
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
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun processDmtRefundWithOtp(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .processDmtRefundWithOtp(obj).execute()
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
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun walletTxnReport(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .walletTxnReport(obj).execute()
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
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed.API ERROR")
                    }
                }
            }
        } else {
            Toast.makeText(context, R.string.check_internet, Toast.LENGTH_SHORT).show()
        }
    }

    fun getTransferTo(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getTransferTo().execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun addPaymentRequest(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .addPaymentRequest(obj).execute()
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

    fun walletRequestList(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .walletRequestList(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
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

    fun isServiceAvailable(context: Context, serviceID: String, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .isServiceAvailable(serviceID).execute()
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

    fun aepsAuthRegister(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsAuthRegister(obj).execute()
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

    fun aepsDailyAuth(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsDailyAuth(obj).execute()
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

    fun aepsBankList(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .aepsBankList().execute()
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

    fun checkFData(context: Context, fData: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitYooPayInstance().create(GetData::class.java)
                        .checkFData(fData).execute()
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

    fun merchantCwAuth(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .merchantCwAuth(obj).execute()
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

    fun aepsCashWithdrawal(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsCashWithdrawal(obj).execute()
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

    fun getUserAadhaarAddress(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getUserAadhaarAddress().execute()
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

    fun aepsBalanceEnquiry(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsBalanceEnquiry(obj).execute()
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

    fun aepsMiniStatement(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsMiniStatement(obj).execute()
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

    fun aepsAadhaarPay(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsAadhaarPay(obj).execute()
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

    fun aepsTransactionList(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsTransactionList(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            //progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            //progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    //progressBar.hideProgress()
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

    fun updateProfileImage(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .updateProfileImage(obj).execute()
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
                    //progressBar.hideProgress()
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

    fun changePassword(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .changePassword(obj).execute()
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

    fun changePin(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .changePin(obj).execute()
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

    fun forgetPinSendOtp(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .forgetPinSendOtp().execute()
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

    fun forgetTransactionPin(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .forgetTransactionPin(obj).execute()
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

    fun getNotifications(context: Context, count: String, readed: Boolean, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getNotifications(count, readed).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            //  progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    // progressBar.hideProgress()
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

    fun getBbpsOperators(context: Context, catID: String, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getBbpsOperators("1", catID).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            //  progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    // progressBar.hideProgress()
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

    fun fetchBill(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .fetchBill(obj).execute()
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

    fun payBill(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .payBill(obj).execute()
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

    fun getDisputeMasters(context: Context, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getDisputeMasters().execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            // progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    //  progressBar.hideProgress()
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

    fun addSupportTicket(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .addSupportTicket(obj).execute()
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

    fun getSupportTicketList(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getSupportTicketList(obj).execute()
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

    fun replyToTicket(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .replyToTicket(obj).execute()
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

    fun getUtilityTransactions(
        context: Context,
        token: String,
        start_date: String,
        end_date: String,
        biller_id: String,
        page: String,
        count: String,
        category_id: String,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getUtilityTransactions(start_date, end_date, biller_id, page, count, category_id).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            // progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    // progressBar.hideProgress()
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

    fun getPackageList(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getPackageList().execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            // progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    // progressBar.hideProgress()
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

    fun purchasePackage(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .purchasePackage(obj).execute()
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

    fun getCmsUrl(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getCmsUrl(obj).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            //progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                            //progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    //progressBar.hideProgress()
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

    fun getRechargeOperators(context: Context, serviceID: String, token:String,callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getRechargeOperators(serviceID).execute()
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

    fun getNumberDetails(context: Context, mobile: String,token:String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getNumberDetails(mobile).execute()
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

    fun getRechargePlans(context: Context, mobile: String,token:String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
//            val progressBar = CustomProgressBar()
//            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getRechargePlans(mobile).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                           // progressBar.hideProgress()
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {
                           // progressBar.hideProgress()
                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {
                    //progressBar.hideProgress()
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

    fun processRecharge(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .processRecharge(obj).execute()
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
    fun getDthInfo(context: Context,operatorCode:String, mobile: String,token:String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getDthInfo(operatorCode,mobile).execute()
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

    fun aepsCashDeposit(context: Context,obj:Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .aepsCashDeposit(obj).execute()
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

    fun cashDepositBanks(context: Context, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .cashDepositBanks().execute()
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

    fun quickDhanSendOtp(context: Context,obj:Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .quickDhanSendOtp(obj).execute()
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

    fun getPayoutBankList(context: Context, token:String,callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getPayoutBankList().execute()
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

    fun getRegistrationOtp(context: Context,obj:Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .getRegistrationOtp(obj).execute()
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

    fun verifyRegisterOtp(context: Context,obj:Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .verifyRegisterOtp(obj).execute()
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

     fun newLoginOtp(context: Context,obj:Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .newLoginOtp(obj).execute()
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


    fun bbpsCategoryList(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .utilityReport().execute()
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

    fun getBbpsServiceId(context: Context, serviceId: String, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .getBbpsServiceId(serviceId).execute()
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

    fun userType(context: Context, token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .userType().execute()
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

    fun noBoardingRequest(context: Context, obj: JsonObject, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .obBoardingRequest(obj).execute()
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

    fun walletRequestListDist(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .walletRequestListDist(obj).execute()
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

    fun savePayoutDetails(context: Context, obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstance().create(GetData::class.java)
                        .savePayoutDetails(obj).execute()
                    withContext(Dispatchers.Main) {
                        progressBar.hideProgress()
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error")) {
                                val status = responseBody.get("error").asBoolean
                                if (status) {
                                    val message = responseBody.get("message").asString
                                    callBackResponse.fail(message)
                                } else {
                                    callBackResponse.success("strresponse", responseBody.toString())
                                }
                            } else {
                                val message = responseBody.get("message").asString ?: "Unknown Error"
                                callBackResponse.fail(message)
                            }

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

    fun getTxnByUtilityId(context: Context,id:String,token: String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .getTxnByUtilityId(id).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {

                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {

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


    fun getNetwork(context: Context,token: String, page:String,count:String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .getNetwork(page,count).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {

                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {

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
    fun getNetworkRetailer(context: Context,token: String, id:String, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token)
                        .create(GetData::class.java)
                        .getNetworkRetailer(id).execute()
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            callBackResponse.success("strresponse", responseBody.toString())
                        } else {

                            callBackResponse.fail("Request Failed.Response Body Null")
                        }
                    }
                } catch (e: Exception) {

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
