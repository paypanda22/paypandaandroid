package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.payoutresponse.Data

interface AepsPayoutClickListener {
    fun onItemClick(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
    fun onItemClickenquiry(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}