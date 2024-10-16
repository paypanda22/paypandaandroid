package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.disputeMaster.Department

interface DepartmentClickLickListner {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Department>,pos:Int)
}