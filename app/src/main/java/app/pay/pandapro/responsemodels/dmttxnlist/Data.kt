package app.pay.pandapro.responsemodels.dmttxnlist

data class Data(
    val total: Int?=0,
    val trans: List<Tran> =listOf()
)