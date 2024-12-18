package app.pay.retailers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.interfaces.UtilityTransactionClick
import app.pay.retailers.responsemodels.utilitytxn.Data

class UtilityTransactionAdapter(
    private val activity:Activity,
    private val list:List<Data>,
    private val click:UtilityTransactionClick
): RecyclerView.Adapter<UtilityTransactionAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvOpen:TextView=itemView.findViewById(R.id.tvOpen)
        val tvStatus:TextView=itemView.findViewById(R.id.tvStatus)
        val tvClosing:TextView=itemView.findViewById(R.id.tvClosing)
        val tvBankName:TextView=itemView.findViewById(R.id.tvBankName)
        val tvAccountNumber:TextView=itemView.findViewById(R.id.tvAccountNumber)
        val tvAmount:TextView=itemView.findViewById(R.id.tvAmount)
        val txnID:TextView=itemView.findViewById(R.id.txnID)
        val utr:TextView=itemView.findViewById(R.id.utr)
        val charges:TextView=itemView.findViewById(R.id.charges)
        val date:TextView=itemView.findViewById(R.id.date)
        val status_update_time:TextView=itemView.findViewById(R.id.status_update_time)
        val ivShare:AppCompatImageView=itemView.findViewById(R.id.ivShare)
        val refresh:AppCompatImageView=itemView.findViewById(R.id.refresh)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_recharge_report,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOpen.text="Open\n"+list[position].open_bal.toString()
        holder.tvClosing.text="Close\n"+list[position].close_bal.toString()
        when(list[position].status){
            2->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_success))
                holder.tvStatus.text="SUCCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                holder.ivShare.visibility=VISIBLE
                holder.refresh.visibility=GONE
            }
            3->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_failed))
                holder.tvStatus.text="FAILED"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                holder.ivShare.visibility= GONE
                holder.refresh.visibility=VISIBLE
            }
            else->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_pending))
                holder.tvStatus.text="IN-PROCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.black))
                holder.ivShare.visibility= GONE
                holder.refresh.visibility=VISIBLE
            }
        }
        holder.tvBankName.text=list[position].operator_name.toString()
        holder.tvAccountNumber.text=list[position].ca_num.toString()
        holder.tvAmount.text=list[position].amount.toString()
        holder.date.text=list[position].createdAt.toString()
        holder.txnID.text=list[position].txn_id.toString()
        holder.utr.text=list[position].biller_id.toString()
        holder.status_update_time.text=list[position].status_update_time.toString()

        holder.ivShare.setOnClickListener{
            if (holder.ivShare.visibility==VISIBLE){
                click.onItemClicked(holder,list,position,3)
            }
        }
        holder.refresh.setOnClickListener{
            click.onItemClicked(holder,list,position,2)
        }
    }
}