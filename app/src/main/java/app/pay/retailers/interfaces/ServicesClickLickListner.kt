package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.disputeMaster.Service

interface ServicesClickLickListner {
    fun onItemClicked2(holder:RecyclerView.ViewHolder,model:List<Service>,pos:Int)
}