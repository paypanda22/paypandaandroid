package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.cmsreportresponse.Data


interface CMSInvoicClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int, stsus: String)
}