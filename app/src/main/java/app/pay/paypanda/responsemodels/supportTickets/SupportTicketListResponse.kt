package app.pay.paypanda.responsemodels.supportTickets

data class SupportTicketListResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)