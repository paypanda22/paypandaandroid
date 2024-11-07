package app.pay.pandapro.interfaces

import app.pay.pandapro.adapters.AsmDownStreamAdapter
import app.pay.pandapro.adapters.ZsmDownStreamAdapter

import app.pay.pandapro.responsemodels.asmresponse.Data

interface DownStreamAsmClick {
    fun onItemClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onTransferMoneyClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)
    fun onReverseMoneyClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)
    fun onViewReportClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)
    fun onViewWalletReportClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)

}