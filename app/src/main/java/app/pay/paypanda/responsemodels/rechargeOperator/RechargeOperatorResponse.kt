package app.pay.paypanda.responsemodels.rechargeOperator

data class RechargeOperatorResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: Int=0
)