package app.pay.retailers.responsemodels.filter


import androidx.annotation.Keep

@Keep
data class DepartmentReposne(
    val `data`: List<Data> = listOf(),
    val error: Boolean = false,
    val statusCode: String = ""
)