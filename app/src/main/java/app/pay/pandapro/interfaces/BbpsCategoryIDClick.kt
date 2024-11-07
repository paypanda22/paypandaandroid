package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.CategoryIdResponse.Data


interface BbpsCategoryIDClick {
    fun onItemClickedId(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}