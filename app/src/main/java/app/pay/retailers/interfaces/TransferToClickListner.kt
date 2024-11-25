package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.transferTo.Data

interface TransferToClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}