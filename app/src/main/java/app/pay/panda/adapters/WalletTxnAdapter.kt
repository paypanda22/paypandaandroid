package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.responsemodels.walletTxn.Wallet
import com.google.android.material.card.MaterialCardView

class WalletTxnAdapter(
    private val activity: Activity,
    private val list:List<Wallet>
) : RecyclerView.Adapter<WalletTxnAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val mcvWalletTxn:MaterialCardView=itemView.findViewById(R.id.mcvWalletTxn)
        val tvTxnTitle:TextView=itemView.findViewById(R.id.tvTxnTitle)
        val tvTxnMessage:TextView=itemView.findViewById(R.id.tvTxnMessage)
        val amount:TextView=itemView.findViewById(R.id.amount)
        val openingBal:TextView=itemView.findViewById(R.id.openingBal)
        val closingBal:TextView=itemView.findViewById(R.id.closingBal)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_wallet_txn,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvTxnMessage.text=list[position].createdAt
        holder.tvTxnTitle.text=list[position].message
        holder.openingBal.text=list[position].o_bal.toString()
        holder.closingBal.text=list[position].c_bal.toString()
        if (list[position].type=="credit"){
            holder.amount.text="+"+list[position].amount.toString()
            holder.amount.setTextColor(ContextCompat.getColor(activity,R.color.green_700))
        }else{
            holder.amount.text="-"+list[position].amount.toString()
            holder.amount.setTextColor(ContextCompat.getColor(activity,R.color.red))
        }
    }
}