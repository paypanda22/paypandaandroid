package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.getOperators.Data

interface OplistClickListner {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}