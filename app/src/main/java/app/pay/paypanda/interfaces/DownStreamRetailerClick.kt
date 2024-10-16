package app.pay.paypanda.interfaces

import app.pay.paypanda.adapters.DownStreamDistributerAdapter

interface DownStreamRetailerClick {
    fun onTransferMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.paypanda.responsemodels.downstramdistributer.Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.paypanda.responsemodels.downstramdistributer.Data>, position: Int)
}