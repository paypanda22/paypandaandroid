package app.pay.panda.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.responsemodels.PackageServices.Data
import app.pay.panda.responsemodels.PackageServices.Slot
import app.pay.panda.responsemodels.packagedetail.OtherComm

class PackaeServiceAdapter (
    private val list: List<Data>
) : RecyclerView.Adapter<PackaeServiceAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val startAmt: TextView = itemView.findViewById(R.id.StartAmt)
        val endAmt: TextView = itemView.findViewById(R.id.EndAmt)
        val charge: TextView = itemView.findViewById(R.id.Charge)
        val chargeType: TextView = itemView.findViewById(R.id.ChargeType)
        val retailerComm: TextView = itemView.findViewById(R.id.Retailer_Commission)
        val retailerCommType: TextView = itemView.findViewById(R.id.CommissionType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lyt_package_services_table, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Check if the slots list is not empty and the index is within bounds
        val currentData = list[position]

        // Set the name from the Data object
        holder.name.text = currentData.name
        if (list[position].slots.isNotEmpty() && position < list[position].slots.size) {
            val service = list[position].slots[position]
          //  holder.name.text = list[position].name.toString()
            holder.startAmt.text = service.start_amt.toString()
            holder.endAmt.text = service.end_amt.toString()
            holder.charge.text = service.charge.toString()
            holder.chargeType.text = service.charge_type.toString()
            holder.retailerComm.text = service.commision.toString()
            holder.retailerCommType.text = service.commision_type.toString()


        } else {
            // Handle case where slots list is empty or the index is out of bounds
            holder.startAmt.text = "N/A"
            holder.endAmt.text = "N/A"
            holder.charge.text = "N/A"
            holder.chargeType.text = "N/A"
            holder.retailerComm.text = "N/A"
            holder.retailerCommType.text = "N/A"
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
}