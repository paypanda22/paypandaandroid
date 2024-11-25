package app.pay.retailers.interfaces

import app.pay.retailers.adapters.AsmAdapter
import app.pay.retailers.responsemodels.asmresponse.Data


interface AsmClick {
    fun onItemClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<Data>, position: Int)
    fun onTransferMoneyClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)
    fun onReverseMoneyClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)
    fun onViewReportClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)
    fun onViewWalletReportClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.asmresponse.Data>, position: Int)

}