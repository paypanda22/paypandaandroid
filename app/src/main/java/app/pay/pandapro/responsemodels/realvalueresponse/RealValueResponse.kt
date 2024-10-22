package app.pay.pandapro.responsemodels.realvalueresponse


import androidx.annotation.Keep


@Keep
data class RealValueResponse(
    val `data`:  Data = Data(),
    val error: Boolean = false,
    val statusCode: String = ""
)