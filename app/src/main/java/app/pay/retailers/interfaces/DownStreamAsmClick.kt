package app.pay.retailers.interfaces

import app.pay.retailers.adapters.AsmDownStreamAdapter

import app.pay.retailers.responsemodels.asmresponse.Data

interface DownStreamAsmClick {
    fun onItemClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlist: MutableList<Data>, position: Int)
    fun onTransferMoneyClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)
    fun onReverseMoneyClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)
    fun onViewReportClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)
    fun onViewWalletReportClicked(holder: AsmDownStreamAdapter.ViewHolder, downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)

}