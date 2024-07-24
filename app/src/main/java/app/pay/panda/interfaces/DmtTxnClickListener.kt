package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.dmttxnlist.Tran

interface DmtTxnClickListener {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Tran>,pos:Int,type:Int)
}