package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.CategoryIdResponse.Data


interface BbpsCategoryIDClick {
    fun onItemClickedId(holder: RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}