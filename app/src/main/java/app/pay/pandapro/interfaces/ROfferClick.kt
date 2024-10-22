package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.rechargePlans.Plans

interface ROfferClick {
    fun onOfferItemClicked(holder:RecyclerView.ViewHolder,model:List<Plans?>,pos:Int)
}