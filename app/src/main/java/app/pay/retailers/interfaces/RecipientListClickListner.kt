package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.dmtBeneficiaryList.Data

interface RecipientListClickListner {

    fun onRecipientItemClick(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}