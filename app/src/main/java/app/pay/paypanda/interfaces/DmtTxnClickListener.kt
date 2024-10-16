package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.dmttxnlist.Tran

interface DmtTxnClickListener {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Tran>,pos:Int,type:Int)
}