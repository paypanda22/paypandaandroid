package app.pay.pandapro.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.responsemodels.getOperators.Data
import app.pay.pandapro.interfaces.OplistClickListner

class OperatorListAdapter(
    private val activity: Activity,
    private val list:List<Data>,
    private val clickListner: OplistClickListner
) : RecyclerView.Adapter<OperatorListAdapter.ViewHolder>(), Filterable {
    private var itemList: MutableList<Data> = list.toMutableList()
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivOperatorImage:ImageView=itemView.findViewById(R.id.ivOperatorImage)
        val tvOperatorName:TextView=itemView.findViewById(R.id.tvOperatorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_op_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOperatorName.text=itemList[position].name
        holder.itemView.setOnClickListener {
            clickListner.onItemClicked(holder,itemList,position)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    list.toMutableList()
                } else {
                    list.filter {
                        it.name.contains(constraint, ignoreCase = true)
                    }.toMutableList()
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.values?.let {
                    @Suppress("UNCHECKED_CAST")
                    val newItems = it as List<Data>
                    updateList(newItems)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(newItems: List<Data>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }
}