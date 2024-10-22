package app.pay.pandapro.responsemodels.countrylist

data class CountryListResponse(
    val `data`: List<Data> =listOf(),
    val error: Boolean=false,
    val statusCode: String=""
)