package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.rechargeOperator.Operator

interface RechargeOperatorClick {

    fun onSelectOperator(holder:RecyclerView.ViewHolder,model:List<Operator>,pos:Int)
}