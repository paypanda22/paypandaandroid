package app.pay.paypanda.responsemodels.addTicket

data class AddSupportTicketResponse(
    val `data`: Data,
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)