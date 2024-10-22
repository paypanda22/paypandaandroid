package app.pay.pandapro.responsemodels.deletePayoutBank


import androidx.annotation.Keep

@Keep
data class DeletePayoutBankAccountResponse(
    val `data`: Data = Data(),
    val error: Boolean = true,
    val message: String = "",
    val statusCode: Int = 0
)