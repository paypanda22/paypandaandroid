package app.pay.retailers.interfaces

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R

class GeneralPlanAdapter(
    private val activity: Activity,
    private val list: List<CommonPlanData>,
    private val click: CommonPlansClick
) : RecyclerView.Adapter<GeneralPlanAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_gen_plans, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDesc.text = list[position].validity.toString()
        holder.tvAmount.text = list[position].rs.toString()
        holder.tvTitle.text = list[position].desc.toString().trim()
        holder.itemView.setOnClickListener {
            click.onGeneralPlanSelected(holder,list,position)
        }
    }

}