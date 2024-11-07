package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.getOperators.Data

interface OplistClickListner {

    fun onItemClicked(holder: RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}