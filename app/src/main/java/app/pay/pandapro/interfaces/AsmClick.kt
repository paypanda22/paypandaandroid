package app.pay.pandapro.interfaces

import app.pay.pandapro.adapters.AsmAdapter
import app.pay.pandapro.adapters.AsmDownStreamAdapter
import app.pay.pandapro.responsemodels.asmresponse.Data


interface AsmClick {
    fun onItemClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<Data>, position: Int)
    fun onTransferMoneyClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)
    fun onReverseMoneyClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)
    fun onViewReportClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)
    fun onViewWalletReportClicked(holder: AsmAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>, position: Int)

}