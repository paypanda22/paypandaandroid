package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.aepsTxnList.Data

interface AepsTxnClick {

    fun onItemClick(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
    fun onItemClickInvoice(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
    fun onItemClickEnquery(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int,type:String)
}