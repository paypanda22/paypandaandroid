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
) : RecyclerView.Adapter<DynamicServicesAdapter.ViewHolder>() {

    // Create a filtered list of icon items that excludes empty service names and icons
    private val filteredIconItems = iconItems.filter {
        !it.service_name.isNullOrEmpty() &&
                !it.icon.isNullOrEmpty()
               /* it.service_name != "Education Fees"&&
                it.service_name != "Recharge"&&
                it.service_name!="DTH"*/
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImage: ImageView = itemView.findViewById(R.id.icon_image)
        val iconText: TextView = itemView.findViewById(R.id.icon_text)
        val route: LinearLayout = itemView.findViewById(R.id.route)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lyt_dynamic_service, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the service data at the current position
        val serviceItem = filteredIconItems[position]

        // Load the icon using MyGlide if URL is available
        MyGlide.with(activity, Uri.parse(Constant.PIMAGE_URL + serviceItem.icon), holder.iconImage)

        // Set the service name text
        holder.iconText.text = serviceItem.service_name

        // Show the route layout
        holder.route.visibility = View.VISIBLE

        // Set click listener for the icon
        holder.iconImage.setOnClickListener {
            click.onItemClicked(holder, filteredIconItems, position)
        }
    }

    override fun getItemCount(): Int {
        return filteredIconItems.size // Return the count of the filtered list
    }
}
