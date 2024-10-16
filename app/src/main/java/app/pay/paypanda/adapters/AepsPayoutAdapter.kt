package app.pay.paypanda.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.R
import app.pay.paypanda.interfaces.AepsPayoutClickListener
import app.pay.paypanda.responsemodels.payoutresponse.Data

class AepsPayoutAdapter(
    private val activity: Activity,
    private val list: List<Data>,
    private val click: AepsPayoutClickListener
) : RecyclerView.Adapter<AepsPayoutAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val rlStatus: RelativeLayout = itemView.findViewById(R.id.rlStatus)

        val tvBankName: TextView = itemView.findViewById(R.id.tvBankName)

        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvAckNo: TextView = itemView.findViewById(R.id.tvAckNo)
        val tvTxnId: TextView = itemView.findViewById(R.id.tvTxnId)
        val tvCustomerMobile: TextView = itemView.findViewById(R.id.tvCustomerMobile)
        val tvCustomerAadhaar: TextView = itemView.findViewById(R.id.tvCustomerAadhaar)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        val tvBankRrn: TextView = itemView.findViewById(R.id.tvBankRrn)
        val accname: TextView = itemView.findViewById(R.id.accname)
        val ivShare: ImageView = itemView.findViewById(R.id.ivShare)

        val refresh: AppCompatImageView = itemView.findViewById(R.id.refresh)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.lyt_aeps_payout_list, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvBankName.text=list[position].bank_name.toString()
        holder.tvAmount.text=list[position].amount.toString()
        holder.tvTxnId.text=list[position].txn_id.toString()
        holder.tvAckNo.text=list[position].charge_amount.toString()

        holder.tvCustomerAadhaar.text=list[position].bank_ifsc.toString()
        holder.tvCreatedAt.text=list[position].createdAt.toString()
        holder.tvBankRrn.text=list[position].bank_account_number.toString()
        holder.accname.text=list[position].account_name.toString()
        when(list[position].status) {
            2 -> {
                holder.rlStatus.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.btn_success
                    )
                )
                holder.tvStatus.text = "SUCCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.ivShare.visibility = VISIBLE
                holder.refresh.visibility = GONE
            }

            3 -> {
                holder.rlStatus.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.btn_failed
                    )
                )
                holder.tvStatus.text = "FAILED"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.ivShare.visibility = GONE
                holder.refresh.visibility = VISIBLE
            }

            else -> {
                holder.rlStatus.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        activity,
                        R.drawable.btn_pending
                    )
                )
                holder.tvStatus.text = "IN-PROCESS"
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.black))
                holder.ivShare.visibility = GONE
                holder.refresh.visibility = VISIBLE
            }
        }
        holder.ivShare.setOnClickListener {
            click.onItemClick(holder, list, position)
        }


        holder.refresh.setOnClickListener{
            click.onItemClickenquiry(holder, list, position)
        }
    }
}