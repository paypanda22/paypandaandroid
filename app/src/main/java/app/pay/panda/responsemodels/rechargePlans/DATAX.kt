package app.pay.panda.responsemodels.rechargePlans

import app.pay.panda.interfaces.CommonPlanData

data class DATAX(
    override val Type: String?,
    override val _id: String?,
    override val desc: String?,
    override val rs: Int?,
    override val validity: String?
):CommonPlanData