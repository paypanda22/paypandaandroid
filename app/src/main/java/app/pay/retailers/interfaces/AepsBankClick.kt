package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.aepsBanklist.Data

interface AepsBankClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}