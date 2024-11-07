package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.disputeMaster.Department

interface DepartmentClickLickListner {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Department>,pos:Int)
}