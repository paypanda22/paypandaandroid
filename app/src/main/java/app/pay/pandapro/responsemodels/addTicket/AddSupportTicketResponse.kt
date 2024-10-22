package app.pay.pandapro.responsemodels.addTicket

data class AddSupportTicketResponse(
    val `data`: Data,
    val error: Boolean=true,
    val message: String="",
    val statusCode: String=""
)