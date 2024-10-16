package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.disputeMaster.Service

interface ServicesClickLickListner {
    fun onItemClicked2(holder:RecyclerView.ViewHolder,model:List<Service>,pos:Int)
}