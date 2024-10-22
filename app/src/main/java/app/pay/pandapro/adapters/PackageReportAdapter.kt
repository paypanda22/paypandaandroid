package app.pay.pandapro.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.AppCompatTextView

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.packagehistory.Data


class PackageReportAdapter(
    private val activity: Activity,
    private val list: List<Data>,

    ) : RecyclerView.Adapter<PackageReportAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val tvBankName: AppCompatTextView =itemView.findViewById(R.id.tvBankName)
        val tvAccountNumber: AppCompatTextView =itemView.findViewById(R.id.tvAccountNumber)
        val tvAmount: AppCompatTextView =itemView.findViewById(R.id.tvAmount)
        val duration: AppCompatTextView =itemView.findViewById(R.id.duration)
        val DurationType: AppCompatTextView =itemView.findViewById(R.id.DurationType)
        val MRP: AppCompatTextView =itemView.findViewById(R.id.MRP)
        val referId: AppCompatTextView =itemView.findViewById(R.id.referId)
        val tax: AppCompatTextView =itemView.findViewById(R.id.tax)
        val taxtype: AppCompatTextView =itemView.findViewById(R.id.taxtype)

        val date: AppCompatTextView =itemView.findViewById(R.id.date)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_package_report_history, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.date.text=list[position].createdAt.toString()
        holder.tvBankName.text=""+list[position].package_name.toString()
        holder.duration.text=list[position].duration.toString()
        holder.DurationType.text=list[position].duration_type.toString()
        holder.tvAmount.text="Purchase Amt : "+list[position].amount.toString()
        holder.MRP.text=""+list[position].mrp.toString()
        holder.referId.text=""+list[position].refer_id.toString()
        holder.tax.text=""+list[position].tax.toString()
        holder.taxtype.text=""+list[position].tax_type.toString()
        holder.tvAccountNumber.text="state: "+list[position].packageid.state.toString()

    }
}