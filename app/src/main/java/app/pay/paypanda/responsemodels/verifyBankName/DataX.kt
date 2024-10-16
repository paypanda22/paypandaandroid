package app.pay.paypanda.responsemodels.verifyBankName


import androidx.annotation.Keep

@Keep
data class DataX(
    val bankName: String = "",
    val branch: String = "",
    val city: String = "",
    val micr: Int = 0,
    val nameAtBank: String = "",
    val refId: String = ""
)