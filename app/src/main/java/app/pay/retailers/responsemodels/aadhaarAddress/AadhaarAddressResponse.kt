package app.pay.retailers.responsemodels.aadhaarAddress

data class AadhaarAddressResponse(
    val `data`: Data = Data(),
    val error: Boolean=true,
    val statusCode: String=""
)