package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.bbpscatagory.Data


interface BbpsCategoryClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}