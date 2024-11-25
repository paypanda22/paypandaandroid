package app.pay.retailers.responsemodels.supportTickets

data class SupportTicketListResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)