package app.pay.panda.interfaces

import app.pay.panda.adapters.DownStreamAdapter
import app.pay.panda.adapters.DownStreamDistributerAdapter
import app.pay.panda.adapters.DownstreamRetailAdapter
import app.pay.panda.responsemodels.downstreamresponse.Data

interface DownStreamRetailerClick {
    fun onTransferMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.panda.responsemodels.downstramdistributer.Data>, position: Int)
    fun onReverseMoneyClicked(holder: DownStreamDistributerAdapter.ViewHolder, downstramlistDistributer: MutableList<app.pay.panda.responsemodels.downstramdistributer.Data>, position: Int)
}