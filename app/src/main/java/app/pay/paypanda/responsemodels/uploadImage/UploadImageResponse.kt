package app.pay.paypanda.responsemodels.uploadImage

data class UploadImageResponse(
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)