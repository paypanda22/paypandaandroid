package app.pay.paypanda.interfaces

import app.pay.paypanda.adapters.DownStreamAdapter
import app.pay.paypanda.responsemodels.downstreamresponse.Data


interface DownStreamClick {
    fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.paypanda.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onViewReportClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)

}