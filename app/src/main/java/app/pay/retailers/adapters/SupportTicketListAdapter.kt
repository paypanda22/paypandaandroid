package app.pay.retailers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.SupportTicketClick
import app.pay.retailers.responsemodels.supportTickets.DataX
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SupportTicketListAdapter(
    private val activity:Activity,
    private val list:List<DataX>,
    private val click:SupportTicketClick
) : RecyclerView.Adapter<SupportTicketListAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvTicketId:TextView=itemView.findViewById(R.id.tvTicketId)
        val tvStatus:TextView=itemView.findViewById(R.id.tvStatus)
        val tvSubject:TextView=itemView.findViewById(R.id.tvSubject)
        val tvMessage:TextView=itemView.findViewById(R.id.tvMessage)
        val tvDate:TextView=itemView.findViewById(R.id.tvDate)
        val tvHistory:TextView=itemView.findViewById(R.id.tvHistory)
        val tvReply:TextView=itemView.findViewById(R.id.tvReply)
        val tvDepartment:TextView=itemView.findViewById(R.id.tvDepartment)
        val txtFileName:TextView=itemView.findViewById(R.id.txtFileName)

        val tvService:TextView=itemView.findViewById(R.id.tvService)
        val edtReplyMessage:TextInputEditText=itemView.findViewById(R.id.edtReplyMessage)
        val lytReply:TextInputLayout=itemView.findViewById(R.id.lytReply)
        val btnChooseFile:Button=itemView.findViewById(R.id.btnChooseFile)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_ticket_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTicketId.text="#"+list[position].refer_id
        holder.tvStatus.text=list[position].status
        holder.tvSubject.text=list[position].subject
        holder.tvMessage.text=list[position].name+" ( "+list[position].mobile+" )"
        holder.tvDate.text=list[position].createdAt
        holder.tvDepartment.text=list[position].refer_code
        holder.tvService.text=list[position].service_id
      //  holder.ticketNo.text=list[position].tic

        holder.tvReply.setOnClickListener {
           if (holder.lytReply.visibility==VISIBLE){
               holder.lytReply.visibility= GONE
           }else{
               holder.lytReply.visibility= VISIBLE
           }
        }
        holder.lytReply.setEndIconOnClickListener {
            if (holder.edtReplyMessage.text.toString().isEmpty()){
                showToast(activity,"Enter Reply Message")
            }else{
                click.onItemClicked(holder,list,position,"reply",holder.edtReplyMessage,holder.lytReply)
            }
        }
        holder.tvHistory.setOnClickListener{
            click.onItemClicked(holder,list,position,"history",holder.edtReplyMessage,holder.lytReply)

        }
        holder.btnChooseFile.setOnClickListener {
            click.onChooseFileClick(holder.txtFileName)
        }

    }
}