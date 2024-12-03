package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.CategoryIdResponse.Data


interface BbpsCategoryIDClick {
    fun onItemClickedId(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}