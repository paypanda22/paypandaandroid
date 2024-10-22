package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.packageListResponse.Data

interface PackageListClickListener {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
    fun onItemClickedDetail(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}