package app.pay.retailers.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.helperclasses.FingerPrintScanner

interface ScannerListClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<FingerPrintScanner>,pos:Int)
}