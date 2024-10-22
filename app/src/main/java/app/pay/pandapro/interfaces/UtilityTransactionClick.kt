package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.utilitytxn.Data

interface UtilityTransactionClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int,type: Int)
}