package app.pay.retailers.responsemodels.rechargePlans

import app.pay.retailers.interfaces.CommonPlanData

data class FRC(
    override val Type: String?,
    override val _id: String?,
    override val desc: String?,
    override val rs: Int?,
    override val validity: String?
):CommonPlanData