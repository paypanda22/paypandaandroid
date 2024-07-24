package app.pay.panda.responsemodels.dmtSettings

data class DmtSettingsResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)