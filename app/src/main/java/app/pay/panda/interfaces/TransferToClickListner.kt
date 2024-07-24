package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.transferTo.Data

interface TransferToClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}