package app.pay.paypanda.responsemodels.rechargePlans

data class OfferPlan(
    val DATA: List<DATAX> = listOf(),
    val FRC: List<FRC> = listOf(),
    val FULLTT: List<FULLTT> = listOf(),
    val Romaing: List<Romaing> = listOf(),
    val STV: Any? ="",
    val TOPUP: List<TOPUP> = listOf()
)