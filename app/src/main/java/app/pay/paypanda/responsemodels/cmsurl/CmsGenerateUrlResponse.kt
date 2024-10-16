package app.pay.paypanda.responsemodels.cmsurl

data class CmsGenerateUrlResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: Int=0
)