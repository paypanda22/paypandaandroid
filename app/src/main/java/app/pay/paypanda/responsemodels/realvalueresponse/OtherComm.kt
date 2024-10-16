package app.pay.paypanda.responsemodels.realvalueresponse


import androidx.annotation.Keep

@Keep
data class OtherComm(
    val _id: String = "",
    val commision: List<Commision> = listOf(),
    val commision_name: String = "",
    val service: String = ""
)