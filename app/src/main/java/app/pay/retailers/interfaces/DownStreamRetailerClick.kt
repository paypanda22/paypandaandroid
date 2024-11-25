package app.pay.retailers.interfaces

import app.pay.retailers.adapters.DownStreamDistributerAdapter

interface DownStreamRetailerClick {
    fun onTransferMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.downstramdistributer.Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.downstramdistributer.Data>, position: Int)
    fun onViewWalletReportClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.downstramdistributer.Data>, position: Int)
    fun onViewReportClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.retailers.responsemodels.downstramdistributer.Data>, position: Int)
}