package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.utilitytxn.Data

interface UtilityTransactionClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int,type: Int)
}