package app.pay.pandapro.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.interfaces.AepsBankClick
import app.pay.pandapro.responsemodels.aepsBanklist.Data

class AepsBanksAdapter(
    private val activity: Activity,
    private val list:List<Data>,
    private val click: AepsBankClick

):RecyclerView.Adapter<AepsBanksAdapter.ViewHolder>() , Filterable {
    private var itemList: MutableList<Data> = list.toMutableList()
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvName:TextView=itemView.findViewById(R.id.tvName)
        val tvCode:TextView=itemView.findViewById(R.id.tvCode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_search_item_name,parent,false))
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
                        it.bankName.contains(constraint, ignoreCase = true)
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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCode.visibility=View.GONE
        holder.tvName.text=itemList[position].bankName
        holder.itemView.setOnClickListener {
            click.onItemClicked(holder,itemList,position)
        }

    }


}