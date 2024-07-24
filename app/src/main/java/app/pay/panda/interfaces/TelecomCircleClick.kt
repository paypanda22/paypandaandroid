package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.helperclasses.TelecomCircle

interface TelecomCircleClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<TelecomCircle>,pos:Int)
}