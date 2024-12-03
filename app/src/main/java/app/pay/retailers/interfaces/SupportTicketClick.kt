package app.pay.retailers.interfaces

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.responsemodels.supportTickets.DataX
import com.google.android.material.textfield.TextInputEditText

interface SupportTicketClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<DataX>,pos:Int,type:String,editText: TextInputEditText,edt:View)
    fun onChooseFileClick(txt:TextView)
   }