package app.pay.retailers.sqlitehelper

import androidx.annotation.Keep
import app.pay.retailers.responsemodels.allservices.Data

@Keep
data class Category1(
    val data: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0
)

@Keep
data class Data(
    val _id: String = "",
    val icon: String = "",
    val service_category: String = "",
    val service_name: String = ""
)
