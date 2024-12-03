package app.pay.retailers.adapters

import SetGridViewHeightBasedOnChildren
import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.responsemodels.ticketHistory.Data

class TicketHistoryAdapter (
    private val activity: Activity,
    private val list: List<Data>
) : RecyclerView.Adapter<TicketHistoryAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val operator: TextView =itemView.findViewById(R.id.operator)
        val Chat: TextView =itemView.findViewById(R.id.Chat)
        val createdAt: TextView =itemView.findViewById(R.id.createdAt)
        val images: GridView =itemView.findViewById(R.id.gridView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lyt_ticket_history, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val desc = Html.fromHtml(list[position].chat, Html.FROM_HTML_MODE_COMPACT)
        holder.Chat.text = desc
        holder.operator.text=list[position].operator
        holder.createdAt.text=list[position].createdAt


        val imageUrls = list[position].attachments // Directly use the list if it's already in the required format

        // Initialize ImageGridAdapter with image URLs

        val adapter = ImageGridAdapter(activity, imageUrls)
        if(list[position].attachments.isNotEmpty()) {
            holder.images.adapter = adapter
            SetGridViewHeightBasedOnChildren(holder.images, 4)
        }else{
            holder.images.visibility=View.GONE
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}
