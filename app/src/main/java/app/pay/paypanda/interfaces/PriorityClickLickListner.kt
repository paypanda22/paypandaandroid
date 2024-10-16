package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.disputeMaster.Priority

interface PriorityClickLickListner {
    fun onItemClicked1(holder:RecyclerView.ViewHolder,model:List<Priority>,pos:Int)
}