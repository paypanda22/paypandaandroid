package app.pay.paypanda.responsemodels.recepentupdatelist


import androidx.annotation.Keep

@Keep
data class Recipient(
    val account: String = "",
    val available_channel: Any = Any(),
    val bank: String = "",
    val ifsc: String = "",
    val isDelete: Boolean = false,
    val isVerified: Boolean = false,
    val recipient_id: String = "",
    val recipient_mobile: String = "",
    val recipient_name: String = ""
)