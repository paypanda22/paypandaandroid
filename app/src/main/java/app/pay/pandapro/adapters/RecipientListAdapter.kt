package app.pay.pandapro.adapters

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.interfaces.RecipientListClickListner
import app.pay.pandapro.responsemodels.dmtBeneficiaryList.Data

class RecipientListAdapter(
    private val activity:Activity,
    private val list:List<Data>,
    private val clickListner: RecipientListClickListner,
    private val deleteRecipient:DeleteRecipient
) : RecyclerView.Adapter<RecipientListAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvRecipientName:TextView=itemView.findViewById(R.id.tvRecipientName)
        val tvBankName:TextView=itemView.findViewById(R.id.tvBankName)
        val tvAccountNumber:TextView=itemView.findViewById(R.id.tvAccountNumber)
        val ivDelete:ImageView=itemView.findViewById(R.id.ivDelete)
        val tvVerified:TextView =itemView.findViewById(R.id.tvVerified)
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
            if (list[position].isVerified){
                holder.tvVerified.visibility=VISIBLE
            }else{
                holder.tvVerified.visibility= GONE
            }

            holder.itemView.setOnClickListener{clickListner.onRecipientItemClick(holder,list,position)}
            holder.ivDelete.setOnClickListener {
                val builder = AlertDialog.Builder(activity)
                builder.setMessage("Do you want to delete this Recipient?")
                    .setTitle("Delete Recipient!")
                    .setIcon(R.drawable.ic_cancel_red) // Replace with your desired icon resource
                    .setNegativeButton("NO") { dialog, which ->

                    }
                    .setPositiveButton("DELETE") { dialog, which ->
                        deleteRecipient.onDeleteIconClicked(holder, list, position)

                    }

                val alertDialog = builder.create()
                alertDialog.show()

// Access and customize the buttons
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(activity, R.color.red_700)) // Positive button background
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(activity, R.color.grey_4)) // Negative button background
               }

        }catch(e:Exception){
            e.printStackTrace()
        }
    }
}

interface DeleteRecipient {
    fun onDeleteIconClicked(holder:RecyclerView.ViewHolder,model:List<Data>,pos:Int)
}