package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.disputeMaster.Department

interface DepartmentClickLickListner {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Department>,pos:Int)
}