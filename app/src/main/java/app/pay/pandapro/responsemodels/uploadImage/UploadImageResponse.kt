package app.pay.pandapro.responsemodels.uploadImage

data class UploadImageResponse(
    val `data`: Data?,
    val error: Boolean?,
    val statusCode: String?
)