package app.pay.paypanda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.interfaces.ROfferClick
import app.pay.paypanda.responsemodels.rechargePlans.Plans

class ROfferAdapter(
    private val activity: Activity,
    private val list: List<Plans?>,
    private val click:ROfferClick
) : RecyclerView.Adapter<ROfferAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_r_offer, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDesc.text= list[position]?.logdesc.toString()
        holder.tvTitle.text= list[position]?.ofrtext.toString()
        holder.tvAmount.text= list[position]?.price.toString()
        holder.itemView.setOnClickListener {
            click.onOfferItemClicked(holder,list,position)
        }
    }
}