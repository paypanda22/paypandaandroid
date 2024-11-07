package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.transferTo.Data

interface TransferToClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}