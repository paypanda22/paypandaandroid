package app.pay.pandapro.responsemodels.certificate


import androidx.annotation.Keep

@Keep
data class CertificateResponse(
    val `data`: Data = Data(),
    val error: Boolean = false,
    val statusCode: Int = 0
)