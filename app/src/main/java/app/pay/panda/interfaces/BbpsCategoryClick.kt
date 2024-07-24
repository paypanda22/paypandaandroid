package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.bbpscatagory.Data


interface BbpsCategoryClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}