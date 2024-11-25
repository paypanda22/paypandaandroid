package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView

interface AdharPayReportClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.retailers.responsemodels.adharpayresponse.Data>, pos: Int, stsus: String)
}