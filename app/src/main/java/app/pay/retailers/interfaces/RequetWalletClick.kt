package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.Request


interface RequetWalletClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Request>, pos:Int, amount: String, date: String, status: String, id:String, mobile:String, userType:String,remark:String)
}