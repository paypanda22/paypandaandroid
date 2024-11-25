package app.pay.retailers.responsemodels.companyBanks

data class CompanyBanksResponse(
    val `data`: List<Data> =listOf(),
    val error: Boolean=true,
    val statusCode: String=""
)