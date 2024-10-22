import android.app.Activity
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.R
import app.pay.pandapro.helperclasses.MyGlide
import app.pay.pandapro.interfaces.DynamicServicesClickListener
import app.pay.pandapro.responsemodels.allservices.Data
import app.pay.pandapro.retrofit.Constant

class DynamicServicesAdapter(
    private val activity: Activity,
    private val iconItems: List<Data>,
    private val click: DynamicServicesClickListener
) :
    RecyclerView.Adapter<DynamicServicesAdapter.ViewHolder>() {
    // Map of service names to custom icons
  /*  private val serviceIcons = mapOf(
        "Aeps Cash Deposit" to R.drawable.money_currency,
        "Credit Card" to R.drawable.credit_card,
        "Electricity" to R.drawable.ic_electricity_new,
        "DMT" to R.drawable.dmt,
        "Gas" to R.drawable.gas_tank,
        "Landline Postpaid" to R.drawable.landline,
        "Fastag" to R.drawable.fastag_logo,
        "DTH" to R.drawable.dth,
        "Broadband Postpaid" to R.drawable.ic_broadband_flat,
        "Water" to R.drawable.ic_waterdrop,
        "Mobile Postpaid" to R.drawable.bill_mobile,
        "Recharge" to R.drawable.ic_mobile_recharge_1,
        "Aeps Bank Withdraw" to R.drawable.cash_withdrawal,
        "Aeps Adhaar pay" to R.drawable.aadhaar_logo_svg,
        "CMS" to R.drawable.content_management_system,
        "Education Fees" to R.drawable.ic_education,
        "Quick Dhan" to R.drawable.dhan,
        "NCMC Recharge" to R.drawable.ic_mobile_recharge_1,
        "Hospital and Pathology" to R.drawable.hospital,
        "Insurance" to R.drawable.ic_insurance,
        "LPG Gas" to R.drawable.ic_gas_flat,
        "Mobile Prepaid" to R.drawable.ic_mobile_recharge,
        "Rental" to R.drawable.house_rent,
        "B2B" to R.drawable.add_business_full,
        "Cable TV" to R.drawable.ic_cable,
        "Municipal Services" to R.drawable.municipality,
        "Recurring Deposit" to R.drawable.recurring_deposit,
        "Metro Recharge" to R.drawable.metro,
        "Samson Hendrix" to R.drawable.sumsung,
        "Loan Repayment" to R.drawable.loan_1,
        "Aeps Adhaar Pay" to R.drawable.aadhaar_logo_svg,

        // Add more service names and corresponding icons
    )*/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImage: ImageView = itemView.findViewById(R.id.icon_image)
        val iconText: TextView = itemView.findViewById(R.id.icon_text)
        val route: LinearLayout = itemView.findViewById(R.id.route)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lyt_dynamic_service, parent, false)
        return ViewHolder(view)
    }
    private val filteredIconItems = iconItems.filter {
        !it.service_name.isNullOrEmpty() && !it.icon.isNullOrEmpty() && it.service_name != "Education Fees"
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Create a filtered list of icon items
      /*  val filteredIconItems = iconItems.filter {
            !it.service_name.isNullOrEmpty() && !it.icon.isNullOrEmpty()
        }*/

        // Check if the current position is within the bounds of the filtered list
        if (position < filteredIconItems.size) {
            val serviceName = filteredIconItems[position].service_name.toString()
            val iconUrl = filteredIconItems[position].icon.toString()

            // Load the icon using MyGlide if URL is available
            MyGlide.with(activity, Uri.parse(Constant.PIMAGE_URL + iconUrl), holder.iconImage)

            // Set the service name text
            holder.iconText.text = serviceName
            holder.route.visibility = if (serviceName == "Education Fees") {
                View.GONE // Hide the route layout
            } else {
                View.VISIBLE // Show the route layout
            }
            // Make the layout visible
            holder.route.visibility = View.VISIBLE
        } else {
            // If the position is out of bounds, hide the entire layout
            holder.route.visibility = View.GONE
        }

        // Set click listener for the icon
        holder.iconImage.setOnClickListener {
            click.onItemClicked(holder, filteredIconItems, position)
        }
    }

    override fun getItemCount(): Int {
        return iconItems.size
    }

}
