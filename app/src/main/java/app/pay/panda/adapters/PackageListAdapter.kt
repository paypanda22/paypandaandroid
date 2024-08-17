package app.pay.panda.adapters

import android.app.Activity
import android.net.Uri
import android.text.Html
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Arrangement
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.interfaces.PackageListClickListener
import app.pay.panda.responsemodels.packageListResponse.Data
import app.pay.panda.retrofit.Constant
import de.hdodenhof.circleimageview.CircleImageView

class PackageListAdapter(
    private val activity: Activity,
    private val list: List<Data>,
    private val click:PackageListClickListener
) : RecyclerView.Adapter<PackageListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: CircleImageView = itemView.findViewById(R.id.ivIcon)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val tvBuyNow: TextView = itemView.findViewById(R.id.tvBuyNow)
        val rvServices: RecyclerView = itemView.findViewById(R.id.rvServices)
        val tvMore: TextView = itemView.findViewById(R.id.tvMore)
        val tvLess: TextView = itemView.findViewById(R.id.tvLess)
        val commission: ImageView = itemView.findViewById(R.id.commission)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_package_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        MyGlide.with(activity, Uri.parse(Constant.PIMAGE_URL + list[position].icon_img), holder.ivIcon)
        holder.tvTitle.text = list[position].package_name
        val desc = Html.fromHtml(list[position].description, Html.FROM_HTML_MODE_COMPACT)
        holder.tvDesc.text = desc
        if (desc.toString().length > 80) {
            holder.tvMore.visibility = VISIBLE
            holder.tvMore.setOnClickListener {
                setMaxLength(holder.tvDesc, desc.toString().length)
                holder.tvDesc.text = desc
                holder.tvMore.visibility = GONE
                holder.tvLess.visibility = VISIBLE
            }
            holder.tvLess.setOnClickListener {
                setMaxLength(holder.tvDesc, 80)
                holder.tvDesc.text = desc
                holder.tvMore.visibility = VISIBLE
                holder.tvLess.visibility = GONE
            }

        }
        if(list[position].isPaid==true){
            holder.tvBuyNow.visibility= VISIBLE
        }else{
            holder.tvBuyNow.visibility= GONE
        }

        if (list[position].services.isNotEmpty()) {
            val servicesAdapter = PackageServicesAdapter(activity, list[position].services)
            holder.rvServices.adapter = servicesAdapter
            holder.rvServices.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }

        holder.tvBuyNow.setOnClickListener {
            click.onItemClicked(holder,list,position)
        }
        holder.commission.setOnClickListener{
            click.onItemClickedDetail(holder,list,position)
        }
    }

    private fun setMaxLength(tvDesc: TextView, length: Int) {
        val filterArray = arrayOf(InputFilter.LengthFilter(length))
        tvDesc.filters = filterArray
    }
}