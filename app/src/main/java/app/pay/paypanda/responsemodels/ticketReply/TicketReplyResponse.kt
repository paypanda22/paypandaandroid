package app.pay.paypanda.responsemodels.ticketReply

data class TicketReplyResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)