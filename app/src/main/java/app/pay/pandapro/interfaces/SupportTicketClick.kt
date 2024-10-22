package app.pay.pandapro.interfaces

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.responsemodels.supportTickets.DataX
import com.google.android.material.textfield.TextInputEditText

interface SupportTicketClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<DataX>,pos:Int,type:String,editText: TextInputEditText,edt:View)
   }