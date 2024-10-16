package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.rechargeOperator.Operator

interface RechargeOperatorClick {

    fun onSelectOperator(holder:RecyclerView.ViewHolder,model:List<Operator>,pos:Int)
}