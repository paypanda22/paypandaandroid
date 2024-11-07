package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.bbpscatagory.Data


interface BbpsCategoryClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}