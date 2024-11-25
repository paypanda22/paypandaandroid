package app.pay.retailers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.interfaces.StateListClickListner
import app.pay.retailers.responsemodels.statelist.Data


class StateListAdapter(
    private val activity: Activity,
    private val list: List<Data>,
    private val clickListner: StateListClickListner
) : RecyclerView.Adapter<StateListAdapter.ViewHolder>(), Filterable {

    private var itemList: MutableList<Data> = list.toMutableList()
    private var filterListener: FilterListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCode: TextView = itemView.findViewById(R.id.tvCode)
    }

    fun setFilterListener(listener: FilterListener) {
        this.filterListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_search_item_name, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
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
                    // Notify the listener about the number of filtered items
                    filterListener?.onFilterResult(newItems.size)
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = itemList[position].name
        holder.tvCode.visibility = View.GONE
        holder.itemView.setOnClickListener {
            clickListner.onItemClicked(holder, itemList, position)
        }
    }

    interface FilterListener {
        fun onFilterResult(count: Int)
    }
}
