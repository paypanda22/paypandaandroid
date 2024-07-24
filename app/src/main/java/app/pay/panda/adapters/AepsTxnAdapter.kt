package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.interfaces.AepsTxnClick
import app.pay.panda.responsemodels.aepsTxnList.Data

class AepsTxnAdapter(
    private val activity: Activity,
    private val list: List<Data>,
    private val click: AepsTxnClick
) : RecyclerView.Adapter<AepsTxnAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOpen: TextView = itemView.findViewById(R.id.tvOpen)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvClose: TextView = itemView.findViewById(R.id.tvClose)
        val tvBankName: TextView = itemView.findViewById(R.id.tvBankName)
        val tvBankRrn: TextView = itemView.findViewById(R.id.tvBankRrn)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvAckNo: TextView = itemView.findViewById(R.id.tvAckNo)
        val tvTxnId: TextView = itemView.findViewById(R.id.tvTxnId)
        val tvCustomerMobile: TextView = itemView.findViewById(R.id.tvCustomerMobile)
        val tvCustomerAadhaar: TextView = itemView.findViewById(R.id.tvCustomerAadhaar)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val ivShare: ImageView = itemView.findViewById(R.id.ivShare)
        val rlClosing: RelativeLayout = itemView.findViewById(R.id.rlClosing)
        val rlOpen: RelativeLayout = itemView.findViewById(R.id.rlOpen)
        val rlStatus: RelativeLayout = itemView.findViewById(R.id.rlStatus)
        val ivType: ImageView = itemView.findViewById(R.id.ivType)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_aeps_txn_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOpen.text = list[position].opening_balance.toString()
        holder.tvClose.text = list[position].closing_balance.toString()
        when(list[position].type){
            "CW"->{
                holder.tvType.text= "Cash Withdrawal"
                holder.ivType.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.loan_1))
                holder.ivShare.visibility= VISIBLE
                holder.tvAmount.visibility= VISIBLE
                holder.rlOpen.visibility= VISIBLE
                holder.rlClosing.visibility= VISIBLE
            }
            "MS"->{
                holder.tvType.text= "Mini-Statement"
                holder.ivType.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.statement_mini))
                holder.ivShare.visibility=GONE
                holder.tvAmount.visibility= GONE
                holder.rlOpen.visibility= GONE
                holder.rlClosing.visibility= GONE
            }
            "BE"-> {
                holder.tvType.text= "Balance Enquiry"
                holder.ivType.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.bag_money))
                holder.ivShare.visibility=GONE
                holder.tvAmount.visibility= GONE
                holder.rlOpen.visibility= GONE
                holder.rlClosing.visibility= GONE
            }
            "AP"->{
                holder.tvType.text= "Aadhaar Pay"
                holder.ivType.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.loan_1))
                holder.ivShare.visibility= VISIBLE
                holder.tvAmount.visibility= VISIBLE
            }
            "CD"->{
                holder.tvType.text= "Cash Deposit"
                holder.ivType.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.deposit))
                holder.ivShare.visibility= VISIBLE
                holder.tvAmount.visibility= VISIBLE
                holder.rlOpen.visibility= VISIBLE
                holder.rlClosing.visibility= VISIBLE
            }
            else->{
                holder.tvType.text= ""
                holder.ivType.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.ic_error_base))
                holder.ivShare.visibility=GONE
                holder.tvAmount.visibility= GONE
                holder.rlOpen.visibility= GONE
                holder.rlClosing.visibility= GONE
            }
        }
        if (list[position].response == 2) {
            holder.tvStatus.text = "SUCCESS"
            holder.rlStatus.setBackgroundColor(ContextCompat.getColor(activity, R.color.green_700))
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.white))
            holder.tvType.setTextColor(ContextCompat.getColor(activity, R.color.white))
            holder.ivType.setColorFilter(ContextCompat.getColor(activity, R.color.white))
            if (list[position].type=="CW" || list[position].type=="AP") holder.ivShare.visibility= VISIBLE
        } else if (list[position].response == 3) {
            holder.tvStatus.text = "FAILED"
            holder.rlStatus.setBackgroundColor(ContextCompat.getColor(activity, R.color.red_700))
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.white))
            holder.tvType.setTextColor(ContextCompat.getColor(activity, R.color.white))
            holder.ivType.setColorFilter(ContextCompat.getColor(activity, R.color.white))
            holder.ivShare.visibility= GONE
        } else {
            holder.tvStatus.text = "PENDING"
            holder.rlStatus.setBackgroundColor(ContextCompat.getColor(activity, R.color.yellow_700))
            holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.black))
            holder.tvType.setTextColor(ContextCompat.getColor(activity, R.color.black))
            holder.ivType.setColorFilter(ContextCompat.getColor(activity, R.color.black))
           holder.ivShare.visibility= GONE
        }
        holder.tvBankName.text = list[position].bank_name
        if (list[position].bank_rrn.isEmpty()) {
            holder.tvBankRrn.text = list[position].txn_id
        } else {
            holder.tvBankRrn.text = list[position].bank_rrn
        }

        holder.tvAmount.text = list[position].amount.toString()
        holder.tvAckNo.text = list[position].ack_no
        holder.tvTxnId.text = list[position].txn_id
        holder.tvCustomerMobile.text = list[position].customer_mobile.toString()
        holder.tvCustomerAadhaar.text = "xxxx-xxxx-${list[position].last_adhar}"
        holder.tvCreatedAt.text = CommonClass.formatDateTime(list[position].createdAt)
        holder.ivShare.setOnClickListener {
            click.onItemClick(holder, list, position)
        }
    }
}