package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.interfaces.TransferToClickListner
import app.pay.panda.responsemodels.transferTo.Data

class TransferToAdapter(
    private val activity: Activity,
    private val list: List<Data>,
    private val clickListener: TransferToAdapter.TransferToClickListener
) : RecyclerView.Adapter<TransferToAdapter.ViewHolder>() {

    // Declare selectedPosition variable
    private var selectedPosition = RecyclerView.NO_POSITION

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnName: AppCompatButton = itemView.findViewById(R.id.btnName)

        init {
            // Set click listener for the button inside ViewHolder constructor
            btnName.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Update selected position
                    selectedPosition = position
                    // Notify adapter that data set changed
                    notifyDataSetChanged()
                    // Inform listener about click event
                    clickListener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.lyt_rv_btn_txt, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnName.text = list[position].title
        if (position == selectedPosition) {
            holder.btnName.background = ContextCompat.getDrawable(activity, R.drawable.submitt_btn_small_green)
        } else {
            holder.btnName.background = ContextCompat.getDrawable(activity, R.drawable.submitt_btn_small_white)
        }
    }
    
    interface TransferToClickListener {
        fun onItemClick(position: Int)
    }
}