package app.pay.pandapro.responsemodels.dmtSettings


import androidx.annotation.Keep

@Keep
data class Data(
    val defaultApi: String = "",
    val dmtApiType: List<DmtApiType> = listOf()
)