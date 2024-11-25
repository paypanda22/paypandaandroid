package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.allservices.Data


interface DynamicServicesClickListener {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int)
}