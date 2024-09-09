package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.responsemodels.earningreport.Wallet


class EarningReportAdapter (
    private val activity: Activity,
    private val earningReportlist: MutableList<Wallet>,

): RecyclerView.Adapter<EarningReportAdapter.ViewHolder>() , Filterable {
    private var itemList: MutableList<Wallet> = earningReportlist.toMutableList()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Date: TextView =itemView.findViewById(R.id.Date)
        val order_id: TextView =itemView.findViewById(R.id.order_id)
        val transactionID: TextView =itemView.findViewById(R.id.transactionID)
        val amount: TextView =itemView.findViewById(R.id.amount)
        val userID: TextView =itemView.findViewById(R.id.userID)
        val TransactionType: TextView =itemView.findViewById(R.id.TransactionType)
        val Approved: TextView =itemView.findViewById(R.id.Approved)
        val type: TextView =itemView.findViewById(R.id.type)
        val message: TextView =itemView.findViewById(R.id.message)
        val opening: TextView =itemView.findViewById(R.id.opening)
        val closing: TextView =itemView.findViewById(R.id.closing)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EarningReportAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_earning_report_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EarningReportAdapter.ViewHolder, position: Int) {

        holder.Date.text = "Date:- "+earningReportlist.get(position).updatedAt
        holder.order_id.text = "order id:- "+earningReportlist.get(position).order_id
        holder.transactionID.text = earningReportlist.get(position).txn_id
        holder.amount.text = earningReportlist.get(position).amount
        holder.userID.text = "User ID:- "+earningReportlist.get(position).userid
        holder.TransactionType.text = earningReportlist.get(position).trans_type
        holder.Approved.text = earningReportlist.get(position).approve
        holder.type.text = earningReportlist.get(position).type
        holder.message.text = earningReportlist.get(position).message
        holder.opening.text = earningReportlist.get(position).o_bal
        holder.closing.text = earningReportlist.get(position).c_bal
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    earningReportlist.toMutableList()
                } else {
                    val filterPattern = constraint.toString().trim().toLowerCase()
                    earningReportlist.filter {
                        it.txn_id.contains(filterPattern, ignoreCase = true) ||
                                it.trans_type.contains(filterPattern, ignoreCase = true)
                    }.toMutableList()
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.values?.let {
                    @Suppress("UNCHECKED_CAST")
                    val newItems = it as List<Wallet>
                    updateList(newItems)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(newItems: List<Wallet>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }
}
