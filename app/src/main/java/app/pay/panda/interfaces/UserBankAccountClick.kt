package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.payoutBanks.Data

interface UserBankAccountClick {
    fun onItemCLicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}