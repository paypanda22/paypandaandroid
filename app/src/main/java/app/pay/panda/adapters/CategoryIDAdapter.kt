package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.interfaces.BbpsCategoryIDClick
import app.pay.panda.responsemodels.CategoryIdResponse.Data

class CategoryIDAdapter(
    private val activity: Activity,
    private val categoryList: List<Data>,
    private val click: BbpsCategoryIDClick
) : RecyclerView.Adapter<CategoryIDAdapter.ViewHolder>(), Filterable {

    private var itemList: MutableList<Data> = categoryList.toMutableList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val catName: TextView = itemView.findViewById(R.id.catName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_category_id_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.catName.text = itemList[position].name
        holder.itemView.setOnClickListener {
            click.onItemClickedId(holder, itemList, position)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    categoryList.toMutableList()
                } else {
                    categoryList.filter {
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
