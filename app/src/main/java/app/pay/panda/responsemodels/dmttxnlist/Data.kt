package app.pay.panda.responsemodels.dmttxnlist

data class Data(
    val total: Int?=0,
    val trans: List<Tran> =listOf()
)