package app.pay.retailers.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.responsemodels.requestList.Request
import com.google.android.material.card.MaterialCardView

class RequestListAdapter(
    private val activity:Activity,
    private val list:List<Request>
) : RecyclerView.Adapter<RequestListAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val mcvRequestItem:MaterialCardView=itemView.findViewById(R.id.mcvRequestItem)
        val tvRequestedTo:TextView=itemView.findViewById(R.id.tvRequestedTo)
        val tvStatus:TextView=itemView.findViewById(R.id.tvStatus)
        val tvAmount:TextView=itemView.findViewById(R.id.tvAmount)
        val Method:TextView=itemView.findViewById(R.id.Method)
        val Remark:TextView=itemView.findViewById(R.id.Remark)
        val remarkByComany:TextView=itemView.findViewById(R.id.remarkByComany)
        val Date:TextView=itemView.findViewById(R.id.Date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_wallet_request_list,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    /*    holder.tvRequestedTo.text=list[position].requestTo.name*/
        holder.tvRequestedTo.text=list[position].bank
       holder.tvAmount.text=list[position].amount.toString()
        holder.tvStatus.text=list[position].status
        holder.Method.text=list[position].method
        holder.Remark.text=list[position].remark
        holder.Date.text=list[position].createdAt
        holder.remarkByComany.text=list[position].remarkByAdmin

        if (list[position].status=="Approved"){
            /*holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.green_50
                )
            )*/
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.green_900))
        }else if (list[position].status=="Pending"){
          /*  holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.yellow_50
                )
            )*/
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.pending))
        }else if(list[position].status=="In progress"){
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.pending))
        }else{
         /*   holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.red_50
                ))*/
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.red))

        }
    }
}