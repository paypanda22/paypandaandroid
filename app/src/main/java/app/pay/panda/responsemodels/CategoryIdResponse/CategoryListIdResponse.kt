import app.pay.panda.responsemodels.CategoryIdResponse.Data


data class CategoryListIdResponse (

    val error: Boolean=true,
    val statusCode: Int=0,
    val data: List<Data> = listOf(),
    )