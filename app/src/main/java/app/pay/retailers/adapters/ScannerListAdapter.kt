package app.pay.retailers.adapters

import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.helperclasses.FingerPrintScanner
import app.pay.retailers.helperclasses.MyGlide
import app.pay.retailers.interfaces.ScannerListClick
import app.pay.retailers.retrofit.Constant

class ScannerListAdapter(
    private val activity:Activity,
    private val list:List<FingerPrintScanner>,
    private val click: ScannerListClick
) :RecyclerView.Adapter<ScannerListAdapter.ViewHolder>() {
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivImage:ImageView=itemView.findViewById(R.id.ivImage)
        val tvName:TextView=itemView.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_rv_scanner,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        MyGlide.with(activity, Uri.parse(Constant.PIMAGE_URL+list[position].getImageURL()),holder.ivImage)
        holder.tvName.text=list[position].getDeviceName()
        holder.itemView.setOnClickListener {
            click.onItemClicked(holder,list,position)
        }
    }
}