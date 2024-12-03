package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.disputeMaster.Priority

interface PriorityClickLickListner {
    fun onItemClicked1(holder:RecyclerView.ViewHolder,model:List<Priority>,pos:Int)
}