package app.pay.retailers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.responsemodels.dmtTransaction.Data

class DmtMakeTxnAdapter(
    val activity: Activity,
    val list:List<Data>
) :RecyclerView.Adapter<DmtMakeTxnAdapter.ViewHolder>() {
    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        val tvAmount:TextView=itemView.findViewById(R.id.tvAmount)
        val tvUtr:TextView=itemView.findViewById(R.id.tvUtr)
        val tvStatus:TextView=itemView.findViewById(R.id.tvStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_dmt_response_item_list,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAmount.text=list[position].amount.toString()
        holder.tvUtr.text=list[position].utr ?: ""
        when (list[position].response) {

            1 -> {
                holder.tvStatus.text="PENDING"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.pending))
            }
            2->{
                holder.tvStatus.text="SUCCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.green_700))
            }
            3->{
                holder.tvStatus.text="FAILED"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.red))
            }
            else -> {
                holder.tvStatus.text="In Process"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.bggrey_dark))
            }
        }

    }
}