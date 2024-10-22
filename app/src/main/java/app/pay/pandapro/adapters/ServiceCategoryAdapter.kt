package app.pay.pandapro.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.allservices.Category

class ServiceCategoryAdapter(
    private val activity: Activity,
    private val list:List<Category>
):RecyclerView.Adapter<ServiceCategoryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvName: TextView =itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_dispute_master_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text=list[position].data[position].service_name
    }
}