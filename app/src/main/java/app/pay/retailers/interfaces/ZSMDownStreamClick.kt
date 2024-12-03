package app.pay.retailers.interfaces

import app.pay.retailers.adapters.ZsmDownStreamAdapter

interface ZSMDownStreamClick {
    fun onItemClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int, callback: (List<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int)
    fun onReverseMoneyClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int)
    fun onViewReportClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int)
    fun onViewWalletReportClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>, position: Int)

}