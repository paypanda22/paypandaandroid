package app.pay.retailers.responsemodels.submitdocs

data class SubmitDocsResponse(
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)