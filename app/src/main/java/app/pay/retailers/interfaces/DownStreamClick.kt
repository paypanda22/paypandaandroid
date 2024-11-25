package app.pay.retailers.interfaces

import app.pay.retailers.adapters.DownStreamAdapter
import app.pay.retailers.responsemodels.downstreamresponse.Data


interface DownStreamClick {
    fun onItemClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int, callback: (List<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onViewReportClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onViewWalletReportClicked(holder: DownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)

}