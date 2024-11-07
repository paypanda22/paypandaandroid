package app.pay.pandapro.interfaces

import app.pay.pandapro.adapters.DownStreamAdapter
import app.pay.pandapro.responsemodels.downstreamresponse.Data


interface DownStreamClick {
    fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.pandapro.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onViewReportClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)

}