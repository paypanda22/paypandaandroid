package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.packageListResponse.Price

interface PackagePriceClick {

    fun onPriceSelected(holder:RecyclerView.ViewHolder, model:List<Price>, pos:Int,)
}