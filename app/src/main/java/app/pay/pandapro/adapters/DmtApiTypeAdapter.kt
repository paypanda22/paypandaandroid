package app.pay.pandapro.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.dmtSettings.DmtApiType

class DmtApiTypeAdapter(
    private val activity : Activity,
    private val list:List<DmtApiType>,
    private val click:DmtTypeClickListener,
    private val defaultPosition:Int
) : RecyclerView.Adapter<DmtApiTypeAdapter.ViewHolder>() {
    private var selectedPosition = -1
    fun setDefaultSelectedPosition() {
        if (defaultPosition in list.indices) {
            selectedPosition = defaultPosition
            notifyDataSetChanged()
        }
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val btnName: AppCompatButton = itemView.findViewById(R.id.btnName)
    }

    interface DmtTypeClickListener {
        fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<DmtApiType>,pos:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_btn_txt,parent,false) )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = list[position]
        holder.btnName.text = item.name
        if (position == selectedPosition) {
            holder.btnName.background = ContextCompat.getDrawable(activity, R.drawable.submitt_btn_small_green)
        } else {
            holder.btnName.background = ContextCompat.getDrawable(activity, R.drawable.submitt_btn_small_white)
        }
        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            click.onItemClicked(holder, list, position)
        }

    }

}