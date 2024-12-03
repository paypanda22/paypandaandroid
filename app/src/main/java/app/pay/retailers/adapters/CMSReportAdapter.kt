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
import app.pay.retailers.adapters.CMSReportAdapter.ViewHolder
import app.pay.retailers.interfaces.CMSInvoicClick
import app.pay.retailers.responsemodels.cmsreportresponse.Data

class CMSReportAdapter (
    private val activity: Activity,
    private val list:List<Data>,
    private val click: CMSInvoicClick
): RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val billerID: TextView =itemView.findViewById(R.id.billerId)
        val billerName: TextView =itemView.findViewById(R.id.BillerName)
        val txnID: TextView =itemView.findViewById(R.id.txnID)
        val mob: TextView =itemView.findViewById(R.id.mob)
        val tvAmount: TextView =itemView.findViewById(R.id.tvAmount)
        val tvAckNo: TextView =itemView.findViewById(R.id.tvAckNo)
        val unique_id: TextView =itemView.findViewById(R.id.unique_id)
        val date: TextView =itemView.findViewById(R.id.date)
        val status_update_time: TextView =itemView.findViewById(R.id.status_update_time)
        val tvStatus:TextView=itemView.findViewById(R.id.tvStatus)
        val ivShare: AppCompatImageView =itemView.findViewById(R.id.ivShare)
        val refresh: AppCompatImageView =itemView.findViewById(R.id.refresh)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CMSReportAdapter.ViewHolder {
        return CMSReportAdapter.ViewHolder(
            LayoutInflater.from(
                activity
            ).inflate(R.layout.lyt_cms_report, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CMSReportAdapter.ViewHolder, position: Int) {
        holder.billerID.text="Biller Id\n"+list[position].biller_id.toString()
        holder.billerName.text="Biller Name\n"+list[position].biller_name.toString()
        holder.txnID.text=list[position].txn_id.toString()
        holder.mob.text=list[position].mobile_no.toString()
        holder.tvAmount.text=list[position].amount.toString()
        holder.tvAckNo.text= "Ackno :"+list[position].ackno.toString()
        holder.unique_id.text="Unique id: "+list[position].unique_id.toString()
        holder.date.text=" "+list[position].createdAt.toString()
        holder.status_update_time.text=" "+list[position].status_update_time.toString()
        when(list[position].status){
            2->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_success))
                holder.tvStatus.text="SUCCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                holder.ivShare.visibility= VISIBLE
                holder.refresh.visibility= GONE
            }
            3->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_failed))
                holder.tvStatus.text="FAILED"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                holder.ivShare.visibility= GONE
                holder.refresh.visibility= VISIBLE
            }
            else->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_pending))
                holder.tvStatus.text="PENDING"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.black))
                holder.ivShare.visibility= GONE
                holder.refresh.visibility= VISIBLE
            }
        }
        holder.ivShare.setOnClickListener{
            if (holder.ivShare.visibility==VISIBLE){
                click.onItemClicked(holder,list,position,list[position].status.toString())
            }
        }
        holder.refresh.setOnClickListener({
            click.onItemClicked(holder,list,position,list[position].status.toString())
        })
    }

}