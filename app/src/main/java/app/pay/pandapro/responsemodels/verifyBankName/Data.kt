package app.pay.pandapro.responsemodels.verifyBankName


import androidx.annotation.Keep

@Keep
data class Data(
    val accountStatus: String = "",
    val accountStatusCode: String = "",
    val `data`: DataX = DataX(),
    val status: String = "",
    val subCode: String = ""
)