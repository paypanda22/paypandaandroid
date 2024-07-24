package app.pay.panda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.helperclasses.FingerPrintScanner

interface ScannerListClick {
    fun onItemClicked(holder:RecyclerView.ViewHolder,model:List<FingerPrintScanner>,pos:Int)
}