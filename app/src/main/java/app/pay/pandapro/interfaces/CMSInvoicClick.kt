package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.cmsreportresponse.Data


interface CMSInvoicClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int, stsus: String)
}