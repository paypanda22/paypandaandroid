package app.pay.panda.responsemodels.mAtmInit


import androidx.annotation.Keep

@Keep
data class MAtmTxnInitResponse(
    val `data`: Data = Data(),
    val error: Boolean = true,
    val statusCode: Int = 0
)