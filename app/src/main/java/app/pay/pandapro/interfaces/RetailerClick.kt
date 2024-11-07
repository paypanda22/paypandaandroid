package app.pay.pandapro.interfaces

import app.pay.pandapro.adapters.DownstreamRetailAdapter
import app.pay.pandapro.responsemodels.downstreamRetailerResponse.Data

interface RetailerClick {
    fun onItemClicked(holder: DownstreamRetailAdapter.ViewHolder, downstramlistRetailer: MutableList<Data>, position: Int, callback: (List<app.pay.pandapro.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: DownstreamRetailAdapter.ViewHolder, downstramlistRetailer: MutableList<Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownstreamRetailAdapter.ViewHolder, downstramlistRetailer: MutableList<Data>, position: Int)
    fun onViewReportClicked(holder: DownstreamRetailAdapter.ViewHolder, downstramlistRetailer: MutableList<Data>, position: Int)
    fun onViewWalletReportClicked(holder: DownstreamRetailAdapter.ViewHolder, downstramlistRetailer: MutableList<Data>, position: Int)

}