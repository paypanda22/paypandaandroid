package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.interfaces.DmtTxnClickListener
import app.pay.panda.responsemodels.dmttxnlist.Tran

class DmtTransactionListAdapter(
    private val activity:Activity,
    private val list:List<Tran>,
    private val clickListener: DmtTxnClickListener
) :RecyclerView.Adapter<DmtTransactionListAdapter.ViewHolder>(){
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvOpen: AppCompatTextView =itemView.findViewById(R.id.tvOpen)
        val tvStatus:AppCompatTextView=itemView.findViewById(R.id.tvStatus)
        val tvClosing:AppCompatTextView=itemView.findViewById(R.id.tvClosing)
        val tvBankName:AppCompatTextView=itemView.findViewById(R.id.tvBankName)
        val tvAccountNumber:AppCompatTextView=itemView.findViewById(R.id.tvAccountNumber)
        val tvAmount:AppCompatTextView=itemView.findViewById(R.id.tvAmount)
        val txnID:AppCompatTextView=itemView.findViewById(R.id.txnID)
        val utr:AppCompatTextView=itemView.findViewById(R.id.utr)
        val charges:AppCompatTextView=itemView.findViewById(R.id.charges)
        val commission:AppCompatTextView=itemView.findViewById(R.id.commission)
        val customer:AppCompatTextView=itemView.findViewById(R.id.customer)
        val txstatus:AppCompatTextView=itemView.findViewById(R.id.txstatus)
        val date:AppCompatTextView=itemView.findViewById(R.id.date)
        val refresh: AppCompatImageView =itemView.findViewById(R.id.refresh)
        val ivOtp:AppCompatImageView=itemView.findViewById(R.id.ivOtp)
        val ivShare:AppCompatImageView=itemView.findViewById(R.id.ivShare)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_dmt_report,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            holder.tvOpen.text="Open\n"+list[position].o_bal.toString()
            holder.tvClosing.text="Close\n"+list[position].c_bal.toString()
            holder.tvBankName.text=list[position].bank_name.toString()
            holder.tvAccountNumber.text=list[position].account_number.toString()
            holder.tvAmount.text=list[position].amount.toString()
            holder.txnID.text=list[position].txn_id.toString()
            holder.utr.text=list[position].utr.toString()
            holder.charges.text=list[position].charge.toString()
            holder.commission.text=list[position].commission.toString()
            holder.customer.text=list[position].customer_mobile.toString()
            holder.date.text=list[position].createdAt.toString()
            when(list[position].response){
                2->{
                    holder.tvStatus.text = "SUCCESS"
                    holder.refresh.setVisibility(View.GONE)
                    holder.ivOtp.setVisibility(View.GONE)
                    holder.ivShare.setVisibility(View.VISIBLE)
                    holder.txstatus.text="SUCCESS"
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                    holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.btn_success))
                    holder.txstatus.text="SUCCESS"
                }
                3->{
                    holder.tvStatus.text = "FAILED"
                    holder.refresh.setVisibility(View.VISIBLE)
                    holder.ivOtp.setVisibility(View.GONE)
                    holder.ivShare.setVisibility(View.GONE)
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.white))
                    holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.btn_failed))
                    holder.txstatus.text="FAILED"
                }
                else->{
                    holder.tvStatus.text = "IN PROCESS"
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity,R.color.black))
                    holder.refresh.setVisibility(View.GONE)
                    holder.tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.btn_pending))
                    if (list[position].tx_status.toString().toInt()==2  ||list[position].tx_status.toString().toInt()==5){
                        holder.refresh.setVisibility(View.VISIBLE)
                        holder.ivOtp.setVisibility(View.GONE)
                        holder.ivShare.setVisibility(View.GONE)
                        holder.txstatus.text="In PROCESS"
                    }else if (list[position].tx_status.toString().toInt()==3){
                        holder.refresh.setVisibility(View.GONE)
                        holder.ivOtp.setVisibility(View.VISIBLE)
                        holder.ivShare.setVisibility(View.GONE)
                        holder.txstatus.text="Refund Pending"
                    }else if (list[position].tx_status.toString().toInt()==0){
                        holder.refresh.setVisibility(View.GONE)
                        holder.ivOtp.setVisibility(View.GONE)
                        holder.ivShare.setVisibility(View.VISIBLE)
                        holder.txstatus.text="SUCCESS"
                    } else{
                        holder.refresh.setVisibility(View.VISIBLE)
                        holder.ivOtp.setVisibility(View.GONE)
                        holder.ivShare.setVisibility(View.GONE)
                        holder.txstatus.text="In PROCESS"
                    }
                }

            }

            holder.ivShare.setOnClickListener{
                if (holder.ivShare.visibility==VISIBLE){
                    clickListener.onItemClicked(holder,list,position,3)
                }
            }
            holder.ivOtp.setOnClickListener{
                if (holder.ivOtp.visibility==VISIBLE){
                    clickListener.onItemClicked(holder,list,position,2)
                }
            }
            holder.refresh.setOnClickListener{
                if (holder.refresh.visibility==VISIBLE){
                    clickListener.onItemClicked(holder,list,position,1)
                }
            }



        }catch(e:Exception){
            e.printStackTrace()
        }
    }
}