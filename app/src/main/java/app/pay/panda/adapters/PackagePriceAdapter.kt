package app.pay.panda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.databinding.LytDialogPackagePricingBinding
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.PackagePriceClick
import app.pay.panda.responsemodels.packageListResponse.Price
import app.pay.panda.retrofit.Constant.RUPEE
import com.google.android.material.card.MaterialCardView

class PackagePriceAdapter(
    private val activity: Activity,
    private val list:List<Price>,
    private val click:PackagePriceClick

):RecyclerView.Adapter<PackagePriceAdapter.ViewHolder>() {
    private var itemPosition=0
    private var clicked=false
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val tvPrice: TextView =itemView.findViewById(R.id.tvPrice)
        val tvDuration: TextView =itemView.findViewById(R.id.tvDuration)
        val tvSalePrice: TextView =itemView.findViewById(R.id.tvSalePrice)
        val tvTax: TextView =itemView.findViewById(R.id.tvTax)
        val total_amount: TextView =itemView.findViewById(R.id.total_amount)
        val mcvBackGround: MaterialCardView =itemView.findViewById(R.id.mcvBackGround)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_package_pricing,parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.tvPrice.text=RUPEE+" "+list[position].mrp.toString()
        holder.total_amount.text=RUPEE+" "+list[position].real_value.toString()
        holder.tvDuration.text=list[position].duration.toString()+" "+list[position].duration_type.toString()
        holder.tvSalePrice.text=RUPEE+" "+list[position].sale_rate.toString()
        holder.tvTax.text=list[position].tax.toString()+" "+list[position].tax_type.toString()
        setBack(holder,position)
        holder.itemView.setOnClickListener {
            itemPosition=position
            click.onPriceSelected(holder,list,position)
            clicked=true
            notifyDataSetChanged()
        }

    }

    private fun setBack(holder: PackagePriceAdapter.ViewHolder, position: Int) {
        if (clicked){
            if (position==itemPosition){
                holder.mcvBackGround.setCardBackgroundColor(ContextCompat.getColor(activity,R.color.bggrey))
            }else{
                holder.mcvBackGround.setCardBackgroundColor(ContextCompat.getColor(activity,R.color.white))
            }
        }


    }


}