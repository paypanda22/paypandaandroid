package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.packageListResponse.Price

interface PackagePriceClick {

    fun onPriceSelected(holder:RecyclerView.ViewHolder, model:List<Price>, pos:Int,)
}