package app.pay.pandapro.responsemodels.deleteBene


import androidx.annotation.Keep

@Keep
data class DeleteBeneResponse(
    val error: Boolean = true,
    val message: String = "",
    val statusCode: Int = 0
)