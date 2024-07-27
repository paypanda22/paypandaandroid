package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.utilitytxn.Data

interface UtilityTransactionClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int,type: Int)
}