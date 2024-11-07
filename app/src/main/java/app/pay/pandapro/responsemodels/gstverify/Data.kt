package app.pay.pandapro.responsemodels.gstverify

data class Data(
    val GSTIN: String="",
    val center_jurisdiction: String="",
    val constitution_of_business: String="",
    val date_of_registration: String="",
    val gst_in_status: String="",
    val last_update_date: String="",
    val legal_name_of_business: String="",
    val message: String="",
    val nature_of_business_activities: List<String> =listOf(),
    val principal_place_address: String="",
    val reference_id: Int=0,
    val state_jurisdiction: String="",
    val taxpayer_type: String="",
    val trade_name_of_business: String="",
    val valid: Boolean=false
)