package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.rechargeOperator.Operator

interface RechargeOperatorClick {

    fun onSelectOperator(holder:RecyclerView.ViewHolder,model:List<Operator>,pos:Int)
}