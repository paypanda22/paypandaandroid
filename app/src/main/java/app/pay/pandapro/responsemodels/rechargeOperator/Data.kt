package app.pay.pandapro.responsemodels.rechargeOperator

data class Data(
    val operators: List<Operator> = listOf(),
    val states: List<State> = listOf()
)