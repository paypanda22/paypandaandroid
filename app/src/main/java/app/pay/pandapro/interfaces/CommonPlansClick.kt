package app.pay.pandapro.interfaces

import androidx.recyclerview.widget.RecyclerView

interface CommonPlansClick {

    fun onGeneralPlanSelected(holder:RecyclerView.ViewHolder,model:List<CommonPlanData>,pos:Int)
}