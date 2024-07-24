package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.disputeMaster.Department
import app.pay.panda.responsemodels.disputeMaster.Service

interface ServicesClickLickListner {
    fun onItemClicked2(holder:RecyclerView.ViewHolder,model:List<Service>,pos:Int)
}