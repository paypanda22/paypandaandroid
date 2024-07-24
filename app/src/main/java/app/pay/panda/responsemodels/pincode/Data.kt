package app.pay.panda.responsemodels.pincode

data class Data(
    val circle_name: String="",
    val district_id: Int=0,
    val district_name: String="",
    val division_name: String="",
    val lattitude: String="",
    val longitude: String="",
    val office_name: String="",
    val office_type: String="",
    val pincode: String="",
    val record_id: Int=0,
    val region_name: String="",
    val state_id: Int=0,
    val state_name: String=""
)