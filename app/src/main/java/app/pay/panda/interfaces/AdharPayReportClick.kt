package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.cmsreportresponse.Data

interface AdharPayReportClick {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<app.pay.panda.responsemodels.adharpayresponse.Data>, pos: Int, stsus: String)
}