package app.pay.pandapro.interfaces

import app.pay.pandapro.adapters.DownStreamDistributerAdapter

interface DownStreamRetailerClick {
    fun onTransferMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.downstramdistributer.Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.downstramdistributer.Data>, position: Int)
}