package app.pay.panda.responsemodels.ticketHistory


import androidx.annotation.Keep

@Keep
data class Data(
    val _id: String = "",
    val attachments: List<String> = listOf(),
    val `by`: By = By(),
    val chat: String = "",
    val createdAt: String = "",
    val `operator`: String = ""
)