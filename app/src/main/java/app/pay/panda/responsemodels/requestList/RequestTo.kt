package app.pay.panda.responsemodels.requestList

data class RequestTo(
    val _id: String="",
    val name: String="",
    val user_type_id: usertypeid=usertypeid(),
)