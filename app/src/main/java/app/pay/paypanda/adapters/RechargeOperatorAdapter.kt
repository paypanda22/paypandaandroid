package app.pay.paypanda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.interfaces.RechargeOperatorClick
import app.pay.paypanda.responsemodels.rechargeOperator.Operator

class RechargeOperatorAdapter(
    private val activity: Activity,
    private val list:List<Operator>,
    private val click: RechargeOperatorClick
) :RecyclerView.Adapter<RechargeOperatorAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val ivImage:ImageView=itemView.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_dispute_master_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text=list[position].name
        when(list[position]._id){
            "6683b0150546958c65bf4bb4"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.bsnl))
            }
            "6683b0150546958c65bf4bb6"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.vi))
            }
            "6683b0150546958c65bf4bb4"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.bsnl))
            }
            "6683b0150546958c65bf4bb3"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.airtel))
            }
            "6683b0150546958c65bf4bb5"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.jio))
            }
            "6683b2970546958c65bf4bb8"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.airtel_tv))
            }
            "6683b2970546958c65bf4bb9"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.dish_tv))
            }
            "6683b2970546958c65bf4bba"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.sun_direct))
            }
            "6683b2970546958c65bf4bbb"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.tata_sky))
            }
            "6683b2970546958c65bf4bbc"->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.v_d2h))
            }
            else->{
                holder.ivImage.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.bsnl))
            }
        }

        holder.itemView.setOnClickListener {
            click.onSelectOperator(holder,list,position)
        }
    }
}