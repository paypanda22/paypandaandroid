package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.CategoryIdResponse.Data


interface BbpsCategoryIDClick {
    fun onItemClickedId(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}