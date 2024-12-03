package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.dmttxnlist.Tran

interface DmtTxnClickListener {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Tran>,pos:Int,type:Int)
}