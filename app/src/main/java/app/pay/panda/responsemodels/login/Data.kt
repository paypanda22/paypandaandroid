package app.pay.panda.responsemodels.login


import androidx.annotation.Keep

@Keep
data class Data(
    val Tpin_status: String? = "",
    val adhaarVerified: Boolean? = false,
    val aeps_wallet: String? = "",
    val commision_wallet: String? = "",
    val email: String? = "",
    val isGstAvailable: Boolean? = false,
    val isIdentity_verified: Boolean? = false,
    val is_approved: Boolean? = false,
    val is_bank: Boolean? = false,
    val is_document: Boolean? = false,
    val is_doorMat: Boolean? = false,
    val is_gst: Boolean? = false,
    val is_kycVid: Boolean? = false,
    val is_personalDetails: Boolean? = false,
    val is_self_declare: Boolean? = false,
    val main_wallet: String? = "",
    val mobile: String? = "",
    val name: String? = "",
    val panVerified: Boolean? = false,
    val pan_name: String? = "",
    val pan_number: String? = "",
    val profile: String? = "",
    val state: String? = "",
    val user: String? = ""
)