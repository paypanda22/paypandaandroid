package app.pay.pandapro.responsemodels.bankverification


import androidx.annotation.Keep

@Keep
data class Data(
    val bank_account_name: String = "",
    val bank_account_number: Long = 0,
    val bank_ifsc: String = "",
    val bank_name: String = "",
    val type: String = ""
)