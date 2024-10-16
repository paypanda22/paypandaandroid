package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.rechargePlans.Plans

interface ROfferClick {
    fun onOfferItemClicked(holder:RecyclerView.ViewHolder,model:List<Plans?>,pos:Int)
}