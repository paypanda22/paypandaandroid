package app.pay.paypanda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.responsemodels.aepsWalletReport.Wallet


class AepsWalletAdapter(
    private val activity: Activity,
    private val list: List<Wallet>,

) : RecyclerView.Adapter<AepsWalletAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOpen: AppCompatTextView =itemView.findViewById(R.id.tvOpen)
        val tvStatus: AppCompatTextView =itemView.findViewById(R.id.tvStatus)
        val tvClosing: AppCompatTextView =itemView.findViewById(R.id.tvClosing)
        val tvBankName: AppCompatTextView =itemView.findViewById(R.id.tvBankName)
        val tvAccountNumber: AppCompatTextView =itemView.findViewById(R.id.tvAccountNumber)
        val tvAmount: AppCompatTextView =itemView.findViewById(R.id.tvAmount)
        val txnID: AppCompatTextView =itemView.findViewById(R.id.txnID)
        val utr: AppCompatTextView =itemView.findViewById(R.id.utr)
        val charges: AppCompatTextView =itemView.findViewById(R.id.charges)
        val commission: AppCompatTextView =itemView.findViewById(R.id.commission)
        val customer: AppCompatTextView =itemView.findViewById(R.id.customer)
        val txstatus: AppCompatTextView =itemView.findViewById(R.id.txstatus)
        val date: AppCompatTextView =itemView.findViewById(R.id.date)
        val refresh: AppCompatImageView =itemView.findViewById(R.id.refresh)
        val ivOtp:AppCompatImageView=itemView.findViewById(R.id.ivOtp)
        val ivShare:AppCompatImageView=itemView.findViewById(R.id.ivShare)
        val view: ImageView =itemView.findViewById(R.id.viewdata)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_aeps_wallet_report, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvOpen.text="Open\n"+list[position].o_bal.toString()
        holder.tvClosing.text="Close\n"+list[position].c_bal.toString()
        holder.tvBankName.text=list[position].order_id
      //  holder.tvBankName.text=list[position].toString()
        holder.tvAccountNumber.text=list[position].message.toString()
        holder.tvAmount.text=list[position].amount.toString()
        holder.txnID.text=list[position].txn_id.toString()
        holder.utr.text=list[position].type.toString()
        holder.charges.text=list[position].is_by_admin.toString()
        holder.txstatus.text=list[position].approve.toString()

        holder.date.text=list[position].createdAt.toString()
       /* holder.viewdata.setOnClickListener{
            click.onItemClickInvoice(holder, list, position)
        }

        holder.refresh.setOnClickListener{
            click.onItemClickEnquery(holder, list, position,list[position].type)
        }*/
   }
}