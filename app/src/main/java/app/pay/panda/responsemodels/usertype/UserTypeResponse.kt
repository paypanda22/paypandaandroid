package app.pay.panda.responsemodels.usertype


data class UserTypeResponse (
    val `data`: List<Data> = listOf(),
    val error: Boolean=true,
    val statusCode: Int=0
)