package app.pay.paypanda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.interfaces.RequetWalletClick
import app.pay.paypanda.responsemodels.Request


import com.google.android.material.card.MaterialCardView

class RequestListAdminAdapter(
    private val activity: Activity,
    private val list: MutableList<Request>,
    private val requestWallletClick: RequetWalletClick
) : RecyclerView.Adapter<RequestListAdminAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val mcvRequestItem: MaterialCardView =itemView.findViewById(R.id.mcvRequestItem)
        val tvRequestedTo: TextView =itemView.findViewById(R.id.tvRequestedTo)
        val tvStatus: TextView =itemView.findViewById(R.id.tvStatus)
        val tvAmount: TextView =itemView.findViewById(R.id.tvAmount)
        val user_type: TextView =itemView.findViewById(R.id.user_type)
        val Date: TextView =itemView.findViewById(R.id.Date)
        val Name: TextView =itemView.findViewById(R.id.Name)
        val mobile: TextView =itemView.findViewById(R.id.mobile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_admin_request_list,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = list[position]
        holder.tvRequestedTo.text = list[position].user_type
        /*  holder.tvRequestedTo.text=list[position].bank*/
        holder.tvAmount.text = list[position].amount.toString()
        holder.user_type.text = list[position].remark
        holder.Date.text = list[position].createdAt
        holder.Name.text = list[position].user_id
        holder.mobile.text = list[position].user_mobile

        holder.tvStatus.text = list[position].status
        if (list[position].status == "Approved") {
            holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.green_50
                )
            )
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.green_900))
        } else if (list[position].status == "Pending") {
            holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.yellow_100
                )
            )
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.deep_orange_900))
        } else {
            holder.mcvRequestItem.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.red_bow
                )
            )
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.dark_red))
        }

        holder.tvStatus.setOnClickListener {
            val id = request._id ?: "N/A"
            val mobile = request.user_mobile ?: "N/A"
            val userType = request.user_id ?: "N/A"
            val remark = request.remark ?: "N/A"
            requestWallletClick.onItemClicked(
                holder,
                list,
                position,
                request.amount.toString(),
                request.createdAt,
                request.status,
                id,
                mobile,
                userType,
                remark
            )
        }
    }
}
