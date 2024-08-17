package app.pay.panda.retrofit

import android.content.Context
import android.widget.Toast
import app.pay.panda.R
import app.pay.panda.helperclasses.CustomProgressBar
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.retrofit.UtilMethods.isNetworkAvailable
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

    fun resendOtpTPin(context: Context, token: String,obj: Any, callBackResponse: MCallBackResponse) {
        if (isNetworkAvailable(context)) {
            val progressBar = CustomProgressBar()
            progressBar.showProgress(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitFactory.getRetrofitInstanceWithToken(token).create(GetData::class.java)
                        .resendOtpTPin(obj).execute()
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


}