package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.payoutBanks.Data

interface UserBankAccountClick {
    fun onItemCLicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
    fun onItemCLickeddelete(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
    fun onItemCLickedstatus(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}