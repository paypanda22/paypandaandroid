package app.pay.pandapro.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.interfaces.AdharPayReportClick
import app.pay.pandapro.responsemodels.adharpayresponse.Data


class AadharPayAdapter (
    private val activity : Activity,
    private val list:List<Data>,
    private val click: AdharPayReportClick
): RecyclerView.Adapter<AadharPayAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val txnID: TextView =itemView.findViewById(R.id.txnID)
        val aadhar_no: TextView =itemView.findViewById(R.id.aadhar_no)
        val nationalbankidentification: AppCompatTextView =itemView.findViewById(R.id.nationalbankidentification)
        val bank_name: AppCompatTextView =itemView.findViewById(R.id.bank_name)
        val tvAmount: AppCompatTextView =itemView.findViewById(R.id.tvAmount)
        val status_update_time: AppCompatTextView =itemView.findViewById(R.id.status_update_time)
        val date: TextView =itemView.findViewById(R.id.date)
        val tvStatus: TextView =itemView.findViewById(R.id.tvStatus)
        val AadharNo: TextView =itemView.findViewById(R.id.AadharNo)
        val ivShare: AppCompatImageView =itemView.findViewById(R.id.ivShare)
        val refresh: AppCompatImageView =itemView.findViewById(R.id.refresh)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_aadhar_pay, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder,@SuppressLint("RecyclerView") position: Int) {
        holder.txnID.text=list[position].txn_id
        holder.aadhar_no.text=list[position].aadhar_no.toString()
       holder.nationalbankidentification.text=list[position].distributor_mobile.toString()
        holder.date.text=list[position].createdAt
        holder.tvAmount.text=list[position].amount.toString()
        holder.bank_name.text=list[position].bank_name.toString()
        holder.AadharNo.text=list[position].nationalbankidentification.toString()
        holder.status_update_time.text=list[position].status_update_time.toString()
        when(list[position].response){
            "2"->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_success))
                holder.tvStatus.text="SUCCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                holder.ivShare.visibility= VISIBLE
                holder.refresh.visibility= GONE
            }
            "3"->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_failed))
                holder.tvStatus.text="FAILED"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                holder.ivShare.visibility= GONE
                holder.refresh.visibility= VISIBLE
            }
            "1"->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_pending))
                holder.tvStatus.text="PENDING"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.black))
                holder.ivShare.visibility= GONE
                holder.refresh.visibility= VISIBLE
            }
            "4"->{
                holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.btn_grey))
                holder.tvStatus.text="REFUNDED"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.black))
                holder.ivShare.visibility= VISIBLE
                holder.refresh.visibility= GONE
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
