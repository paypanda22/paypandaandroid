package app.pay.panda.responsemodels.userid



data class UserIDResponse (
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)