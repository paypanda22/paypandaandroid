package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.dmtBeneficiaryList.Data

interface RecipientListClickListner {

    fun onRecipientItemClick(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}