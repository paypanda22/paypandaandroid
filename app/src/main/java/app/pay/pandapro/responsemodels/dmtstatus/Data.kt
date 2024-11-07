package app.pay.pandapro.responsemodels.dmtstatus


import androidx.annotation.Keep

@Keep
data class Data(
    val __v: Int = 0,
    val _id: String = "",
    val createdAt: String = "",
    val priority: String = "",
    val status: Boolean = false,
    val updatedAt: String = ""
)