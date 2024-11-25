package app.pay.retailers.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getString
import app.pay.retailers.R
import app.pay.retailers.helperclasses.CustomProgressBar
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.MCallBackResponse
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call

object CommonApiCall {
    private val progressBar = CustomProgressBar()
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    private fun performApiCall(
        context: Context,
        isLoader: Boolean = true,
        token: String="",
        obj: Any? = null,
        param1: Any? = null,
        apiCall: (GetData, Any?) -> Call<JsonObject>?,
        callBackResponse: MCallBackResponse
    ) {
        if (isNetworkAvailable(context)) {
            if (isLoader) {
                progressBar.showProgress(context)
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val requestBody = param1 ?: obj
                    val retrofitInstance = if (token.isEmpty()) {
                        RetrofitFactory.getRetrofitInstance()
                    } else {
                        RetrofitFactory.getRetrofitInstanceWithToken(token)
                    }
                    val response = apiCall(retrofitInstance.create(GetData::class.java), requestBody)?.execute()

                    withContext(Dispatchers.Main) {
                        if (response != null) {
                            if (response.isSuccessful && response.body() != null) {
                                progressBar.hideProgress()
                                val responseBody = response.body() as JsonObject
                                callBackResponse.success("response", responseBody.toString())
                            } else {
                                progressBar.hideProgress()
                                callBackResponse.fail("Request Failed. Response Body Null")
                            }
                        }
                    }
                } catch (e: Exception) {
                    progressBar.hideProgress()
                    withContext(Dispatchers.Main) {
                        e.printStackTrace()
                        callBackResponse.fail("Request Failed. API ERROR")
                    }
                }
            }
        } else {
            showToast(context, getString(context, R.string.check_internet))
        }
    }
    fun deleteDmtBeneficiary(context: Context, token: String, obj: Any? = null, param1: Any? = null, callBackResponse: MCallBackResponse) {
        performApiCall(context,true,token, obj,param1, { api, body -> body?.let { api.deleteDmtBeneficiary(it) } }, callBackResponse)
    }
}