package app.pay.pandapro.responsemodels.dthInfo

data class DthInforResponse(
    val `data`: Data? =Data(),
    val error: Boolean? =true,
    val message: String? ="",
    val statusCode: Int? =0
)