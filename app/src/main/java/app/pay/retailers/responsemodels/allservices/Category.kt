package app.pay.retailers.responsemodels.allservices


import androidx.annotation.Keep

@Keep
data class Category(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: Int = 0
)