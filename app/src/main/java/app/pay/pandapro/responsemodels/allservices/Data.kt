package app.pay.pandapro.responsemodels.allservices


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val icon: String = "",
    val service_category: String = "",
    val service_name: String = ""
)