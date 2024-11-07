package app.pay.pandapro.responsemodels.rechargePlans

import app.pay.pandapro.interfaces.CommonPlanData

data class DATAX(
    override val Type: String?,
    override val _id: String?,
    override val desc: String?,
    override val rs: Int?,
    override val validity: String?
):CommonPlanData