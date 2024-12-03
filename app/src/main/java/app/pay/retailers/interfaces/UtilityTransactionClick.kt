package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.utilitytxn.Data

interface UtilityTransactionClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int,type: Int)
}