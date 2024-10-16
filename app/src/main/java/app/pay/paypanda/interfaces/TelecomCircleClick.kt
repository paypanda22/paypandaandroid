package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.helperclasses.TelecomCircle

interface TelecomCircleClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<TelecomCircle>,pos:Int)
}