package app.pay.panda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.interfaces.UserBankAccountClick
import app.pay.panda.responsemodels.payoutBanks.Data

class PayoutAccountAdapter(
    private val activity:Activity,
    private val list:List<Data>,
    private val click:UserBankAccountClick,
    private val onLongPressed:PayoutAccountOnLongPressed
) :RecyclerView.Adapter<PayoutAccountAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvAcName:TextView=itemView.findViewById(R.id.tvAcName)
        val tvBankName:TextView=itemView.findViewById(R.id.tvBankName)
        val tvAcNumber:TextView=itemView.findViewById(R.id.tvAcNumber)
        val tvBranch:TextView=itemView.findViewById(R.id.tvBranch)
        val tvIfsc:TextView=itemView.findViewById(R.id.tvIfsc)
        val tvType:TextView=itemView.findViewById(R.id.tvType)

    }

    interface PayoutAccountOnLongPressed {
        fun onLongPressed(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_user_bank_accounts,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.tvAcName.text=list[position].bank_account_name
        holder.tvIfsc.text=list[position].bank_ifsc
        holder.tvType.text=list[position].account_type
        holder.tvAcNumber.text=list[position].bank_account_number.toString()
        holder.tvBankName.text=list[position].bank_name
        holder.tvBranch.text=list[position].bank_branch
        holder.itemView.setOnClickListener {
            click.onItemCLicked(holder,list,position)
        }
    }
}