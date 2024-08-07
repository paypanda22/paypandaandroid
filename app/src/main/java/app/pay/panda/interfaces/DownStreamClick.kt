package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.adapters.DownStreamAdapter
import app.pay.panda.responsemodels.downstreamresponse.Data


interface DownStreamClick {
    fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.panda.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onViewReportClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
}