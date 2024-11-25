package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.dmtBankList.Data

interface BankListClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}