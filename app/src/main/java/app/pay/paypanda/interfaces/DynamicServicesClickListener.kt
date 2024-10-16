package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.allservices.Data


interface DynamicServicesClickListener {
    fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int)
}