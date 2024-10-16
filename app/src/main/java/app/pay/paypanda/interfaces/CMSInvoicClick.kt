package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.cmsreportresponse.Data


interface CMSInvoicClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int, stsus: String)
}