package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.statelist.Data


interface StateListClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}