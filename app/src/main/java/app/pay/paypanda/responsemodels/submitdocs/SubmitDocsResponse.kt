package app.pay.paypanda.responsemodels.submitdocs

data class SubmitDocsResponse(
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)