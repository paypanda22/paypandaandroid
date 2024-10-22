import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.packagedetail.OtherComm
class PackageDetailAdapter(
    private val list: List<OtherComm>
) : RecyclerView.Adapter<PackageDetailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      /*  val startAmt: TextView = itemView.findViewById(R.id.startAmt)
        val endAmt: TextView = itemView.findViewById(R.id.endAmt)
        val charge: TextView = itemView.findViewById(R.id.charge)
        val chargeType: TextView = itemView.findViewById(R.id.ChargeType)
        val retailerComm: TextView = itemView.findViewById(R.id.retailer_comm)
        val retailerCommType: TextView = itemView.findViewById(R.id.RetailerCommissionType)*/
        val name: TextView = itemView.findViewById(R.id.name)
        val commissionRecyclerViewr: RecyclerView = itemView.findViewById(R.id.commissionList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lyt_package_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = list[position]
        // Set the name from the Data object
        holder.name.text = currentData.service
        // Initialize the RecyclerView for the commission list
        if (currentData.commision.isNotEmpty()) {
            val commissionAdapter = CommissionAdapter(currentData.commision)
            holder.commissionRecyclerViewr.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.commissionRecyclerViewr.adapter = commissionAdapter
        } else {
            // Handle the case where the commission list is empty
            holder.commissionRecyclerViewr.visibility = View.GONE
        }
       /* val commission = list[0].commision[position]
        if (commission != null) {
            holder.startAmt.text = commission.start_amt.toString()
            holder.endAmt.text = commission.end_amt.toString()
            holder.charge.text = commission.charge.toString()
            holder.chargeType.text = commission.charge_type
            holder.retailerComm.text = commission.commision.toString()
            holder.retailerCommType.text = commission.commision_type
        } else {
            // Handle empty commissionList case
            holder.startAmt.text = "N/A"
            holder.endAmt.text = "N/A"
            holder.charge.text = "N/A"
            holder.chargeType.text = "N/A"
            holder.retailerComm.text = "N/A"
            holder.retailerCommType.text = "N/A"
        }*/
    }


    override fun getItemCount(): Int {
        return list.size
    }
}