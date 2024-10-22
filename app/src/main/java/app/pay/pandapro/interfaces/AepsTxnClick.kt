package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.aepsTxnList.Data

interface AepsTxnClick {

    fun onItemClick(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
    fun onItemClickInvoice(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
    fun onItemClickEnquery(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int,type:String)
}