package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.rechargePlans.Plans

interface ROfferClick {
    fun onOfferItemClicked(holder:RecyclerView.ViewHolder,model:List<Plans?>,pos:Int)
}