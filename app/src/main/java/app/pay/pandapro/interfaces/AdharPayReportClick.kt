package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView

interface AdharPayReportClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.pandapro.responsemodels.adharpayresponse.Data>, pos: Int, stsus: String)
}