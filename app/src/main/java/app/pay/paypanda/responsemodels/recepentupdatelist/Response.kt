package app.pay.paypanda.responsemodels.recepentupdatelist


import androidx.annotation.Keep

@Keep
data class Response(
    val recipient_list: List<Recipient> = listOf()
)