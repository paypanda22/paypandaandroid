package app.pay.panda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R

import app.pay.panda.interfaces.RequetWalletClick
import app.pay.panda.responsemodels.requestList.Request
import com.google.android.material.card.MaterialCardView

class RequestListAdapterDist (
    private val activity: Activity,
    private val list:List<Request>,

) : RecyclerView.Adapter<RequestListAdapterDist.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val mcvRequestItem: MaterialCardView =itemView.findViewById(R.id.mcvRequestItem)
        val tvRequestedTo: TextView =itemView.findViewById(R.id.tvRequestedTo)
        val tvStatus: TextView =itemView.findViewById(R.id.tvStatus)
        val tvAmount: TextView =itemView.findViewById(R.id.tvAmount)
        val user_type: TextView =itemView.findViewById(R.id.user_type)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_wallet_dist_sup_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = list[position]
        holder.tvRequestedTo.text = list[position].requestTo?.name
        /*  holder.tvRequestedTo.text=list[position].bank*/
        holder.tvAmount.text = list[position].amount.toString()
        holder.user_type.text = list[position].requestTo?.user_type_id?.user_type.toString()
        holder.tvStatus.text = list[position].status
        if (list[position].status == "Approved") {
            holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.green_bow
                )
            )
        } else if (list[position].status == "Pending") {
            holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.rainbow_Orange
                )
            )
        } else {
            holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.red_bow
                )
            )

        }
    }
    }