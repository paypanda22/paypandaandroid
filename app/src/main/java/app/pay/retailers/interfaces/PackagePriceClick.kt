package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.realvalueresponse.Price


interface PackagePriceClick {

    fun onPriceSelected(holder:RecyclerView.ViewHolder, model:List<Price>, pos:Int,)
}