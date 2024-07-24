package app.pay.panda.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.responsemodels.notification.Notification

class NotificationListAdapter(
    private val activity: Activity,
    private val list:List<Notification>

) :RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvSubject:TextView=itemView.findViewById(R.id.tvSubject)
        val tvDescription:TextView=itemView.findViewById(R.id.tvDescription)
        val tvDate:TextView=itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_notifications,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSubject.text=list[position].subject
        holder.tvDescription.text=list[position].message
        holder.tvDate.text=list[position].createdAt
    }
}