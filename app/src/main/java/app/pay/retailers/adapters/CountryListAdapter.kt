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
import app.pay.retailers.interfaces.CountryListClickListner
import app.pay.retailers.responsemodels.countrylist.Data

class CountryListAdapter(
    private val activity: Activity,
    private val list: List<Data>,
    private val clickListner: CountryListClickListner
) : RecyclerView.Adapter<CountryListAdapter.ViewHolder>() , Filterable {
    private var itemList: MutableList<Data> = list.toMutableList()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCode: TextView = itemView.findViewById(R.id.tvCode)

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
        holder.tvCode.text= "(${itemList[position].phone_code})"
        holder.itemView.setOnClickListener {
            clickListner.onItemClicked(holder, itemList, position)
        }
    }
}