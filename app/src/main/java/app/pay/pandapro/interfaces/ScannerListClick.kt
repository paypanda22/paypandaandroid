package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.pandapro.helperclasses.FingerPrintScanner

interface ScannerListClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<FingerPrintScanner>,pos:Int)
}