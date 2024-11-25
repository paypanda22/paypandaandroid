package app.pay.retailers.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.interfaces.DepartmentClickLickListner
import app.pay.retailers.interfaces.PriorityClickLickListner
import app.pay.retailers.interfaces.ServicesClickLickListner
import app.pay.retailers.responsemodels.disputeMaster.Department
import app.pay.retailers.responsemodels.disputeMaster.Priority
import app.pay.retailers.responsemodels.disputeMaster.Service

class DisputeMastersAdapter(
    private val activity:Activity,
    private val departments:List<Department>,
    private val priority:List<Priority>,
    private val services:List<Service>,
    private val click1:DepartmentClickLickListner,
    private val click2:PriorityClickLickListner,
    private val click3:ServicesClickLickListner,
    private val type:String
) :RecyclerView.Adapter<DisputeMastersAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvName:TextView=itemView.findViewById(R.id.tvName)
        val ivImage:ImageView=itemView.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_dispute_master_item,parent,false))
    }

    override fun getItemCount(): Int {
        if (type=="department"){
            return departments.size
        }else if (type=="priority"){
            return priority.size
        }else{
            return services.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivImage.visibility=View.GONE
        if (type=="department"){
            holder.tvName.text=departments[position].department
            holder.itemView.setOnClickListener {
                click1.onItemClicked(holder,departments,position)
            }

        }else if (type=="priority"){
            holder.tvName.text=priority[position].priority
            holder.itemView.setOnClickListener {
                click2.onItemClicked1(holder,priority,position)
            }
        }else{
            holder.tvName.text=services[position].service_name
            holder.itemView.setOnClickListener {
                click3.onItemClicked2(holder,services,position)
            }
        }
    }
}