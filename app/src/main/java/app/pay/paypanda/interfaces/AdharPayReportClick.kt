package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView

interface AdharPayReportClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.paypanda.responsemodels.adharpayresponse.Data>, pos: Int, stsus: String)
}