package app.pay.retailers.responsemodels.companyBanks

data class Data(
    val __v: Int=0,
    val _id: String="",
    val bank_account_number: String="",
    val bank_branch: String="",
    val bank_code: String="",
    val bank_id: String="",
    val bank_name: String="",
    val branch_name: String="",
    val eko_bank_code: String="",
    val ifsc_code: String="",
    val is_active: Boolean=false,
    val paysprint_bank_id: String="",
    val remark: String=""
)