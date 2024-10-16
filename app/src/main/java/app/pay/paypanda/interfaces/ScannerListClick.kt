package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.helperclasses.FingerPrintScanner

interface ScannerListClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<FingerPrintScanner>,pos:Int)
}