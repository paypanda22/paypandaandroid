package app.pay.retailers.responsemodels.dmtSettings


import androidx.annotation.Keep

@Keep
data class DmtSettingsResponse(
    val `data`: Data = Data(),
    val error: Boolean = true,
    val statusCode: String = ""
)