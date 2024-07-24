package app.pay.panda.responsemodels.requestList

data class Request(
    val __v: Int=0,
    val _id: String="",
    val amount: Long = 0L,
    val createdAt: String="",
    val paymentDate: String="",
    val requestTo: RequestTo=RequestTo(),
    val status: String="",
    val updatedAt: String="",
    val user_id: String="",
    val bank: String="",

)