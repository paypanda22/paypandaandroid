package app.pay.panda.responsemodels.distributerDashobord

data class Data(
    val dmt: List<Dmt>,
    val payout: List<Payout>,
    val paymentRequest: List<PaymentRequest>,
    val newAepsOnBoarding: Int,
    val panAgent: Int,
    val panCouponRequest: Int,
    val disputeRequest: Int,
    val wallet: Wallet,
    val dmtTotal: Int,
    val services: List<Service>,
    val cms: Cms,
    val quickDhan: QuickDhan,
    val adhaarPay: AdhaarPay,
    val CashWithdrew: CashWithdrew,
    val CashDeposit: CashDeposit
)

data class Dmt(
    val success: Int,
    val pending: Int,
    val failed: Int
)

data class Payout(
    val success: Int,
    val pending: Int,
    val failed: Int
)

data class PaymentRequest(
    val approved: Int,
    val rejected: Int,
    val pending: Int
)

data class Wallet(
    val creditTotal: Double,
    val debitTotal: Int
)

data class Service(
    val _id: String,
    val service_name: String,
    val pendingTotalAmount: Int,
    val successTotalAmount: Int,
    val failedTotalAmount: Int
)

data class Cms(
    val success: Int,
    val pending: Int,
    val failed: Int
)

data class QuickDhan(
    val success: Int,
    val pending: Int,
    val failed: Int
)

data class AdhaarPay(
    val success: Int,
    val pending: Int,
    val failed: Int
)

data class CashWithdrew(
    val success: Int,
    val pending: Int,
    val failed: Int
)

data class CashDeposit(
    val success: Int,
    val pending: Int,
    val failed: Int
)