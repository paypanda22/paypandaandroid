package app.pay.retailers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.interfaces.DownStreamRetailerClick

class DownStreamDistributerAdapter(
    private val activity: Activity,
    private val downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.downstramdistributer.Data>,
    private val downStreamClick: DownStreamRetailerClick
) : RecyclerView.Adapter<DownStreamDistributerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NAme: TextView = itemView.findViewById(R.id.NAme)
        val refer_id: TextView = itemView.findViewById(R.id.refer_id)
        val main_wallet: TextView = itemView.findViewById(R.id.main_wallet)
        val is_approved: TextView = itemView.findViewById(R.id.is_approved)
        val SrNo: TextView = itemView.findViewById(R.id.SrNo)
        val transfer: TextView = itemView.findViewById(R.id.transfer)
        val Reversetransfer: TextView = itemView.findViewById(R.id.Reversetransfer)
        val email: TextView = itemView.findViewById(R.id.email)
        val mobile: TextView = itemView.findViewById(R.id.Mobile)
        val report: ImageView = itemView.findViewById(R.id.report)
        val walletreport: Button = itemView.findViewById(R.id.walletreport)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_donstream_dist_adp, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return downstramlistRetailer.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = downstramlistRetailer[position]
        holder.SrNo.text = (position + 1).toString()
        holder.NAme.text = data.name
        holder.refer_id.text = "Refer ID:-" + data.refer_id
        holder.main_wallet.text = "Balance:- " + data.main_wallet
        holder.is_approved.text = "Approved:- " + data.is_approved
        holder.email.text = "Email: ${data.email}"
        holder.mobile.text = " ${data.mobile}"
        holder.transfer.setOnClickListener {
            downStreamClick.onTransferMoneyClicked(holder, downstramlistRetailer, position)
        }

        holder.Reversetransfer.setOnClickListener {
            downStreamClick.onReverseMoneyClicked(holder, downstramlistRetailer, position)
        }
        holder.walletreport.setOnClickListener{
            downStreamClick.onViewWalletReportClicked(holder, downstramlistRetailer, position)
        }
holder.report.setOnClickListener{
    downStreamClick.onViewReportClicked(holder, downstramlistRetailer, position)
}
    }
}
