package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.disputeMaster.Department

interface DepartmentClickLickListner {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Department>,pos:Int)
}