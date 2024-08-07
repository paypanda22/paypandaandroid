package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.Request


interface RequetWalletClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Request>, pos:Int, amount: String, date: String, status: String, id:String, mobile:String, userType:String)
}