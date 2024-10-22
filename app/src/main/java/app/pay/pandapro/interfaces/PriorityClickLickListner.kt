package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.disputeMaster.Priority

interface PriorityClickLickListner {
    fun onItemClicked1(holder:RecyclerView.ViewHolder,model:List<Priority>,pos:Int)
}