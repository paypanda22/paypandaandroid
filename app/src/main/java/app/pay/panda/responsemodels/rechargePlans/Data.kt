package app.pay.panda.responsemodels.rechargePlans

data class Data(
    val circle: String="",
    val offerPlan: OfferPlan=OfferPlan(),
    val `operator`: Operator= Operator(),
    val operator_code: String="",
    val plans: List<Plans?>? = listOf()
)