package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.aepsBanklist.Data

interface AepsBankClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}