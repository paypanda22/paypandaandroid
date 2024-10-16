package app.pay.paypanda.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.responsemodels.dmtTxnByBath.AllTran

class DmtTxnReceiptAdapter(
    private val context:Context,
    private val list:List<AllTran>
) :RecyclerView.Adapter<DmtTxnReceiptAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvAmount:TextView=itemView.findViewById(R.id.tvAmount)
        val tvUtr:TextView=itemView.findViewById(R.id.tvUtr)
        val tvStatus:TextView=itemView.findViewById(R.id.tvStatus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.lyt_rv_txn_receipt_list,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvAmount.text=list[position].amount.toString()
        holder.tvStatus.text=list[position].status.toString().uppercase()
        holder.tvUtr.text=list[position].utr.toString()
    }
}