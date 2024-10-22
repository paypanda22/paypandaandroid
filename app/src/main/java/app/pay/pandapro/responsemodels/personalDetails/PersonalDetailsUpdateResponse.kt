package app.pay.pandapro.responsemodels.personalDetails

data class PersonalDetailsUpdateResponse(
    val `data`: Data?,
    val error: Boolean?,
    val message: String?,
    val statusCode: Int?
)