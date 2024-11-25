package app.pay.retailers.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.responsemodels.walletreport.Wallet

class WallletReportAdapter (
    private val activity : Activity,
    private val list:List<Wallet>,

): RecyclerView.Adapter<WallletReportAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val txnID: TextView =itemView.findViewById(R.id.txnID)
     //   val aadhar_no: TextView =itemView.findViewById(R.id.aadhar_no)
//        val nationalbankidentification: AppCompatTextView =itemView.findViewById(R.id.nationalbankidentification)
       // val bank_name: AppCompatTextView =itemView.findViewById(R.id.bank_name)
        val tvAmount: AppCompatTextView =itemView.findViewById(R.id.tvAmount)
      //  val status_update_time: AppCompatTextView =itemView.findViewById(R.id.status_update_time)
        val date: TextView =itemView.findViewById(R.id.date)
        val tvStatus: TextView =itemView.findViewById(R.id.tvStatus)
        val tvOpen: TextView =itemView.findViewById(R.id.tvOpen)
        val tvClosing: TextView =itemView.findViewById(R.id.tvClosing)
        val txstatus: TextView =itemView.findViewById(R.id.txstatus)
        val Type: TextView =itemView.findViewById(R.id.Type)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallletReportAdapter.ViewHolder {
        return WallletReportAdapter.ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_wallet, parent, false)
        )
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txnID.text=list[position].txn_id
        holder.tvOpen.text=list[position].o_bal.toString()
        holder.tvClosing.text=list[position].c_bal.toString()
        holder.Type.text=list[position].type.toString()
        if(list[position].approve==true){
            holder.txstatus.text="Approved"
        }else{
            holder.txstatus.text="Not Approved"
        }

        holder.tvAmount.text="Amount : "+list[position].amount.toString()
        if(list[position].trans_type=="transfer"){
            holder.tvStatus.text=list[position].trans_type
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(activity, R.color.green))
        }else
        {
            holder.tvStatus.text=list[position].trans_type
            holder.tvStatus.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue_900))
        }

        holder.date.text=list[position].createdAt
    }
}

