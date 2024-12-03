package app.pay.retailers.responsemodels.aepsenquiry


import androidx.annotation.Keep

@Keep
data class Data(
    val amount: Int = 0,
    val bal_amount: Int = 0,
    val bank_name: String = "",
    val bank_rrn: String = "",
    val errorCode: Boolean = false,
    val iin: String = "",
    val last_adhaar: String = "",
    val status: Int = 0
)