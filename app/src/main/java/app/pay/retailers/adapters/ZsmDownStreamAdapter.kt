package app.pay.retailers.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.R
import app.pay.retailers.interfaces.RetailerClick
import app.pay.retailers.interfaces.ZSMDownStreamClick

class ZsmDownStreamAdapter (
    private val activity: Activity,
    private val downstramlist: MutableList<app.pay.retailers.responsemodels.zsmresponse.Data>,
    private val downStreamClick: ZSMDownStreamClick
) : RecyclerView.Adapter<ZsmDownStreamAdapter.ViewHolder>() {

    private val expandedPositions = SparseBooleanArray() // To track which items are expanded
    private val retailerDataMap =
        mutableMapOf<Int, List<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>>() // To store retailer data for each position


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NAme: TextView = itemView.findViewById(R.id.NAme)
        val refer_id: TextView = itemView.findViewById(R.id.refer_id)
        val main_wallet: TextView = itemView.findViewById(R.id.main_wallet)
        val is_approved: TextView = itemView.findViewById(R.id.is_approved)
        val transfer: TextView = itemView.findViewById(R.id.transfer)
        val Reversetransfer: TextView = itemView.findViewById(R.id.Reversetransfer)
        val email: TextView = itemView.findViewById(R.id.email)
        val mobile: TextView = itemView.findViewById(R.id.Mobile)
        val walletreport: TextView = itemView.findViewById(R.id.walletreport)
        val usertype: TextView = itemView.findViewById(R.id.usertype)
        val retailer: TextView = itemView.findViewById(R.id.retailer)
        val report: ImageView = itemView.findViewById(R.id.report)
       // val card_top: CardView = itemView.findViewById(R.id.card_top)
        val recyclerRetailer: RecyclerView = itemView.findViewById(R.id.recyclerRetailer)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ZsmDownStreamAdapter.ViewHolder {
        return ZsmDownStreamAdapter.ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.lyt_zsm_downstream, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return downstramlist.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ZsmDownStreamAdapter.ViewHolder,
        position: Int
    ) {
        val data = downstramlist[position]

        holder.NAme.text = data.name
        holder.refer_id.text = "Refer ID: ${data.refer_id}"
        holder.main_wallet.text = "Balance: ${data.main_wallet}"
        holder.email.text = "Email: ${data.email}"
        holder.mobile.text = " ${data.mobile}"
        if(data.is_approved==true){
            holder.is_approved.text = "Status:- Approved"
        }else{
            holder.is_approved.text = "Status:- Pending"
        }
        holder.usertype.text = "User Type: ${data.user_type}"
        // Set up the retailer click listener
        holder.retailer.setOnClickListener {
            if (expandedPositions[position]) {
                // If already expanded, collapse it
                expandedPositions.delete(position)
                retailerDataMap.remove(position)
                holder.recyclerRetailer.visibility = View.GONE
            } else {
                // If not expanded, expand it and fetch retailer data
                expandedPositions.put(position, true)
                // Fetch retailer data and update retailerDataMap inside this callback
                downStreamClick.onItemClicked(
                    holder,
                    downstramlist,
                    position
                ) { fetchedRetailerData ->
                    // Store the fetched data in the map
                    retailerDataMap[position] = fetchedRetailerData
                    // Notify that this item has changed to update visibility
                    notifyItemChanged(position)
                }
            }
        }

        holder.transfer.setOnClickListener {
            downStreamClick.onTransferMoneyClicked(holder, downstramlist, position)
        }

        holder.Reversetransfer.setOnClickListener {
            downStreamClick.onReverseMoneyClicked(holder, downstramlist, position)
        }

        holder.report.setOnClickListener {
            downStreamClick.onViewReportClicked(holder, downstramlist, position)
        }
        holder.walletreport.setOnClickListener{
            downStreamClick.onViewWalletReportClicked(holder, downstramlist, position)
        }

        // Check if retailer data is available
        val retailerData = retailerDataMap[position]
        if (retailerData != null && expandedPositions[position]) {
            holder.recyclerRetailer.layoutManager = LinearLayoutManager(activity)
            val adapter = DownstreamRetailAdapter(activity, retailerData.toMutableList(),object : RetailerClick{
                override fun onItemClicked(
                    holder: DownstreamRetailAdapter.ViewHolder,
                    downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>,
                    position: Int,
                    callback: (List<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>) -> Unit
                ) {

                }

                override fun onTransferMoneyClicked(
                    holder: DownstreamRetailAdapter.ViewHolder,
                    downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>,
                    position: Int
                ) {

                }

                override fun onReverseMoneyClicked(
                    holder: DownstreamRetailAdapter.ViewHolder,
                    downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>,
                    position: Int
                ) {

                }

                override fun onViewReportClicked(
                    holder: DownstreamRetailAdapter.ViewHolder,
                    downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>,
                    position: Int
                ) {

                }

                override fun onViewWalletReportClicked(
                    holder: DownstreamRetailAdapter.ViewHolder,
                    downstramlistRetailer: MutableList<app.pay.retailers.responsemodels.downstreamRetailerResponse.Data>,
                    position: Int
                ) {

                }

            })
            holder.recyclerRetailer.adapter = adapter
            holder.recyclerRetailer.visibility = View.VISIBLE
        } else {
            holder.recyclerRetailer.visibility = View.GONE
        }

    }
}
