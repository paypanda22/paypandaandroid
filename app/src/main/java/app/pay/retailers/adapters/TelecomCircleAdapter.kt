package app.pay.retailers.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.helperclasses.TelecomCircle
import app.pay.retailers.interfaces.TelecomCircleClick

class TelecomCircleAdapter(
    private val activity: Activity,
    private val list: List<TelecomCircle>,
    private val click: TelecomCircleClick
) : RecyclerView.Adapter<TelecomCircleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_dispute_master_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = list[position].name
        holder.itemView.setOnClickListener {
            click.onItemClicked(holder, list, position)
        }
    }
}