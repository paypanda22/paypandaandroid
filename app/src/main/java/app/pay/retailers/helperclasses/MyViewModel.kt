import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.retrofit.GetData
import app.pay.retailers.retrofit.RetrofitFactory
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {

    private val _apiResponse = MutableLiveData<Result<String>>()
    val apiResponse: LiveData<Result<String>> get() = _apiResponse

    private val progressBar = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = progressBar

    fun chkMobile(context: Context, mobileNo: String, callBackResponse: MCallBackResponse) {
        if (UtilMethods.isNetworkAvailable(context)) {
            progressBar.value = true
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitFactory.getRetrofitInstance()
                        .create(GetData::class.java)
                        .chkMobile(mobileNo)
                        .execute()

                    withContext(Dispatchers.Main) {
                        progressBar.value = false
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body() as JsonObject
                            if (responseBody.has("error") && responseBody["error"].asBoolean) {
                                val message = responseBody["message"].asString
                                _apiResponse.value = Result.failure(Exception(message))
                            } else {
                                _apiResponse.value = Result.success(responseBody.toString())
                            }
                        } else {
                            _apiResponse.value = Result.failure(Exception("Request Failed. Response Body Null"))
                        }
                    }
                } catch (e: Exception) {
                    progressBar.value = false
                    withContext(Dispatchers.Main) {
                        _apiResponse.value = Result.failure(Exception("API Error"))
                    }
                }
            }
        } else {
            // Handle no internet case in UI
            _apiResponse.value = Result.failure(Exception("No Internet Connection"))
        }
    }

    // You can add other functions for chkEmail, userLogin, etc. similarly
}
