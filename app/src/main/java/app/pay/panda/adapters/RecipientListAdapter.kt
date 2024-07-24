package app.pay.panda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.interfaces.RecipientListClickListner
import app.pay.panda.responsemodels.dmtBeneficiaryList.Data

class RecipientListAdapter(
    private val activity:Activity,
    private val list:List<Data>,
    private val clickListner: RecipientListClickListner
) : RecyclerView.Adapter<RecipientListAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvRecipientName:TextView=itemView.findViewById(R.id.tvRecipientName)
        val tvBankName:TextView=itemView.findViewById(R.id.tvBankName)
        val tvAccountNumber:TextView=itemView.findViewById(R.id.tvAccountNumber)
        val ivDelete:ImageView=itemView.findViewById(R.id.ivDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_beneficiary_list,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            holder.tvRecipientName.text=list[position].recipient_name
            holder.tvAccountNumber.text=list[position].account.toString()
            holder.tvBankName.text=list[position].bank

            holder.itemView.setOnClickListener{clickListner.onItemClicked(holder,list,position)}
            holder.ivDelete.setOnClickListener {
                Toast.makeText(activity,"Delete Api Required",Toast.LENGTH_SHORT).show()
            }

        }catch(e:Exception){
            e.printStackTrace()
        }
    }
}