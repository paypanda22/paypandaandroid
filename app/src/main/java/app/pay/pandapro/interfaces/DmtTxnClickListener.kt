package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.dmttxnlist.Tran

interface DmtTxnClickListener {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Tran>,pos:Int,type:Int)
}