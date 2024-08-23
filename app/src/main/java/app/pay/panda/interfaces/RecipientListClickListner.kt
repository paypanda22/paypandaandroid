package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.responsemodels.dmtBeneficiaryList.Data

interface RecipientListClickListner {

    fun onRecipientItemClick(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}