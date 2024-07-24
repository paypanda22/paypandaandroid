package app.pay.panda.responsemodels.updateProfilePic

data class UpdateProfileImageResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)