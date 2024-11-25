package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.rechargeOperator.Operator

interface RechargeOperatorClick {

    fun onSelectOperator(holder:RecyclerView.ViewHolder,model:List<Operator>,pos:Int)
}