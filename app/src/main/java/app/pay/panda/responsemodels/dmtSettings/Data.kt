package app.pay.panda.responsemodels.dmtSettings

data class Data(
    val bankVerificationCharge: Int=0,
    val defaultApi: String="",
    val dmtApiType: String=""
)