package app.pay.panda.responsemodels.supportTickets

data class SupportTicketListResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)