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
import app.pay.pandapro.interfaces.AsmClick


class AsmAdapter (
private val activity: Activity,
private val downstramlistDistributer: MutableList<app.pay.pandapro.responsemodels.asmresponse.Data>,
private val asmClick: AsmClick
) : RecyclerView.Adapter<AsmAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NAme: TextView = itemView.findViewById(R.id.NAme)
        val refer_id: TextView = itemView.findViewById(R.id.refer_id)
        val main_wallet: TextView = itemView.findViewById(R.id.main_wallet)
        val is_approved: TextView = itemView.findViewById(R.id.is_approved)
        val SrNo: TextView = itemView.findViewById(R.id.SrNo)
        val transfer: TextView = itemView.findViewById(R.id.transfer)
        val Reversetransfer: TextView = itemView.findViewById(R.id.Reversetransfer)
        val walletreport: TextView = itemView.findViewById(R.id.walletreport)
        val retailer: TextView = itemView.findViewById(R.id.retailer)
        val UserType: TextView = itemView.findViewById(R.id.usertype)
        val report: ImageView = itemView.findViewById(R.id.report)
        val email: TextView = itemView.findViewById(R.id.email)
        val mobile: TextView = itemView.findViewById(R.id.Mobile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_asm, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return downstramlistDistributer.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = downstramlistDistributer[position]
        holder.SrNo.text = (position + 1).toString()
        holder.NAme.text = data.name
        holder.refer_id.text = "Refer ID:-" + data.refer_id
        holder.main_wallet.text = "Balance:- " + data.main_wallet
        holder.UserType.text = "User Type:- " + data.user_type
        holder.email.text = "Email: ${data.email}"
        holder.mobile.text = " ${data.mobile}"
        if(data.is_approved==true){
            holder.is_approved.text = "Status:- Approved"
        }else{
            holder.is_approved.text = "Status:- Pending"
        }
        holder.retailer.setOnClickListener {
            asmClick.onItemClicked(
                holder,
                downstramlistDistributer,
                position
            )
        }
        holder.transfer.setOnClickListener {
            asmClick.onTransferMoneyClicked(holder, downstramlistDistributer, position)
        }

        holder.Reversetransfer.setOnClickListener {
            asmClick.onReverseMoneyClicked(holder, downstramlistDistributer, position)
        }
        holder.report.setOnClickListener {
            asmClick.onViewReportClicked(holder, downstramlistDistributer, position)
        }
        holder.walletreport.setOnClickListener {
            asmClick.onViewWalletReportClicked(holder, downstramlistDistributer, position)
        }

    }
}