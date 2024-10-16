package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.dmtBeneficiaryList.Data

interface RecipientListClickListner {

    fun onRecipientItemClick(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}