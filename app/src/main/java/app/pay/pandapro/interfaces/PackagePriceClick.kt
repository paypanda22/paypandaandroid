package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.realvalueresponse.Price


interface PackagePriceClick {

    fun onPriceSelected(holder:RecyclerView.ViewHolder, model:List<Price>, pos:Int,)
}