package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.rechargePlans.Plans

interface ROfferClick {
    fun onOfferItemClicked(holder:RecyclerView.ViewHolder,model:List<Plans?>,pos:Int)
}