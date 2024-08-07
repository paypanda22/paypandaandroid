package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.adapters.DownstreamRetailAdapter.ViewHolder
import app.pay.panda.responsemodels.viewreportdialog.Data

class ViewReportAdapter (
    private  val myActivity: Activity,
    private  val viewlist:MutableList<Data>


): RecyclerView.Adapter<ViewReportAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionID: TextView = itemView.findViewById(R.id.transactionID)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val Type: TextView = itemView.findViewById(R.id.type)
        val date: TextView = itemView.findViewById(R.id.date)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        return ViewHolder(
            LayoutInflater.from(myActivity).inflate(R.layout.lyt_item_view_report, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return viewlist.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val data = viewlist[position]
        holder.transactionID.text = data.txn_id
        holder.amount.text = data.amount
        holder.Type.text = data.trans_type
        holder.date.text = data.createdAt
    }
}