package app.pay.retailers.responsemodels.quickdhansendotp

data class QuickDhanSendOtpResponse(
    val `data`: Data?=Data(),
    val error: Boolean?=true,
    val message: String?="",
    val status_Code: Int?=0
)