package app.pay.paypanda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.responsemodels.miniStatement.Ministatement

class AepsMsAdapter(
    private val activity: Activity,
    private val list:List<Ministatement>
) :RecyclerView.Adapter<AepsMsAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val date:TextView=itemView.findViewById(R.id.date)
        val type:TextView=itemView.findViewById(R.id.type)
        val desc:TextView=itemView.findViewById(R.id.desc)
        val amount:TextView=itemView.findViewById(R.id.amount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_minitstmt_txn,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text=list[position].date
        holder.amount.text=list[position].amount.toString()
        holder.desc.text=list[position].narration
        holder.type.text=list[position].txnType
        if (list[position].txnType.lowercase()=="dr"){
            holder.amount.setTextColor(ContextCompat.getColor(activity,R.color.red))
        }else{
            holder.amount.setTextColor(ContextCompat.getColor(activity,R.color.green_700))
        }

    }
}