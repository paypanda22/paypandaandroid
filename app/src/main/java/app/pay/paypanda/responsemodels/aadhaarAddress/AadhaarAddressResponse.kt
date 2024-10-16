package app.pay.paypanda.responsemodels.aadhaarAddress

data class AadhaarAddressResponse(
    val `data`: Data = Data(),
    val error: Boolean=true,
    val statusCode: String=""
)