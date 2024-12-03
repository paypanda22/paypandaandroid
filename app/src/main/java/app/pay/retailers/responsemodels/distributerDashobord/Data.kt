package app.pay.retailers.responsemodels.distributerDashobord

data class Data(
    val dmt: List<Dmt>,
    val payout: List<Payout>,
    val paymentRequest: List<PaymentRequest>,
    val newAepsOnBoarding: String ,
    val panAgent: String ,
    val panCouponRequest: String ,
    val disputeRequest: String ,
    val wallet: Wallet,
    val dmtTotal: String ,
    val services: List<Service>,
    val cms: Cms,
    val quickDhan: QuickDhan,
    val adhaarPay: AdhaarPay,
    val CashWithdrew: CashWithdrew,
    val CashDeposit: CashDeposit
)

data class Dmt(
    val success: String ,
    val pending: String ,
    val failed: String
)

data class Payout(
    val success: Double ,
    val pending: Double ,
    val failed: Double
)

data class PaymentRequest(
    val approved: String ,
    val rejected: String ,
    val pending: String
)

data class Wallet(
    val creditTotal: String,
    val debitTotal: String
)

data class Service(
    val _id: String,
    val service_name: String,
    val pendingTotalAmount: String,
    val successTotalAmount: String,
    val failedTotalAmount: String
)

data class Cms(
    val success: String,
    val pending: String,
    val failed: String
)

data class QuickDhan(
    val success: String,
    val pending: String,
    val failed: String
)

data class AdhaarPay(
    val success: String,
    val pending: String,
    val failed: String
)

data class CashWithdrew(
    val success: String,
    val pending: String ,
    val failed: String
)

data class CashDeposit(
    val success: String,
    val pending: String,
    val failed: String
)