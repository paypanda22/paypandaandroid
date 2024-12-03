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
import app.pay.retailers.interfaces.BbpsCategoryClick
import app.pay.retailers.responsemodels.bbpscatagory.Data

class CategoryAdapter (
    private val activity: Activity,
    private val catList: List<Data>,
    private val click: BbpsCategoryClick
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), Filterable {

    private var itemList: MutableList<Data> = catList.toMutableList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catName: TextView = itemView.findViewById(R.id.catName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        return CategoryAdapter.ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_category_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        holder.catName.text = itemList[position].service_name
        holder.itemView.setOnClickListener {
            click.onItemClicked(holder, itemList, position)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    catList.toMutableList()
                } else {
                    catList.filter {
                        it.service_name.contains(constraint, ignoreCase = true)
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
