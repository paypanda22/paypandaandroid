package app.pay.panda.responsemodels.dmtBankList

data class Data(
    val __v: Int=0,
    val _id: String="",
    val available_channels: Int=0,
    val bankID: String="",
    val bank_name: String="",
    val ifsc_code: String="",
    val ifsc_status: Int=0,
    val isActive: Int=0,
    val is_imps: Int=0,
    val is_neft: Int=0,
    val is_verification: Int=0,
    val shor_code: String=""
)