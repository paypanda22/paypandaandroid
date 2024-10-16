package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.cashDepositBanks.Data


interface CdBankClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}