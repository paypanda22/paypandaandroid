package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.disputeMaster.Department
import app.pay.panda.responsemodels.disputeMaster.Priority

interface PriorityClickLickListner {
    fun onItemClicked1(holder:RecyclerView.ViewHolder,model:List<Priority>,pos:Int)
}