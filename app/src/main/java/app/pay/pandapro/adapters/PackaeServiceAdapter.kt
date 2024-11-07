package app.pay.pandapro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.PackageServices.Data

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
        // Get the data object at the current position
        val currentData = list[position]

        // Set the name from the current data object
        holder.name.text = currentData.name

        // Check if the slots list is not empty
        if (currentData.slots.isNotEmpty()) {
            // You likely want the first slot (index 0), or you could loop through the slots if needed
            val service = currentData.slots[0]

            // Bind slot data to the views
            holder.startAmt.text = service.start_amt.toString()
            holder.endAmt.text = service.end_amt.toString()
            holder.charge.text = service.charge.toString()
            holder.chargeType.text = service.charge_type
            holder.retailerComm.text = service.commision.toString()
            holder.retailerCommType.text = service.commision_type

        } else {
            // Handle the case where the slots list is empty
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