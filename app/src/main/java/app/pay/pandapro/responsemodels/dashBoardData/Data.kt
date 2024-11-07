package app.pay.pandapro.responsemodels.dashBoardData

import androidx.annotation.Keep


@Keep
data class Data(
    val Tpin_status: String? = "",
    val adhaarVerified: Boolean? = false,
    val aeps_wallet: String? = "",
    val business_name: String? = "",
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
    val is_package_purchased: Boolean? = false,
    val is_personalDetails: Boolean? = false,
    val is_self_declare: Boolean? = false,
    val main_wallet: String? = "",
    val mobile: String? = "",
    val name: String? = "",
    val packageNameList: List<PackageName?>? = listOf(),
    val panVerified: Boolean? = false,
    val pan_name: String? = "",
    val pan_number: String? = "",
    val profile: String? = "",
    val refer_id: String? = "",
    val notification: Int? = 0,
    val user_type_id: UserTypeId? = UserTypeId()
){
    fun isEmpty(): Boolean {
        return !adhaarVerified!! && aeps_wallet == "0.00" && commision_wallet == "0.00" && email?.isEmpty() == true && !isGstAvailable!! && !isIdentity_verified!! && !is_approved!! && !is_bank!! && !is_document!! && !is_gst!! && !is_kycVid!! && !is_personalDetails!! && !is_self_declare!! && main_wallet == "0.00" && mobile?.isEmpty()==true && name?.isEmpty()==true  && !panVerified!! && pan_name?.isEmpty()==true && pan_number?.isEmpty()==true && profile?.isEmpty() ==true
    }
}