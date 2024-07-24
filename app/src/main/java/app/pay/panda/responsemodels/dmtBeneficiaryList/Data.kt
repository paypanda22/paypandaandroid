package app.pay.panda.responsemodels.dmtBeneficiaryList

data class Data(
    val account: String="",
    val available_channel: Int=0,
    val bank: String="",
    val ifsc: String="",
    val isVerified: Boolean=false,
    val recipient_id: String="",
    val recipient_mobile: String="",
    val recipient_name: String="",
    val bankId:String=""
)