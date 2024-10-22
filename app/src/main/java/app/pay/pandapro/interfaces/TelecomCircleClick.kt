package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.helperclasses.TelecomCircle

interface TelecomCircleClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<TelecomCircle>,pos:Int)
}