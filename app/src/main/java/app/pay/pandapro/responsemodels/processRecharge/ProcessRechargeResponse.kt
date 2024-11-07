package app.pay.pandapro.responsemodels.processRecharge

data class ProcessRechargeResponse(
    val `data`: Data?,
    val error: Boolean? =true,
    val message: String? ="",
    val statusCode: Int? =0
)