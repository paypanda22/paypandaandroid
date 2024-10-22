package app.pay.pandapro.interfaces

import app.pay.pandapro.adapters.DownStreamAdapter
import app.pay.pandapro.adapters.ZsmDownStreamAdapter
import app.pay.pandapro.responsemodels.downstreamresponse.Data

interface ZSMDownStreamClick {
    fun onItemClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.pandapro.responsemodels.zsmresponse.Data>, position: Int, callback: (List<app.pay.pandapro.responsemodels.downstreamRetailerResponse.Data>) -> Unit)
    fun onTransferMoneyClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.pandapro.responsemodels.zsmresponse.Data>, position: Int)
    fun onReverseMoneyClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.pandapro.responsemodels.zsmresponse.Data>, position: Int)
    fun onViewReportClicked(holder: ZsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<app.pay.pandapro.responsemodels.zsmresponse.Data>, position: Int)

}