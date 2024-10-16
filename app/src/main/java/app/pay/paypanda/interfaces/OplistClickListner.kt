package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.getOperators.Data

interface OplistClickListner {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}