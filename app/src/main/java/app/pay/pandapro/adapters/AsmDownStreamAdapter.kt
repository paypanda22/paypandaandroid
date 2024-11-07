package app.pay.pandapro.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.interfaces.DownStreamAsmClick
import app.pay.pandapro.interfaces.DownStreamRetailerClick
import app.pay.pandapro.responsemodels.asmresponse.Data

class AsmDownStreamAdapter (
private val activity: Activity,
private val downstramlistRetailer: MutableList<Data>,
 private val clickRListner: DownStreamAsmClick
) : RecyclerView.Adapter<AsmDownStreamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NAme: TextView = itemView.findViewById(R.id.NAme)
        val refer_id: TextView = itemView.findViewById(R.id.refer_id)
        val main_wallet: TextView = itemView.findViewById(R.id.main_wallet)
        val is_approved: TextView = itemView.findViewById(R.id.is_approved)
        val SrNo: TextView = itemView.findViewById(R.id.SrNo)
        val retailer: TextView = itemView.findViewById(R.id.retailer)
        val UserType: TextView = itemView.findViewById(R.id.usertype)
           val transfer: TextView = itemView.findViewById(R.id.transfer)
           val Reversetransfer: TextView = itemView.findViewById(R.id.Reversetransfer)
           val report: ImageView = itemView.findViewById(R.id.report)
        val walletreport: TextView = itemView.findViewById(R.id.walletreport)
        val email: TextView = itemView.findViewById(R.id.email)
        val mobile: TextView = itemView.findViewById(R.id.Mobile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity)
                .inflate(R.layout.lyt_asm_downstream, parent, false)
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
        holder.email.text = "Email: ${data.email}"
        holder.mobile.text = " ${data.mobile}"
        if(data.is_approved==true){
            holder.is_approved.text = "Status:- Approved"
        }else{
            holder.is_approved.text = "Status:- Pending"
        }

       holder.UserType.text = "User Type:- " + data.user_type

        holder.retailer.setOnClickListener {
            clickRListner.onItemClicked(
                holder,
                downstramlistRetailer,
                position
            )
        }
          holder.transfer.setOnClickListener {
             clickRListner.onTransferMoneyClicked(holder, downstramlistRetailer, position)
          }

          holder.Reversetransfer.setOnClickListener {
              clickRListner.onReverseMoneyClicked(holder, downstramlistRetailer, position)
          }
        holder.report.setOnClickListener {
            clickRListner.onViewReportClicked(holder, downstramlistRetailer, position)
        }
        holder.walletreport.setOnClickListener {
            clickRListner.onViewWalletReportClicked(holder, downstramlistRetailer, position)
        }
    }
}