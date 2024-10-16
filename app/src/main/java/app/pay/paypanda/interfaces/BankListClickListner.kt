package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.dmtBankList.Data

interface BankListClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}