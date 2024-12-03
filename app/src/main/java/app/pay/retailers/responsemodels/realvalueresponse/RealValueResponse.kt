package app.pay.retailers.responsemodels.realvalueresponse


import androidx.annotation.Keep


@Keep
data class RealValueResponse(
    val `data`:  Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)