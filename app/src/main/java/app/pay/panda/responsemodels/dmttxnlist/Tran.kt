package app.pay.panda.responsemodels.dmttxnlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tran(
    val _id: String?,
    val account_number: String?,
    val amount: Int?,
    val bank_name: String?,
    val c_bal: Double?,
    val charge: Double?,
    val commission: Double?,
    val createdAt: String?,
    val response:Int=1,
    val beneficiary_name:String="",
    val customer_mobile: String?,
    val customer_name: String?,
    val is_commision: Boolean?,
    val is_refunded: Boolean?,
    val o_bal: Double?,
    val tx_status: String?,
    val txn_id: String?,
    val updatedAt: String?,
    val batchId: String?,
    val utr:String?=""
): Parcelable