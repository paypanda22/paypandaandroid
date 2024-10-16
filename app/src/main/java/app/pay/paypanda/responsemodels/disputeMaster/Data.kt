package app.pay.paypanda.responsemodels.disputeMaster

data class Data(
    val department: List<Department> = listOf(),
    val priority: List<Priority> = listOf(),
    val services: List<Service> = listOf()
)