import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.activity.AepsOnBoardingActivity
import app.pay.panda.activity.AirtelCmsActivity
import app.pay.panda.activity.DmtActivity
import app.pay.panda.activity.RechargeActivity
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.interfaces.DynamicServicesClickListener
import app.pay.panda.responsemodels.allservices.Data
import app.pay.panda.retrofit.Constant

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lyt_dynamic_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serviceName = iconItems[position].service_name.toString()
        val iconUrl = iconItems[position].icon.toString()

        // Check if there's a custom icon for this service name
      /*  val customIcon = serviceIcons[serviceName]

        if (customIcon != null) {
            // Set the custom icon based on the service name
            holder.iconImage.setImageResource(customIcon)
        } else if (!iconUrl.isNullOrEmpty()) {*/
            // Load the icon using MyGlide if URL is available
            MyGlide.with(activity, Uri.parse(Constant.PIMAGE_URL + iconUrl), holder.iconImage)
       /* } else {
            // Set a default icon if no URL or custom icon is found
            holder.iconImage.setImageResource(R.drawable.ic_no_txn)
        }*/

        // Set the service name text
        holder.iconText.text = serviceName

        // Set click listener for the icon
        holder.iconImage.setOnClickListener {
            click.onItemClicked(holder, iconItems, position)
        }
    }

    override fun getItemCount(): Int {
        return iconItems.size
    }

}
