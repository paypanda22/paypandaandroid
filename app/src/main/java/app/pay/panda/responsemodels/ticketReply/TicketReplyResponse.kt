package app.pay.panda.responsemodels.ticketReply

data class TicketReplyResponse(
    val `data`: Data=Data(),
    val error: Boolean=true,
    val statusCode: String=""
)