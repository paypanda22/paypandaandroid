package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.helperclasses.TelecomCircle

interface TelecomCircleClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<TelecomCircle>,pos:Int)
}