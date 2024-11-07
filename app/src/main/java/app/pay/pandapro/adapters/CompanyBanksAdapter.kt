package app.pay.pandapro.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.companyBanks.Data

class CompanyBanksAdapter(
    private val activity: Activity,
    private val list:List<Data>
):RecyclerView.Adapter<CompanyBanksAdapter.ViewHolder>() {
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val tvBankName:TextView=itemView.findViewById(R.id.tvBankName)
        val tvIfsc:TextView=itemView.findViewById(R.id.tvIfsc)
        val tvBankCode:TextView=itemView.findViewById(R.id.tvBankCode)
        val tvBranchName:TextView=itemView.findViewById(R.id.tvBranchName)
        val tvRemark:TextView=itemView.findViewById(R.id.tvRemark)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_company_banks,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBankName.text=list[position].bank_name
        holder.tvBankCode.text=list[position].bank_account_number
        holder.tvIfsc.text=list[position].ifsc_code
        holder.tvBranchName.text=list[position].branch_name
        holder.tvRemark.text=list[position].remark
    }
}