import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.packagedetail.Commision

class CommissionAdapter(
    private val commissionList: List<Commision>) :
    RecyclerView.Adapter<CommissionAdapter.CommissionViewHolder>() {

    class CommissionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startAmt: TextView = itemView.findViewById(R.id.startAmt)
        val endAmt: TextView = itemView.findViewById(R.id.endAmt)
        val charge: TextView = itemView.findViewById(R.id.charge)
        val chargeType: TextView = itemView.findViewById(R.id.ChargeType)
        val retailerComm: TextView = itemView.findViewById(R.id.retailer_comm)
        val retailerCommType: TextView = itemView.findViewById(R.id.RetailerCommissionType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lyt_commission_list, parent, false)
        return CommissionViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommissionViewHolder, position: Int) {
        val commission = commissionList[position]
        holder.startAmt.text = commission.start_amt.toString()
        holder.endAmt.text = commission.end_amt.toString()
        holder.charge.text = commission.charge.toString()
        holder.chargeType.text = commission.charge_type
        holder.retailerComm.text = commission.commision.toString()
        holder.retailerCommType.text = commission.commision_type
    }

    override fun getItemCount(): Int {
        return commissionList.size
    }
}
