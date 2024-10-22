package app.pay.pandapro.responsemodels.aadhaarAddress

data class AadhaarAddressResponse(
    val `data`: Data = Data(),
    val error: Boolean=true,
    val statusCode: String=""
)